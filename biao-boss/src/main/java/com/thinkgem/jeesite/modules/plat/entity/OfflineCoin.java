/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * c2c_coinEntity
 *
 * @author dazi
 * @version 2018-04-29
 */
public class OfflineCoin extends DataEntity<OfflineCoin> {

    private static final long serialVersionUID = 1L;
    private String symbol;        // c2c买卖币种标识
    private String coinId;        // 币种id
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private Integer pointPrice;
    private BigDecimal maxVolume;
    private BigDecimal minVolume;
    private Integer pointVolume;
    private Integer disable;
    private BigDecimal buyFee ;
    private BigDecimal sellFee ;
    private Integer feeType ;//手续费类型0:固定 1:比例
    private String buyFeeStep ; //买入费率公式
    private String sellFeeStep ; //10000|20
    private BigDecimal dayIncPrice; //每日价格增量
    private String isChangeAccount; //是否开启转账
    private BigDecimal realDayLimit; //实名转账限额
    private BigDecimal nonRealDayLimit; //非实名转账限额
    private String changeFeeType; //转账手续费类型0:不收 1:固定2：比例
    private String changeFeeStep; //转账手续费公式
    private BigDecimal changeFee; //转账手续费比例卖出
    private BigDecimal changeMinVolume; //转账最低数量
    private Integer isVolume; //0-显示c2c资产
    public Integer getDisable() {
        return disable;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    @DecimalMin(value = "0.00", message = "最大价格必须大于0")
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    @DecimalMin(value = "0.00", message = "最小价格必须大于0")
    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    @Max(value = 8, message = "最大限制不能超过8位")
    @Min(value = 1, message = "最小限制为1位")
    public Integer getPointPrice() {
        return pointPrice;
    }

    public void setPointPrice(Integer pointPrice) {
        this.pointPrice = pointPrice;
    }

    @DecimalMin(value = "0.00", message = "最大数量必须大于0")
    public BigDecimal getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(BigDecimal maxVolume) {
        this.maxVolume = maxVolume;
    }

    @DecimalMin(value = "0.00", message = "最小数量必须大于0")
    public BigDecimal getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(BigDecimal minVolume) {
        this.minVolume = minVolume;
    }

    @Max(value = 8, message = "最大限制不能超过8位")
    @Min(value = 1, message = "最小限制为1位")
    public Integer getPointVolume() {
        return pointVolume;
    }

    public void setPointVolume(Integer pointVolume) {
        this.pointVolume = pointVolume;
    }

    public OfflineCoin() {
        super();
    }

    public OfflineCoin(String id) {
        super(id);
    }

    @Length(min = 1, max = 11, message = "c2c买卖币种标识长度必须介于 1 和 11 之间")
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Length(min = 0, max = 64, message = "币种id长度必须介于 0 和 64 之间")
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

	public BigDecimal getBuyFee() {
		return buyFee;
	}

	public void setBuyFee(BigDecimal buyFee) {
		this.buyFee = buyFee;
	}

	public BigDecimal getSellFee() {
		return sellFee;
	}

	public void setSellFee(BigDecimal sellFee) {
		this.sellFee = sellFee;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public String getBuyFeeStep() {
		return buyFeeStep;
	}

	public void setBuyFeeStep(String buyFeeStep) {
		this.buyFeeStep = buyFeeStep;
	}

	public String getSellFeeStep() {
		return sellFeeStep;
	}

	public void setSellFeeStep(String sellFeeStep) {
		this.sellFeeStep = sellFeeStep;
	}

    public BigDecimal getDayIncPrice() {
        return dayIncPrice;
    }

    public void setDayIncPrice(BigDecimal dayIncPrice) {
        this.dayIncPrice = dayIncPrice;
    }

    public String getIsChangeAccount() {
        return isChangeAccount;
    }

    public void setIsChangeAccount(String isChangeAccount) {
        this.isChangeAccount = isChangeAccount;
    }

    public BigDecimal getRealDayLimit() {
        return realDayLimit;
    }

    public void setRealDayLimit(BigDecimal realDayLimit) {
        this.realDayLimit = realDayLimit;
    }

    public BigDecimal getNonRealDayLimit() {
        return nonRealDayLimit;
    }

    public void setNonRealDayLimit(BigDecimal nonRealDayLimit) {
        this.nonRealDayLimit = nonRealDayLimit;
    }

    public String getChangeFeeType() {
        return changeFeeType;
    }

    public void setChangeFeeType(String changeFeeType) {
        this.changeFeeType = changeFeeType;
    }

    public String getChangeFeeStep() {
        return changeFeeStep;
    }

    public void setChangeFeeStep(String changeFeeStep) {
        this.changeFeeStep = changeFeeStep;
    }

    public BigDecimal getChangeFee() {
        return changeFee;
    }

    public void setChangeFee(BigDecimal changeFee) {
        this.changeFee = changeFee;
    }

    public BigDecimal getChangeMinVolume() {
        return changeMinVolume;
    }

    public void setChangeMinVolume(BigDecimal changeMinVolume) {
        this.changeMinVolume = changeMinVolume;
    }

    public Integer getIsVolume() {
        return isVolume;
    }

    public void setIsVolume(Integer isVolume) {
        this.isVolume = isVolume;
    }
}