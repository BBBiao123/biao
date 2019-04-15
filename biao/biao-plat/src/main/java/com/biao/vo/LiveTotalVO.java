package com.biao.vo;

public class LiveTotalVO {

    private int curMinute;

    private long curCount;

    public String valueLabel;

    public String getValueLabel() {
        return valueLabel;
    }

    public void setValueLabel(String valueLabel) {
        this.valueLabel = valueLabel;
    }

    private String dataType;

    public int getCurMinute() {
        return curMinute;
    }

    public void setCurMinute(int curMinute) {
        this.curMinute = curMinute;
    }

    public long getCurCount() {
        return curCount;
    }

    public void setCurCount(long curCount) {
        this.curCount = curCount;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
