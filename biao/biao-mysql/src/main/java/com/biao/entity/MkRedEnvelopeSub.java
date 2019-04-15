package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

@SqlTable("mk_red_envelope_sub")
public class MkRedEnvelopeSub extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("envelope_id")
    private String envelopeId;
    @SqlField("receive_user_id")
    private String receiveUserId;
    @SqlField("receive_mobile")
    private String receiveMobile;
    @SqlField("receive_mail")
    private String receiveMail;
    @SqlField("receive_real_name")
    private String receiveRealName;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("type")
    private String type;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("status")
    private String status;
    @SqlField("send_user_id")
    private String sendUserId;
    @SqlField("send_mobile")
    private String sendMobile;
    @SqlField("send_mail")
    private String sendMail;
    @SqlField("send_real_name")
    private String sendRealName;
    @SqlField("remark")
    private String remark;
    @SqlField("version")
    private Long version;

    public String getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(String envelopeId) {
        this.envelopeId = envelopeId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveMobile() {
        return receiveMobile;
    }

    public void setReceiveMobile(String receiveMobile) {
        this.receiveMobile = receiveMobile;
    }

    public String getReceiveMail() {
        return receiveMail;
    }

    public void setReceiveMail(String receiveMail) {
        this.receiveMail = receiveMail;
    }

    public String getReceiveRealName() {
        return receiveRealName;
    }

    public void setReceiveRealName(String receiveRealName) {
        this.receiveRealName = receiveRealName;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendMobile() {
        return sendMobile;
    }

    public void setSendMobile(String sendMobile) {
        this.sendMobile = sendMobile;
    }

    public String getSendMail() {
        return sendMail;
    }

    public void setSendMail(String sendMail) {
        this.sendMail = sendMail;
    }

    public String getSendRealName() {
        return sendRealName;
    }

    public void setSendRealName(String sendRealName) {
        this.sendRealName = sendRealName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
