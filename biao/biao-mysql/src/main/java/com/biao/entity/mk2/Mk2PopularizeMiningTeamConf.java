package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeDeserializer;
import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 团队挖矿争霸配置
 *
 *  ""ongfeng
 * @version 2018-11-12
 */
@SqlTable("mk2_popularize_mining_team_conf")
public class Mk2PopularizeMiningTeamConf implements Serializable {

    private static final long serialVersionUID = 1L;
    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    private String show;

    @SqlField("sort_begin_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime sortBeginDate;

    @SqlField("sort_end_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime sortEndDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public LocalDateTime getSortBeginDate() {
        return sortBeginDate;
    }

    public void setSortBeginDate(LocalDateTime sortBeginDate) {
        this.sortBeginDate = sortBeginDate;
    }

    public LocalDateTime getSortEndDate() {
        return sortEndDate;
    }

    public void setSortEndDate(LocalDateTime sortEndDate) {
        this.sortEndDate = sortEndDate;
    }
}
