package com.biao.pojo;

import com.biao.enums.TradeEnum;
import com.biao.util.TradeCompute;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by p on 2018/4/7.
 * TradeVo扩展的一些字段；
 *
 * @see TradeVo
 */
public class TradeDto extends TradeVo implements Serializable {

    /**
     * 交易类型;
     */
    private TradeEnum type;
    /**
     * 锁定的额度；
     * 需用什么币总去交易什么币总；
     * 需要的币总就是需要锁定的币总；
     */
    private BigDecimal blockVolume;
    /**
     * 主交易的数量;
     * buy:需要花费多少主交易区币总数量；
     * sell:得到多少主交易区的币总数量；
     */
    private BigDecimal tradeVolume;
    /**
     * 参与计算的Volume;
     * buy:得到了多少；
     * sell:已经花掉了多少；
     */
    private BigDecimal computerVolume;

    /**
     * 这个主要用与去查询需要交易锁定的币种信息；
     */
    private String tradeCoin;
    /**
     * 交易区币总
     */
    private String toTradeCoin;
    /**
     * 取消锁;
     */
    private String cancelLock;
    

    public String getTradeCoin() {
        return tradeCoin;
    }

    public TradeDto setTradeCoin(String tradeCoin) {
        this.tradeCoin = tradeCoin;
        return this;
    }

    public TradeEnum getType() {
        return type;
    }

    public TradeDto setType(TradeEnum type) {
        this.type = type;
        return this;
    }

    public BigDecimal getBlockVolume() {
        return blockVolume;
    }

    public TradeDto setBlockVolume(BigDecimal blockVolume) {
        this.blockVolume = blockVolume;
        return this;
    }

    public BigDecimal getTradeVolume() {
        return tradeVolume;
    }

    public TradeDto setTradeVolume(BigDecimal tradeVolume) {
        this.tradeVolume = tradeVolume;
        return this;
    }

    public String getToTradeCoin() {
        return toTradeCoin;
    }

    public TradeDto setToTradeCoin(String toTradeCoin) {
        this.toTradeCoin = toTradeCoin;
        return this;
    }

    public BigDecimal getComputerVolume() {
        return computerVolume;
    }

    public TradeDto setComputerVolume(BigDecimal computerVolume) {
        this.computerVolume = computerVolume;
        return this;
    }

    public String getCancelLock() {
        return cancelLock;
    }

    public TradeDto setCancelLock(String cancelLock) {
        this.cancelLock = cancelLock;
        return this;
    }

    /**
     * 对象转换；
     *
     * @param vo 把vo对象转换成可传输对象；
     * @return 可传输对象；
     */
    public static TradeDto transform(TradeVo vo, TradeEnum tradeEnum) {
        TradeDto dto = new TradeDto();
        dto.setVolume(vo.getVolume());
        dto.setCoinMain(vo.getCoinMain());
        dto.setCoinMain(vo.getCoinMain());
        dto.setPrice(vo.getPrice());
        dto.setCoinOther(vo.getCoinOther());
        dto.setOrderNo(vo.getOrderNo());
        dto.setUserId(vo.getUserId());
        dto.setType(tradeEnum);
        //这里还需要计算blockVolume;
        if (Objects.equals(tradeEnum, TradeEnum.BUY)) {
            BigDecimal bVolume = TradeCompute.multiply(dto.getVolume(), dto.getPrice());
            dto.setTradeCoin(vo.getCoinMain());
            dto.setToTradeCoin(vo.getCoinOther());
            //买入的锁定
            dto.setBlockVolume(bVolume);
            dto.setTradeVolume(bVolume);
            dto.setComputerVolume(vo.getVolume());

        } else {
            dto.setTradeCoin(vo.getCoinOther());
            dto.setToTradeCoin(vo.getCoinMain());
            dto.setTradeVolume(vo.getVolume());
            dto.setBlockVolume(vo.getVolume());
            dto.setComputerVolume(vo.getVolume());
        }
        return dto;
    }

    /**
     * 获取一个消息拼装的key;
     *
     * @return key;
     */
    public String ackKey() {
        return this.getCoinOther() + "_" + this.getCoinMain();
    }
}
