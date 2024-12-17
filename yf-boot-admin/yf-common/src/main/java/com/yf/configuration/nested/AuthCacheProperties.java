package com.yf.configuration.nested;

import lombok.Data;

import java.time.Duration;

@Data
public class AuthCacheProperties {
    /**
     * 缓存类型
     */
    private String type = "default";

    /**
     * 缓存前缀 ( 目前只针对 redis )，默认 JUST-AUTH:STATE:
     */
    private String prefix = "JUST-AUTH:STATE:";

    /**
     * 超时时长，目前只对redis缓存生效，默认3分钟
     */
    private Duration timeout = Duration.ofMinutes(3);
}
//@Getter
//@ToString
//enum AuthCacheType{
//    /**
//     * 使用JustAuth内置的缓存
//     */
//    DEFAULT,
//    /**
//     * 使用Redis缓存
//     */
//    REDIS
//}