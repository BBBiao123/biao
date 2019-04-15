/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 币币订单Entity
 *
 * @author dazi
 * @version 2018-04-27
 */
public class ExOrder extends DataEntity<ExOrder> {

    private static final long serialVersionUID = 1L;
    private String userId;        // 用户id
    private String askVolume;        // 挂单数量
    private String successVolume;        // 消费数量
    private String coinSymbol;        // 挂单币种标识
    private String exFee;        // 手续费
    private String status;        // 成交状态0：未成交 1：部分成交 2：全部成交 3：部分取消 4：全部取消
    private String toCoinSymbol;        // 换取的币种符号
    private String toCoinVolume;        // 交易得到的币的数量
    private String exType;        // 挂单类型 0：买入 1：卖出
    private String coinId;        // coin_id
    private String askCoinId;        // ask_coin_id
    private String flag;        // 记录标识
    private Date beginCreateDate;	// 创建日期
    private Date endCreateDate;	// 创建日期

    private BigDecimal price;

    public BigDecimal getPrice() {
        return  this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public ExOrder() {
        super();
    }

    public ExOrder(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "用户id长度必须介于 1 和 64 之间")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAskVolume() {
        return askVolume;
    }

    public void setAskVolume(String askVolume) {
        this.askVolume = askVolume;
    }

    public String getSuccessVolume() {
        return successVolume;
    }

    public void setSuccessVolume(String successVolume) {
        this.successVolume = successVolume;
    }

    @Length(min = 1, max = 11, message = "挂单币种标识长度必须介于 1 和 11 之间")
    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getExFee() {
        return exFee;
    }

    public void setExFee(String exFee) {
        this.exFee = exFee;
    }

    @Length(min = 0, max = 1, message = "成交状态0：未成交 1：部分成交 2：全部成交 3：部分取消 4：全部取消长度必须介于 0 和 1 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Length(min = 0, max = 12, message = "换取的币种符号长度必须介于 0 和 12 之间")
    public String getToCoinSymbol() {
        return toCoinSymbol;
    }

    public void setToCoinSymbol(String toCoinSymbol) {
        this.toCoinSymbol = toCoinSymbol;
    }

    public String getToCoinVolume() {
        return toCoinVolume;
    }

    public void setToCoinVolume(String toCoinVolume) {
        this.toCoinVolume = toCoinVolume;
    }

    @Length(min = 0, max = 1, message = "挂单类型 0：买入 1：卖出长度必须介于 0 和 1 之间")
    public String getExType() {
        return exType;
    }

    public void setExType(String exType) {
        this.exType = exType;
    }

    @Length(min = 0, max = 64, message = "coin_id长度必须介于 0 和 64 之间")
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    @Length(min = 0, max = 64, message = "ask_coin_id长度必须介于 0 和 64 之间")
    public String getAskCoinId() {
        return askCoinId;
    }

    public void setAskCoinId(String askCoinId) {
        this.askCoinId = askCoinId;
    }

    @Length(min = 0, max = 1, message = "记录标识长度必须介于 0 和 1 之间")
    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Date getBeginCreateDate() {
        return beginCreateDate;
    }

    public void setBeginCreateDate(Date beginCreateDate) {
        this.beginCreateDate = beginCreateDate;
    }

    public Date getEndCreateDate() {
        return endCreateDate;
    }

    public void setEndCreateDate(Date endCreateDate) {
        this.endCreateDate = endCreateDate;
    }
}