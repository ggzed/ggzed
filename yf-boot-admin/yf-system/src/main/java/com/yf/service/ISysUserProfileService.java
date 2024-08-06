package com.yf.service;

import com.yf.model.form.ResetUserPasswordForm;
import com.yf.model.form.UserProfileForm;
import com.yf.model.vo.UserProfileInfoVO;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人中心服务类
 *
 * @author : YiFei
 * @since : 2024/7/22 19:08
 */
public interface ISysUserProfileService {
    /**
     * 获取用户个人信息
     *
     * @return 用户个人信息
     */
    UserProfileInfoVO getUserProfileInfo();

    /**
     * 绑定第三方平台账户
     *
     * @param type  授权平台
     * @param oauth 授权信息
     * @return 是否绑定成功
     */
    boolean bindThirdParty(String type, AuthCallback oauth);

    /**
     * 解绑第三方平台账户
     *
     * @param oauthId 授权ID
     * @return 是否解绑成功
     */
    boolean unbindThirdParty(String oauthId);

    /**
     * 修改用户头像
     *
     * @param avatar 用户头像
     * @return 头像地址
     */
    String updateAvatar(MultipartFile avatar);

    /**
     * 修改用户个人信息
     *
     * @param form 表单
     * @return 是否修改成功
     */
    boolean updateUserProfile(UserProfileForm form);

    /**
     * 修改个人密码
     *
     * @param resetUserPasswordForm 修改密码表单
     * @return 是否修改整个
     */
    boolean updateUserPassword(ResetUserPasswordForm resetUserPasswordForm);
}
