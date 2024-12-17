package com.yf.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.constants.RedisKeyConstants;
import com.yf.constants.SystemConstants;
import com.yf.converter.UserConverter;
import com.yf.exception.ServiceException;
import com.yf.mapper.system.SysOauthMapper;
import com.yf.mapper.system.SysUserMapper;
import com.yf.model.common.dto.UserAuthInfo;
import com.yf.model.common.enums.EnableStatusEnum;
import com.yf.model.system.bo.UserBo;
import com.yf.model.system.entity.SysOauth;
import com.yf.model.system.entity.SysUser;
import com.yf.model.system.entity.SysUserRole;
import com.yf.model.system.form.LoginForm;
import com.yf.model.system.form.UserForm;
import com.yf.model.system.query.UserPageQuery;
import com.yf.model.vo.UserInfoVO;
import com.yf.model.vo.UserPageVO;
import com.yf.oss.storage.FileStorageService;
import com.yf.oss.utils.FileUtils;
import com.yf.result.ResultCode;
import com.yf.security.utils.SecurityUtil;
import com.yf.service.ISysMenuService;
import com.yf.service.ISysRoleService;
import com.yf.service.ISysUserRoleService;
import com.yf.service.ISysUserService;
import com.yf.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息表-SysUserIServiceImpl
 *
 * @author YiFei
 * @since 2024-04-18 16:59:58
 */
