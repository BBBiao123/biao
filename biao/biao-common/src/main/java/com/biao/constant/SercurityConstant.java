package com.biao.constant;

public class SercurityConstant {

    public static final String SESSION_TOKEN_HEADER = "stoken";

    public static final String SESSION_TOKEN_REDIS_NAMESAPCE = "session:";

    public static final String SESSION_TOKEN_REDIS_USER = "user";

    public static final String AUTH_USERNAME_REDIS_NAMESAPCE = "auth:";

    public static final String VALID_CODE_USERNAME_REDIS_NAMESAPCE = "validCode:";

    public static final String VALID_TIME_USERNAME_REDIS_NAMESAPCE = "validTime:";

    public static final long REDIS_EXPIRE_TIME_HALF_HOUR = 1800;

    public static final long REDIS_EXPIRE_TIME_ONE_HOUR = 3600;

    /**
     * 用户session失效时间
     */
    public static final long REDIS_EXPIRE_TIME_SESSION = 1800;

    public enum AuthType {

        PASS_AUTH("auth_true", "权限认证通过"), REFUSED_AUTH("auth_false", "权限认证拒绝");

        private String code;

        private String message;

        AuthType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public static final String redisTicketOutKey(String userId) {
        return String.format("user-session-ticketout:%s", userId);
    }

    public static final String redisUserCollectKey(String userId) {
        return String.format("user-session-collect:%s", userId);
    }
}
