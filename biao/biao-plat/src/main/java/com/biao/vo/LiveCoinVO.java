package com.biao.vo;

public class LiveCoinVO {

    public long tTime;
    public String tTimeLabel;
    public long value;
    public String valueLabel;

    public long gettTime() {
        return tTime;
    }

    public void settTime(long tTime) {
        this.tTime = tTime;
    }

    public String gettTimeLabel() {
        return tTimeLabel;
    }

    public void settTimeLabel(String tTimeLabel) {
        this.tTimeLabel = tTimeLabel;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getValueLabel() {
        return valueLabel;
    }

    public void setValueLabel(String valueLabel) {
        this.valueLabel = valueLabel;
    }
}
