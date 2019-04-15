package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@SqlTable("js_plat_user_auto")
@Data
public class PlatUserSyna implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    private String id;

    //同步状态，0:未同步，1:已同步
    @SqlField("status")
    private Integer status;

    @SqlField("source_parent_id")
    private String sourceParentId;

    @SqlField("source_id")
    private String sourceId;

    @SqlField("mobile")
    private String mobile;

    @SqlField("username")
    private String username;

    @SqlField("source")
    private String source;

    @SqlField("create_date")
    private LocalDateTime createDate;

    @SqlField("update_date")
    private LocalDateTime updateDate;

    @SqlField("syn_date")
    private LocalDateTime synDate;

    @SqlField("pass")
    private String pass;

    @SqlField("is_send_msg")
    private String isSendMsg;

    @SqlField("remark")
    private String remark;

}
