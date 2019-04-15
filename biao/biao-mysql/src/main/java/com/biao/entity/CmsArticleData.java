package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

@SqlTable("cms_article_data")
public class CmsArticleData {

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;
    @SqlField("content")
    private String content;
    @SqlField("copyfrom")
    private String copyfrom;
    @SqlField("relation")
    private String relation;
    @SqlField("allow_comment")
    private Integer allowComment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCopyfrom() {
        return copyfrom;
    }

    public void setCopyfrom(String copyfrom) {
        this.copyfrom = copyfrom;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Integer getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Integer allowComment) {
        this.allowComment = allowComment;
    }
}
