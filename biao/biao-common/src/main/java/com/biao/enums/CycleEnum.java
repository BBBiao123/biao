package com.biao.enums;

/**
 * 周期枚举
 */
public enum CycleEnum {
    DAY("1", "DAY"),
    WEEK("2", "WEEK"),
    MONTH("3", "MONTH"),
    YEAR("4", "YEAR")
    ;
    private String code;
    private String name;
    CycleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(String value) {
        return this.getCode().equals(value);
    }
}
