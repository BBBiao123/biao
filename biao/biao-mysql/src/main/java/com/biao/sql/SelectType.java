package com.biao.sql;

public enum SelectType {
    EQUAL("等于"), GE("大于或等于"), LE("小于或等于"), GT("大于"), LT("小于"), NOTNULL("不为空"), NOTEQUAL("不等于"), LIKE("like"), ISNULL("为空"), IN("in");

    private String desc;

    SelectType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
