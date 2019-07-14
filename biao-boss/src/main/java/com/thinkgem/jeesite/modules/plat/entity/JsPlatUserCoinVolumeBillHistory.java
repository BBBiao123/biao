/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 前台用户Entity
 *
 * @author dazi
 * @version 2018-04-26
 */
public class JsPlatUserCoinVolumeBillHistory extends DataEntity<JsPlatUserCoinVolumeBillHistory> {

    private static final long serialVersionUID = 1L;
    private String userId;        // 用户id
    private String coinSymbol;        //币总信息
    private String priority;        // 优先级 默认5
    private String refKey;        // 关联KEY
    private Integer opSign;        // 操作符号
    private BigDecimal opLockVolume;        // 操作lock_volume的数量
    private Integer retryCount; //已经重试的次数
    private BigDecimal opVolume; //变化的数量
    private String source;        // 来源
    private String mark;        // 备注信息
    private Integer status;        // 执行状态
    private Integer forceLock;        // 是否需要强制拿锁
    private Integer hash;        // 确定哪台服务器拉取到当前的数据

    @NotNull(message="用户id不能为空")
    public String getUserId() {
        return userId;
    }

    public Integer getOpSign() {
        return opSign;
    }

    public void setOpSign(Integer opSign) {
        this.opSign = opSign;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public BigDecimal getOpVolume() {
        return opVolume;
    }

    public void setOpVolume(BigDecimal opVolume) {
        this.opVolume = opVolume;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getForceLock() {
        return forceLock;
    }

    public void setForceLock(Integer forceLock) {
        this.forceLock = forceLock;
    }

    public Integer getHash() {
        return hash;
    }

    public void setHash(Integer hash) {
        this.hash = hash;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRefKey() {
        return refKey;
    }

    public void setRefKey(String refKey) {
        this.refKey = refKey;
    }

    public BigDecimal getOpLockVolume() {
        return opLockVolume;
    }

    public void setOpLockVolume(BigDecimal opLockVolume) {
        this.opLockVolume = opLockVolume;
    }
    @Length(min=1, max=64, message="来源长度必须介于 1 和 64 之间")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }


}