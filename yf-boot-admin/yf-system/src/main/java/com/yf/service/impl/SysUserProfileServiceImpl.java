package com.yf.service.impl;

import com.yf.constants.RedisKeyConstants;
import com.yf.constants.SystemConstants;
import com.yf.converter.OauthConverter;
import com.yf.converter.UserConverter;
import com.yf.exception.ServiceException;
import com.yf.justauth.factory.JustAuthFactory;
import com.yf.model.system.entity.SysDept;
import com.yf.model.system.entity.SysOauth;
import com.yf.model.system.entity.SysRole;
import com.yf.model.system.entity.SysUser;
import com.yf.model.system.form.ResetUserPasswordForm;
import com.yf.model.system.form.UserProfileForm;
import com.yf.model.vo.UserProfileInfoVO;
import com.yf.model.vo.UserProfileOauthVo;
import com.yf.oss.storage.FileStorageService;
import com.yf.oss.utils.FileUtils;
import com.yf.oss.utils.NSFWAnalyzerUtils;
import com.yf.result.ResultCode;
import com.yf.security.model.dto.SysUserDetails;
import com.yf.security.utils.SecurityUtil;
import com.yf.service.*;
import com.yf.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 个人中心服务类
 *
 * @author : YiFei
 * @since : 2024/7/22 19:09
 */
@Slf4j
@Service("sysUserProfileService")
@RequiredArgsConstructor
public class SysUserProfileServiceImpl implements ISysUserProfileService {

    private final UserConverter userConverter;
    private final OauthConverter oauthConverter;
    private final ISysUserService userService;
    private final ISysDeptService deptService;
    private final ISysOauthService oauthService;
    private final ISysRoleService roleService;
    private final JustAuthFactory justAuthFactory;
    private final FileStorageService fileStorageService;
    private final NSFWAnalyzerUtils nsfwAnalyzerUtils;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private final TaskExecutor ioIntensiveExecutor;

    /**
     * 获取用户个人信息
     *
     * @return 用户个人信息
     */
    @Override
    public UserProfileInfoVO getUserProfileInfo() {
        SysUserDetails user = SecurityUtil.getUser();
        Set<String> userRoleCodes = SecurityUtil.getUserRoles();
        if (user == null) {
            throw new ServiceException(ResultCode.AUTH_USER_NOT_LOGIN);
        }
        // 1. 获取用户基本信息
        CompletableFuture<SysUser> asyncSysUser = CompletableFuture.supplyAsync(
                () -> userService.lambdaQuery()
                        .eq(SysUser::getId, user.getUserId())
                        .one()
        );

        // 2. 获取用户角色信息
        CompletableFuture<List<String>> asyncUserRoles = CompletableFuture.supplyAsync(() -> {
                    List<SysRole> list = roleService.lambdaQuery()
                            .in(SysRole::getCode, userRoleCodes)
                            .list();
                    return list.stream().map(SysRole::getName).toList();
                }
        );
        // 3. 获取用户部门信息
        CompletableFuture<String> asyncDept = CompletableFuture.supplyAsync(
                () -> deptService.lambdaQuery()
                        .select(SysDept::getName)
                        .eq(SysDept::getId, user.getDeptId())
                        .one()
                        .getName()
        );
        // 4. 获取用户第三方授权信息
        CompletableFuture<List<UserProfileOauthVo>> asyncOauth = CompletableFuture.supplyAsync(() -> {
                    List<SysOauth> list = oauthService.lambdaQuery()
                            .eq(SysOauth::getUserId, user.getUserId())
                            .orderByAsc(SysOauth::getCreateTime)
                            .list();
                    return oauthConverter.list2profileOauthVo(list);
                }
        );
        // 5. 等待请求整合完成
        CompletableFuture<UserProfileInfoVO> userProfileInfoFuture = CompletableFuture.allOf(asyncSysUser, asyncUserRoles, asyncDept, asyncOauth)
                .thenApply(value -> {
                    try {
                        // 5.1 等待数据请求完成
                        SysUser sysUser = asyncSysUser.get();
                        List<String> userRoles = asyncUserRoles.get();
                        String deptName = asyncDept.get();
                        List<UserProfileOauthVo> oatuhList = asyncOauth.get();
                        // 5.2 返回 userProfileInfo
                        UserProfileInfoVO userProfileInfo = userConverter.user2profileVo(sysUser);
                        userProfileInfo
                                .setRoles(userRoles)
                                .setDeptName(deptName)
                                .setOauthInfo(oatuhList);
                        return userProfileInfo;
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("Error while fetching user profile info", e);
                        throw new ServiceException(ResultCode.SYSTEM_EXECUTION_ERROR);
                    }
                });
        // 6. 等待请求整合完成后返回
        try {
            return userProfileInfoFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while fetching user profile info", e);
            throw new ServiceException(ResultCode.SYSTEM_EXECUTION_ERROR);
        }
    }

