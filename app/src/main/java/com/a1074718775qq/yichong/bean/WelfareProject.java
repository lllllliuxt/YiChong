package com.a1074718775qq.yichong.bean;

public class WelfareProject {
    int image;
    String title;
    String date;
    public WelfareProject()
    {}
    public WelfareProject(int image,String title,String date)
    {
        this.image=image;
        this.title=title;
        this.date=date;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
