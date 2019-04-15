/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 币币交易对Entity
 *
 * @author dazi
 * @version 2018-04-26
 */
public class ExPair extends DataEntity<ExPair> {

    private static final long serialVersionUID = 1L;
    private String otherCoinId;        // 被交易币种id
    private String pairOther;        // 被交易的币种符号
    private String coinId;        // 交易区币种id
    private String pairOne;        // 交易区符号
    private String status;        // 是否锁定 0：未发布 1：发布
    private String lastPrice;        // 最后一次成交价格
    private String change;        // 24小时涨跌幅
    private String high;        // 24小时内最高价格
    private String low;        // 24小时内 最低价格
    private String volume;        // 24小时的成交量
    private String type;        // 是否主交易区  0 ：主交易 1：创新
    private String fee;
    private Integer sort;
    private String maxVolume;
    private String minVolume;
    private Integer pricePrecision;
    private Integer volumePrecision;
    private Integer volumePercent;

    private String pairSymbol;

    public Integer getVolumePercent() {
        return volumePercent;
    }

    public void setVolumePercent(Integer volumePercent) {
        this.volumePercent = volumePercent;
    }

    public Integer getPricePrecision() {
        return pricePrecision;
    }

    public void setPricePrecision(Integer pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    public Integer getVolumePrecision() {
        return volumePrecision;
    }

    public void setVolumePrecision(Integer volumePrecision) {
        this.volumePrecision = volumePrecision;
    }


    public String getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(String maxVolume) {
        this.maxVolume = maxVolume;
    }

    public String getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(String minVolume) {
        this.minVolume = minVolume;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public ExPair() {
        super();
    }

    public ExPair(String id) {
        super(id);
    }

    @Length(min = 0, max = 64, message = "被交易币种id长度必须介于 0 和 64 之间")
    public String getOtherCoinId() {
        return otherCoinId;
    }

    public void setOtherCoinId(String otherCoinId) {
        this.otherCoinId = otherCoinId;
    }

    @Length(min = 1, max = 24, message = "被交易的币种符号长度必须介于 1 和 24 之间")
    public String getPairOther() {
        return pairOther;
    }

    public void setPairOther(String pairOther) {
        this.pairOther = pairOther;
    }

    @Length(min = 0, max = 64, message = "交易区币种id长度必须介于 0 和 64 之间")
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    @Length(min = 1, max = 64, message = "交易区符号长度必须介于 1 和 64 之间")
    public String getPairOne() {
        return pairOne;
    }

    public void setPairOne(String pairOne) {
        this.pairOne = pairOne;
    }

    @Length(min = 0, max = 1, message = "是否锁定 0：未发布 1：发布长度必须介于 0 和 1 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    @Length(min = 0, max = 45, message = "24小时涨跌幅长度必须介于 0 和 45 之间")
    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Length(min = 0, max = 1, message = "是否主交易区  0 ：主交易 1：创新长度必须介于 0 和 1 之间")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPairSymbol() {
        return this.pairOther.concat("/").concat(this.pairOne);
    }

    public void setPairSymbol(String pairSymbol) {
        this.pairSymbol = pairSymbol;
    }
}