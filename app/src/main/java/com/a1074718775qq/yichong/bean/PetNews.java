package com.a1074718775qq.yichong.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class PetNews {
    @JSONField(name="news_id")
    int news_id;
    @JSONField(name="news_picture")
    String news_picture;
    @JSONField(name="news_title")
    String news_title;
    @JSONField(name="news_introduce")
    String news_introduce;
    @JSONField(name="news_url")
    String news_url;
    public PetNews()
    {

    }

    public int getNews_id() {
        return news_id;
    }
    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }
    public String getNews_picture() {
        return news_picture;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getNews_introduce() {
        return news_introduce;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_picture(String news_picture) {
        this.news_picture = news_picture;
    }

    public void setNews_introduce(String news_introduce) {
        this.news_introduce = news_introduce;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }
    public String toString() {
        return "PetNews [news_id=" + news_id + ", news_picture=" + news_picture + ",news_title"+news_title+"news_introduce"+news_introduce+"news_url"+news_url+"]";
    }
}
