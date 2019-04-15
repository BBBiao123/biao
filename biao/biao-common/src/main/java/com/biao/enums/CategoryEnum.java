package com.biao.enums;

public enum CategoryEnum {

    NEWS("0", "", "资讯"),
    HEADER_NOTICE("document", "index-document", "首页公告"),
    HEADER_BANNER("advert", "index-advert", "首页BANNER"),
    HEADER_INTRODUCE("article", "website_introduce", "首页底部连接"),
    HEADER_APP_HELPCENTER("help_center", "app_help_center", "移动端帮助中心"),
    HEADER_HELPCENTER("help_center", "help_center", "帮助中心");
    private String moduel;

    private String keyword;

    private String message;

    CategoryEnum(String moduel, String keyword, String message) {
        this.moduel = moduel;
        this.keyword = keyword;
        this.message = message;
    }

    public String getModuel() {
        return moduel;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getMessage() {
        return message;
    }
}