    /**
     * 绑定第三方平台账户
     *
     * @param type  授权平台
     * @param oauth 授权信息
     * @return 是否绑定成功
     */
    @Override
    public boolean bindThirdParty(String type, AuthCallback oauth) {
        Long userId = SecurityUtil.getUserId();
        // 1. 通过工厂类获取对应的第三方授权请求实例
        AuthRequest authRequest = justAuthFactory.getAuthRequest(type);
        try {
            // 2. 使用授权信息进行登录，获取第三方用户信息
            AuthResponse<AuthUser> login = authRequest.login(oauth);
            AuthUser loginData = login.getData();
            // 3. 判断账号是否已经被绑定
            String uuid = loginData.getUuid();
            SysOauth alreadyBindOauth = oauthService.lambdaQuery()
                    .eq(SysOauth::getPlatformUserId, uuid)
                    .one();
            if (alreadyBindOauth != null) {
                throw new ServiceException(ResultCode.AUTH_ALREADY_BIND_ERROR);
            }
            // 4. 存储第三方用户信息
            return oauthService.save(SysOauth.builder()
                    .userId(userId)
                    .platformName(loginData.getSource())
                    .platformUserId(uuid)
                    .platformUsername(loginData.getUsername())
                    .platformUserAvatar(loginData.getAvatar())
                    .build());
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            } else {
                log.error("第三方平台 {} 绑定失败: ", type, e);
                throw new ServiceException(ResultCode.AUTH_BIND_THIRD_PARTY_ERROR);
            }
        }
    }

    /**
     * 解绑第三方平台账户
     *
     * @param oauthId 授权ID
     * @return 是否解绑成功
     */
    @Override
    public boolean unbindThirdParty(String oauthId) {
        Long userId = SecurityUtil.getUserId();
        return oauthService.lambdaUpdate()
                .and(wrapper -> wrapper
                        .eq(SysOauth::getUserId, userId)
                        .eq(SysOauth::getId, oauthId)
                )
                .remove();
    }

    /**
     * 修改用户头像
     *
     * @param avatar 用户头像
     * @return 头像地址
     */
    @Override
    public String updateAvatar(MultipartFile avatar) {
        Long userId = SecurityUtil.getUserId();
        // 1. 鉴定文件是否含有暴力倾向
        try {
            if (nsfwAnalyzerUtils.isNsfwFile(avatar)) {
                throw new ServiceException(ResultCode.FILE_VIOLATIONS);
            }
        } catch (Exception e) {
            // 防止文件解析错误 , 本身是 zip 文件 , 但是文件名修改为 png
            throw new ServiceException(e.getMessage());
        }
        // 2. 重命名文件
        MultipartFile file = FileUtils.renameFile(avatar);
        // 3. 上传文件
        String fileUrl = fileStorageService.uploadFile(SystemConstants.DEFAULT_AVATAR_SAVE_PATH, file);
        // 4. 查询用户头像信息 ( 为异步删除原头像提供地址 )
        String earlyAvatarUrl = userService.lambdaQuery()
                .select(SysUser::getAvatar)
                .eq(SysUser::getId, userId)
                .one().getAvatar();
        // 5. 异步删除原文件 ( 无头像、默认头像则不删除 | 可替换为MQ处理 )
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

        // 6. 修改用户信息
        userService.lambdaUpdate()
                .eq(SysUser::getId, userId)
                .set(SysUser::getAvatar, fileUrl)
                .update();

        // 7. 清除缓存数据 ( 可以选择删除或者修改 )
        redisUtil.deleteObject(RedisKeyConstants.SYSTEM_ME_CACHE_PREFIX + userId);
        return fileUrl;
    }

    /**
     * 修改用户个人信息
     *
     * @param form 表单
     * @return 是否修改成功
     */
    @Override
    public boolean updateUserProfile(UserProfileForm form) {
        Long userId = SecurityUtil.getUserId();

        userService.lambdaUpdate()
                .eq(SysUser::getId, userId)
                .update(userConverter.profileForm2entity(form));

        return true;
    }

    /**
     * 修改个人密码
     *
     * @param resetUserPasswordForm 修改密码表单
     * @return 是否修改整个
     */
    @Override
    public boolean updateUserPassword(ResetUserPasswordForm resetUserPasswordForm) {
        Long userId = SecurityUtil.getUserId();
        if (!resetUserPasswordForm.getNewPassword().equals(resetUserPasswordForm.getCheckPassword())) {
            throw new ServiceException(ResultCode.USER_RESET_PASSWORD);
        }
        // 1. 查询用户信息
        SysUser oldUserPassword = userService.lambdaQuery()
                .select(SysUser::getPassword)
                .eq(SysUser::getId, userId)
                .one();
        if (oldUserPassword.getPassword() != null) {
            // 2. 校验旧密码是否匹配  ( 无密码则不校验 )
            if (!passwordEncoder.matches(resetUserPasswordForm.getOldPassword(), oldUserPassword.getPassword())) {
                throw new ServiceException(ResultCode.USER_RESET_OLD_PASSWORD);
            }
        }
        // 3. 加密新密码
        String newPassword = passwordEncoder.encode(resetUserPasswordForm.getNewPassword());
        // 4. 返回是否存储成功
        return userService.lambdaUpdate()
                .eq(SysUser::getId, userId)
                .set(SysUser::getPassword, newPassword)
                .update();
    }

    /**
     * 校验用户是否有密码
     *
     * @return 是否有密码
     */
    @Override
    public boolean checkPasswordExistence() {
        // 1. 获取当前用户Id
        Long userId = SecurityUtil.getUserId();
        // 2. 返回用户是否有密码
        return userService.lambdaQuery()
                .eq(SysUser::getId, userId)
                .isNull(SysUser::getPassword)
                .exists();
    }

    /**
     * 修改用户名 7天 一次
     *
     * @param username 用户名
     * @return 是否修改成功
     */
    @Override
    public boolean updateUsername(String username) {
        // 1. 获取当前用户
        Long userId = SecurityUtil.getUserId();
        // 2. 修改用户名
        return userService.lambdaUpdate()
                .eq(SysUser::getId, userId)
                .set(SysUser::getUsername, username)
                .update();
    }

}
