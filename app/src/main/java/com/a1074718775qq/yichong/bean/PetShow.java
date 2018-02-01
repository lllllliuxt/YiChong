package com.a1074718775qq.yichong.bean;

/**
 * create by 刘晓童
 * on 2018/2/1 0001
 */
public class PetShow {
    int image;
    String nick;
    String comment;
    int photo[];
    public PetShow()
    {

    }
    public PetShow(int image,String nick,String comment,int photo[])
    {
        this.image=image;
        this.nick=nick;
        this.comment=comment;
        this.photo=photo;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int[] getPhoto() {
        return photo;
    }

    public void setPhoto(int[] photo) {
        this.photo = photo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
