package com.yf.justauth.model.dto;

import lombok.Data;

@Data
public class BaiduOauth {
    private int code;
    private Data data;

    @lombok.Data
    public static class Data {
        private String avatar;
        private Gender gender;
        private String nickname;
        private RawUserInfo rawUserInfo;
        private Source source;
        private Token token;
        private String username;
        private String uuid;
    }

    @lombok.Data
    public static class RawUserInfo {
        private String birthday;
        private String openid;
        private String sex;
        private String is_realname;
        private String portrait;
        private String is_bind_mobile;
        private String blood;
        private String username;
    }

    @lombok.Data
    public static class Token {
        private String accessToken;
        private long expireIn;
        private String refreshToken;
        private String scope;
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public enum Source {
        BAIDU
        // Add other sources as needed
    }
}