@Service("sysUserService")
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final RedisUtil redisUtil;
    private final UserConverter userConverter;
    private final SysOauthMapper oauthMapper;
    private final ISysMenuService menuService;
    private final ISysRoleService roleService;
    private final ISysUserRoleService userRoleService;
    private final TaskExecutor ioIntensiveExecutor;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 分页查询用户
     *
     * @param queryParams 查询参数
     * @return 用户分页数据
     */
    @Override
    public IPage<UserPageVO> getUserPage(UserPageQuery queryParams) {
        // 1. 创建分页对象
        Page<UserBo> page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());
        // 2. 分页查询
        Page<UserBo> userPage = this.baseMapper.getUserPage(page, queryParams);
        // 3. bo 转 vo
        return userConverter.pageBo2pageVo(userPage);
    }

    @Override
    public UserAuthInfo getUserAuthInfo(LoginForm principal) {
        // 查询除了 permissions,dataScope 以外字段
        UserAuthInfo userAuthInfo = this.baseMapper.getUserAuthInfo(principal);
        if (userAuthInfo != null) {
            Set<String> roles = userAuthInfo.getRoles();
            // 获取 userAuthInfo 的角色信息，不为空则查询权限信息 ( permissions , dataScope )
            if (!CollectionUtils.isEmpty(roles)) {
                // 获取 permissions
                userAuthInfo.setPermissions(menuService.listPermissions(roles));
                // 获取 dataScope
                userAuthInfo.setDataScope(roleService.getMaximumDataScope(roles));
            } else {
                // 用户未分配角色返回报错信息
                throw new ServiceException(ResultCode.ROLE_NOT_ASSIGNED);
            }
        }
        return userAuthInfo;
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 当前用户信息
     */
    @Override
    @Cacheable(value = RedisKeyConstants.SYSTEM_ME_CACHE_PREFIX, key = "#userId")
    public UserInfoVO getCurrentUserInfo(Long userId) {
        // 1. 查询数据库当前用户信息
        SysUser user = this.lambdaQuery()
                .select(
                        SysUser::getId,
                        SysUser::getUsername,
                        SysUser::getNickname,
                        SysUser::getAvatar,
                        SysUser::getPhoneNumber,
                        SysUser::getEmail
                )
                .eq(SysUser::getId, userId)
                .eq(SysUser::getStatus, EnableStatusEnum.ENABLE)
                .one();
        if (user == null) {
            throw new ServiceException(ResultCode.AUTH_USER_INFO_ERROR);
        }
        // 2. SysUser 转换为 UserInfoVo
        UserInfoVO userInfoVo = userConverter.user2userInfoVo(user);

        // 3.填写用户权限信息
        Set<String> permissions = redisUtil.getCacheObject(RedisKeyConstants.USER_PERMISSIONS_CACHE_PREFIX + userId);
        userInfoVo.setPermissions(permissions);
        // 4. 填充用户角色信息
        Set<String> userRoles = SecurityUtil.getUserRoles();
        userInfoVo.setRoles(userRoles);
        // 5. 返回数据
        return userInfoVo;
    }

    /**
     * 新增用户 （ 管理端使用 ）
     *
     * @param userForm 用户表单信息
     * @return 是否新增用户成功
     */
    @Override
    @Transactional
    public Long saveUser(UserForm userForm) {
        // 1. 查询用户名、邮箱、手机号是否重复 ( 这里不做校验是因为有 JSR303 校验 )
        isDuplicate(null, userForm);
        // 2. 设置密码 （使用 Security PasswordEncoder ）
        String encodeDefaultPassword = passwordEncoder.encode(SystemConstants.SYSTEM_DEFAULT_PASSWORD);
        // 3. 保存用户信息
        // 3.1 转换对象
        SysUser sysUser = userConverter.userForm2entity(userForm);
        sysUser.setPassword(encodeDefaultPassword);
        // 3.2 存入数据库
        boolean isSaveUser = this.save(sysUser);
        // 4. 保存角色信息
        if (isSaveUser) {
            // 4.1 构建用户角色信息
            List<SysUserRole> collect = userForm.getRoleIds().stream()
                    .map(item -> new SysUserRole(sysUser.getId(), item))    // 构建SysUserRole对象
                    .toList();
            // 4.2 保存用户角色信息
            userRoleService.saveBatch(collect);
            return sysUser.getId();
        }
        return null;
    }

    /**
     * 删除用户 : 根据 ids
     * 加入 @Transactional 是因为内部是 1000 为一条删除，会有事务
     *
     * @return 是否删除成功
     */
    @Transactional
    @Override
    public boolean deleteUser(List<Long> userIds) {
        // 1. 删除用户缓存信息
        Set<String> cacheIds = userIds.stream().map((userId) -> RedisKeyConstants.SYSTEM_ME_CACHE_PREFIX + userId).collect(Collectors.toSet());
        redisUtil.deleteObject(cacheIds);
        // 2. 删除用户头像 （注 : 可以交由 MQ 执行 , 或不执行）
        this.removeUserAvatar(userIds);
        // 3. 删除用户信息
        this.removeBatchByIds(userIds);
        // 4. 删除用户绑定的角色
        userRoleService.lambdaUpdate()
                .in(SysUserRole::getUserId, userIds)
                .remove();
        // 5. 删除用户的授权信息
        oauthMapper.delete(Wrappers.<SysOauth>lambdaQuery()
                .in(SysOauth::getUserId, userIds));
        return true;
    }

    /**
     * 异步删除已经删除的用户的
     *
     * @param userIds
     */
    private void removeUserAvatar(List<Long> userIds) {
        // 1. 获取所有被删除的用户信息
        List<SysUser> list = this.lambdaQuery()
                .select(SysUser::getAvatar)
                .in(SysUser::getId, userIds)
                .list();
        // 2. 删除用户头像
        for (SysUser sysUser : list) {
            String earlyAvatarUrl = sysUser.getAvatar();
            if (StringUtils.hasText(earlyAvatarUrl) && earlyAvatarUrl.startsWith(fileStorageService.getFileStorageEndpoint())) {
                ioIntensiveExecutor.execute(() -> {
                    // 5.1 解析文件路径
                    String savePath = FileUtils.parseFileSavePath(SystemConstants.DEFAULT_AVATAR_SAVE_PATH, earlyAvatarUrl);
                    // 5.2 解析文件名
                    String fileName = earlyAvatarUrl.substring(earlyAvatarUrl.lastIndexOf('/') + 1);
                    // 5.3 删除文件
                    fileStorageService.deleteFile(savePath, fileName);
                });
            }
        }
    }

    /**
     * 修改用户 : 根据 id
     *
     * @param userId   用户id
     * @param userForm 表单
     * @return 是否修改成功
     */
    @Transactional
    @Override
    @CacheEvict(value = RedisKeyConstants.SYSTEM_ME_CACHE_PREFIX, key = "#userId")
    public boolean updateUser(Long userId, UserForm userForm) {
        // 1. 查看是否修改是否重复
        isDuplicate(userId, userForm);
        // 2. 转换为 entity 设置id
        SysUser sysUser = userConverter.userForm2entity(userForm).setId(userId);
        // 3. 修改用户数据
        boolean userUpdated = this.updateById(sysUser);
        // 4. 修改成功后修改角色信息
        if (userUpdated) {
            return userRoleService.saveUserRole(userId, userForm.getRoleIds());
        }
        return false;
    }

    /**
     * 修改用户密码
     *
     * @param userId   用户id
     * @param password 密码
     * @return 是否修改成功
     */
    @Override
    public boolean resetUserPassword(Long userId, String password) {
        // 1. 校验密码长度
        if (password.length() < 8 || password.length() > 16) {
            throw new ServiceException(ResultCode.USER_PASSWORD_LENGTH);
        }
        // 2. 加密
        String resetPassword = passwordEncoder.encode(password);
        // 3. 存储到数据库
        return this.lambdaUpdate()
                .eq(SysUser::getId, userId)
                .set(SysUser::getPassword, resetPassword)
                .update();
    }

    /**
     * 修改用户状态
     *
     * @param userId 用户Id
     * @param status 状态
     * @return 是否修改成功
     */
    @Override
    @CacheEvict(value = RedisKeyConstants.SYSTEM_ME_CACHE_PREFIX, key = "#userId")
    public boolean updateUserStatus(Long userId, Boolean status) {
        return this.lambdaUpdate()
                .eq(SysUser::getId, userId)
                .set(SysUser::getStatus, status)
                .update();
    }

    /**
     * 用户表单数据
     *
     * @param userId 用户Id
     * @return 用户表单数据
     */
    @Override
    public UserForm getUserForm(Long userId) {
        // 1. 查询用户信息
        SysUser oneSysUser = this.lambdaQuery()
                .select()
                .eq(SysUser::getId, userId)
                .one();
        // 2. entity 转换为 form 对象
        UserForm userForm = userConverter.entity2form(oneSysUser);
        // 3. 查询角色信息
        List<SysUserRole> roles = userRoleService.lambdaQuery()
                .select(SysUserRole::getRoleId)
                .eq(SysUserRole::getUserId, userId)
                .list();
        // 4. 设置用户角色信息
        userForm.setRoleIds(roles.stream().map(SysUserRole::getRoleId).toList());
        return userForm;
    }

    /**
     * 自动注册用户 : 1. 传入空则自动注册 , 2. 传入 userForm 则 填充唯一字段不为空的数据, 例如 ↓
     *
     * @param userForm 用户表单 -> 只会获取 username , email , phoneNumber
     * @return 是否注册成功
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long autoRegisterUser(UserForm userForm) {
        // 1. 默认角色 、 默认部门 、 随机、用户名昵称 、 无密码 、 无性别
        SysUser.SysUserBuilder builder = SysUser.builder()
                .username(RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER, 6))
                .gender(0)
                .deptId(SystemConstants.DEFAULT_DEPT);
        // 1. 判断 userForm 是否为空 , 空则走默认
        if (userForm == null) {
            // 1.1 随机用户名
            // 1.1.1 重试次数为 2
            int retry = 2;
            boolean exist = true;
            String username = "";
            while (retry-- > 0) {
                username = RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER, 8);
                // 判断用户名是否重复
                exist = this.lambdaQuery()
                        .eq(SysUser::getUsername, username)
                        .exists();
                // 成功则跳出
                if (!exist) {
                    break;
                }
            }
            // 1.1.2 两次失败使用 UUID
            if (exist) {
                builder.username(UUID.fastUUID().toString());
            }
            // 1.1.3 填充用户名
            builder.username(username);
        } else {
            // 1.2 用户名不为空则填入
            if (StringUtils.hasText(userForm.getUsername())) {
                builder.username(userForm.getUsername());
            }
            // 1.3 用户邮箱不为空则填入
            if (StringUtils.hasText(userForm.getEmail())) {
                builder.email(userForm.getEmail());
            }
            // 1.4 用户手机号不为空则填入
            if (StringUtils.hasText(userForm.getPhoneNumber())) {
                builder.phoneNumber(userForm.getPhoneNumber());
            }
        }
        SysUser sysUser = builder.build();
        // 2. 存储用户信息
        boolean saveUser = this.save(sysUser);
        if (!saveUser) {
            throw new ServiceException(ResultCode.USER_AUTO_REGISTER);
        }
        // 3. 存储角色信息
        boolean saveUserRole = userRoleService.save(SysUserRole.builder()
                .userId(sysUser.getId())
                .roleId(SystemConstants.DEFAULT_ROLE)
                .build());
        if (!saveUserRole) {
            throw new ServiceException(ResultCode.USER_AUTO_REGISTER);
        }
        // 返回用户名
        return sysUser.getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long autoRegisterUser() {
        return autoRegisterUser(null);
    }

    /**
     * 查询用户是否重复 : form不会全为空 , 否则报错
     */
    private void isDuplicate(Long userId, UserForm userForm) {
        boolean hasUsername = StringUtils.hasText(userForm.getUsername());
        boolean hasEmail = StringUtils.hasText(userForm.getEmail());
        boolean hasPhoneNumber = StringUtils.hasText(userForm.getPhoneNumber());
        // TODO OR 导致索引失效，后续改成 SQL 连接( UNION ALL )
        SysUser oneSysUser = this.lambdaQuery()
                .select(SysUser::getId, SysUser::getUsername, SysUser::getEmail, SysUser::getPhoneNumber)
                .or(wrapper -> wrapper
                        .eq(hasUsername, SysUser::getUsername, userForm.getUsername())
                        .eq(hasEmail, SysUser::getEmail, userForm.getEmail())
                        .eq(hasPhoneNumber, SysUser::getPhoneNumber, userForm.getPhoneNumber())
                )
                .one();
        if (oneSysUser == null || oneSysUser.getId().equals(userId)) {
            return;
        }
        // 1.1 用户名重复
        if (hasUsername && userForm.getUsername().equals(oneSysUser.getUsername())) {
            throw new ServiceException(ResultCode.USER_NAME_DUPLICATE);
        }
        // 1.2 手机号重复
        if (hasPhoneNumber && userForm.getPhoneNumber().equals(oneSysUser.getPhoneNumber())) {
            throw new ServiceException(ResultCode.USER_PHONE_NUMBER_DUPLICATE);
        }
        // 1.3 邮箱重复
        if (hasEmail && userForm.getEmail().equals(oneSysUser.getEmail())) {
            throw new ServiceException(ResultCode.USER_EMAIL_DUPLICATE);
        }
    }
}

