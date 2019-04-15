package com.biao.entity.relay;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

@SqlTable("mk_relay_auto_config")
public class MkRelayAutoConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("status")
    private String status;
    @SqlField("start_reward_number")
    private Integer startRewardNumber;
    @SqlField("remark")
    private String remark;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStartRewardNumber() {
        return startRewardNumber;
    }

    public void setStartRewardNumber(Integer startRewardNumber) {
        this.startRewardNumber = startRewardNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
