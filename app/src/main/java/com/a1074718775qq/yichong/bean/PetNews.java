package com.a1074718775qq.yichong.bean;

public class PetNews {
    int image;
    String title;
    String introduction;
    public PetNews()
    {

    }
    public PetNews(int image,String title,String introduction )
    {
        this.image=image;
        this.title=title;
        this.introduction=introduction;
    }

    public int getImage() {
        return image;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
