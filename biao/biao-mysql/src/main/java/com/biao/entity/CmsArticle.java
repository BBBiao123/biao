package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.time.LocalDateTime;

@SqlTable("cms_article")
public class CmsArticle extends BaseEntity {

    @SqlField("category_id")
    private String categoryId;
    @SqlField("title")
    private String title;
    @SqlField("link")
    private String link;
    @SqlField("image")
    private String image;
    @SqlField("keywords")
    private String keywords;
    @SqlField("description")
    private String description;
    @SqlField("weight")
    private Integer weight;
    @SqlField("weight_date")
    private LocalDateTime weightDate;
    @SqlField("hits")
    private Long hits;
    @SqlField("posid")
    private String posid;
    @SqlField("remarks")
    private String remarks;
    @SqlField("del_flag")
    private Integer delFlag;
    @SqlField("language")
    private String language;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public LocalDateTime getWeightDate() {
        return weightDate;
    }

    public void setWeightDate(LocalDateTime weightDate) {
        this.weightDate = weightDate;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public String getPosid() {
        return posid;
    }

    public void setPosid(String posid) {
        this.posid = posid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
