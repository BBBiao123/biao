package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

@SqlTable("mk_common_user_relation")
public class UserRelation extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("username")
    private String username;
    @SqlField("parent_id")
    private String parentId;
    @SqlField("top_parent_id")
    private String topParentId;
    @SqlField("tree_id")
    private String treeId;
    @SqlField("deth")
    private Integer deth;
    @SqlField("level")
    private Integer level;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTopParentId() {
        return topParentId;
    }

    public void setTopParentId(String topParentId) {
        this.topParentId = topParentId;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public Integer getDeth() {
        return deth;
    }

    public void setDeth(Integer deth) {
        this.deth = deth;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
