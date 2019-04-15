package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 普通用户冻结资产表
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_common_member")
public class Mk2PopularizeCommonMember extends Mk2PopularizeMember {

    private static final long serialVersionUID = 1L;

    @SqlField("type")
    private String type;

    @SqlField("parent_id")
    private String parentId;

    @SqlField("id_card")
    private String idCard;

    @SqlField("real_name")
    private String realName;

    @SqlField("lock_status")
    private String lockStatus;

    @SqlField("lock_volume")
    private BigDecimal lockVolume;

    @SqlField("release_volume")
    private BigDecimal releaseVolume;

    @SqlField("release_begin_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime releaseBeginDate;

    @SqlField("release_cycle")
    private String releaseCycle;

    @SqlField("release_cycle_ratio")
    private BigDecimal releaseCycleRatio;

    @SqlField("release_over")
    private String releaseOver;

    @SqlField("relation_id")
    private String relationId;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;

    @SqlField("remark")
    private String remark;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String getIdCard() {
        return idCard;
    }

    @Override
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public BigDecimal getLockVolume() {
        return lockVolume;
    }

    public void setLockVolume(BigDecimal lockVolume) {
        this.lockVolume = lockVolume;
    }

    public BigDecimal getReleaseVolume() {
        return releaseVolume;
    }

    public void setReleaseVolume(BigDecimal releaseVolume) {
        this.releaseVolume = releaseVolume;
    }

    public LocalDateTime getReleaseBeginDate() {
        return releaseBeginDate;
    }

    public void setReleaseBeginDate(LocalDateTime releaseBeginDate) {
        this.releaseBeginDate = releaseBeginDate;
    }

    public String getReleaseCycle() {
        return releaseCycle;
    }

    public void setReleaseCycle(String releaseCycle) {
        this.releaseCycle = releaseCycle;
    }

    public BigDecimal getReleaseCycleRatio() {
        return releaseCycleRatio;
    }

    public void setReleaseCycleRatio(BigDecimal releaseCycleRatio) {
        this.releaseCycleRatio = releaseCycleRatio;
    }

    public String getReleaseOver() {
        return releaseOver;
    }

    public void setReleaseOver(String releaseOver) {
        this.releaseOver = releaseOver;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Mk2PopularizeCommonMember{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", parentId='" + parentId + '\'' +
                ", userId='" + userId + '\'' +
                ", mail='" + mail + '\'' +
                ", mobile='" + mobile + '\'' +
                ", idCard='" + idCard + '\'' +
                ", realName='" + realName + '\'' +
                ", coinId='" + coinId + '\'' +
                ", coinSymbol='" + coinSymbol + '\'' +
                ", lockStatus='" + lockStatus + '\'' +
                ", lockVolume=" + lockVolume +
                ", releaseVolume=" + releaseVolume +
                ", releaseBeginDate=" + releaseBeginDate +
                ", releaseCycle='" + releaseCycle + '\'' +
                ", releaseCycleRatio=" + releaseCycleRatio +
                ", releaseOver='" + releaseOver + '\'' +
                ", relationId='" + relationId + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", remark='" + remark + '\'' +
                '}';
    }
}
