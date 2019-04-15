package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

@SqlTable("cms_link")
public class CmsLink {
    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;
    @SqlField("title")
    private String title;
    @SqlField("image")
    private String image;
    @SqlField("href")
    private String href;
    @SqlField("weight")
    private Integer weight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
