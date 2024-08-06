package com.yf.justauth.factory;

import cn.hutool.core.util.EnumUtil;
import com.yf.configuration.JustAuthConfiguration;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.request.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * JustAuthFactory ( JustAuth 工厂 )
 *
 * @author YiFei
 * @since 2024/4/16 23:11
 */
@Component
@RequiredArgsConstructor
public class JustAuthFactory {

    private final JustAuthConfiguration justAuthConfiguration;
    private final AuthStateCache authStateCache;

    /**
     * 通过名字获取 AuthRequest
     *
     * @param type 第三方平台名
     * @return AuthRequest
     */
    public AuthRequest getAuthRequest(String type) {
        // 1. 校验字符串是否为空
        if (!StringUtils.hasText(type)) {
            throw new AuthException(AuthResponseStatus.NO_AUTH_SOURCE);
        }
        // 2. 查找对应 AuthDefaultSource
        AuthDefaultSource authDefaultSource;

        try {
            // 可以使用  org.apache.commons.lang3.EnumUtils ( 注: 能获取到 Enum 即可 )
            authDefaultSource = EnumUtil.fromString(AuthDefaultSource.class, type.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 未匹配到第三方平台信息
            throw new RuntimeException("无对应第三方登录平台 => " + type.toUpperCase());
        }
        // 3. 查找配置中是否存在该type
        AuthConfig config = justAuthConfiguration.getType().get(authDefaultSource.name());
        if (config == null) {
            throw new RuntimeException("未配置相应第三方平台数据 => " + authDefaultSource.getName());
        }
        // 4. 配置 http config ( 默认不修改 )
        // 5. 创建对应的 AuthRequest
        return getAuthRequest(authDefaultSource, config);
    }

    /**
     * 通过 authDefaultSource 获取 AuthRequest
     *
     * @param authDefaultSource 类型
     * @return AuthRequest
     */
    public AuthRequest getAuthRequest(AuthDefaultSource authDefaultSource) {
        // 1. 查找配置中是否存在该type
        AuthConfig config = justAuthConfiguration.getType().get(authDefaultSource.name());
        if (config == null) {
            throw new RuntimeException("未配置相应第三方平台数据 => " + authDefaultSource.getName());
        }
        return getAuthRequest(authDefaultSource, config);
    }

    private AuthRequest getAuthRequest(AuthDefaultSource authDefaultSource, AuthConfig config) {
        return switch (authDefaultSource) {
            case GITHUB -> new AuthGithubRequest(config, authStateCache);
            case WEIBO -> new AuthWeiboRequest(config, authStateCache);
            case GITEE -> new AuthGiteeRequest(config, authStateCache);
            case DINGTALK -> new AuthDingTalkRequest(config, authStateCache);
            case DINGTALK_ACCOUNT -> new AuthDingTalkAccountRequest(config, authStateCache);
            case BAIDU -> new AuthBaiduRequest(config, authStateCache);
            case CSDN -> new AuthCsdnRequest(config, authStateCache);       // CSDN的授权开放平台已经下线
            case CODING -> new AuthCodingRequest(config, authStateCache);
            case OSCHINA -> new AuthOschinaRequest(config, authStateCache);
            case ALIPAY -> new AuthAlipayRequest(config, authStateCache);   // 建议自己实现
            case QQ -> new AuthQqRequest(config, authStateCache);
            case WECHAT_OPEN -> new AuthWeChatOpenRequest(config, authStateCache);
            case WECHAT_MP -> new AuthWeChatMpRequest(config, authStateCache);
            case WECHAT_ENTERPRISE -> new AuthWeChatEnterpriseQrcodeRequest(config, authStateCache);
            case WECHAT_ENTERPRISE_WEB -> new AuthWeChatEnterpriseWebRequest(config, authStateCache);
            case TAOBAO -> new AuthTaobaoRequest(config, authStateCache);
            case GOOGLE -> new AuthGoogleRequest(config, authStateCache);
            case FACEBOOK -> new AuthFacebookRequest(config, authStateCache);
            case DOUYIN -> new AuthDouyinRequest(config, authStateCache);
            case LINKEDIN -> new AuthLinkedinRequest(config, authStateCache);
            case MICROSOFT -> new AuthMicrosoftRequest(config, authStateCache);
            case MI -> new AuthMiRequest(config, authStateCache);
            case TOUTIAO -> new AuthToutiaoRequest(config, authStateCache);
            case TEAMBITION -> new AuthTeambitionRequest(config, authStateCache);
            case RENREN -> new AuthRenrenRequest(config, authStateCache);
            case PINTEREST -> new AuthPinterestRequest(config, authStateCache);
            case STACK_OVERFLOW -> new AuthStackOverflowRequest(config, authStateCache);
            case HUAWEI -> new AuthHuaweiRequest(config, authStateCache);
            case GITLAB -> new AuthGitlabRequest(config, authStateCache);
            case KUJIALE -> new AuthKujialeRequest(config, authStateCache);
            case ELEME -> new AuthElemeRequest(config, authStateCache);
            case MEITUAN -> new AuthMeituanRequest(config, authStateCache);
            case TWITTER -> new AuthTwitterRequest(config, authStateCache);
            case FEISHU -> new AuthFeishuRequest(config, authStateCache);
            case JD -> new AuthJdRequest(config, authStateCache);
            case ALIYUN -> new AuthAliyunRequest(config, authStateCache);
            case XMLY -> new AuthXmlyRequest(config, authStateCache);
            case AMAZON -> new AuthAmazonRequest(config, authStateCache);
            case SLACK -> new AuthSlackRequest(config, authStateCache);
            case LINE -> new AuthLineRequest(config, authStateCache);
            case OKTA -> new AuthOktaRequest(config, authStateCache);
            default -> throw new RuntimeException("无对应第三方登录平台 =>" + authDefaultSource.getName());
        };
    }
}
