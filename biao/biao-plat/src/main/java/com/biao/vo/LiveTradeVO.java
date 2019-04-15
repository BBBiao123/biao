package com.biao.vo;

public class LiveTradeVO {
    public long uTime;
    public String uTimeLabel;
    public long value;

    public String valueLabel;

    public String getValueLabel() {
        return valueLabel;
    }

    public void setValueLabel(String valueLabel) {
        this.valueLabel = valueLabel;
    }

    public long getuTime() {
        return uTime;
    }

    public void setuTime(long uTime) {
        this.uTime = uTime;
    }

    public String getuTimeLabel() {
        return uTimeLabel;
    }

    public void setuTimeLabel(String uTimeLabel) {
        this.uTimeLabel = uTimeLabel;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
