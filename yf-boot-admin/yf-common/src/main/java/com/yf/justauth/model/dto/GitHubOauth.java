package com.yf.justauth.model.dto;

import lombok.Data;

@Data
public class GitHubOauth {
    private int code;
    private String msg;
    private Data data;

    @lombok.Data
    public static class Data {
        private String uuid;
        // 用户名
        private String username;
        // 昵称
        private String nickname;
        // 昵称
        private String avatar;
        private String blog;
        private String company;
        private String location;
        private String email;
        private String remark;
        private String gender;
        private String source;
        private Token token;

        @lombok.Data
        public static class Token {
            private String accessToken;
            private int expireIn;
            private String refreshToken;
            private String uid;
            private String openId;
            private String accessCode;
            private String unionId;
            private String scope;
            private String tokenType;
            private String idToken;
            private String macAlgorithm;
            private String macKey;
            private String code;
        }
    }
}