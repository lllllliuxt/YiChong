package com.a1074718775qq.yichong.bean;

import android.graphics.Bitmap;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

/**
 * create by 刘晓童
 * on 2018/2/1 0001
 */
public class PetShow {
    @JSONField(name="pet_show_id")
    String pet_show_id;
    @JSONField(name="pet_show_photo")
    int pet_show_photo;
    @JSONField(name="pet_show_context")
    String pet_show_context;
    @JSONField(name="pet_show_time")
    String pet_show_time;
    @JSONField(name="user_id")
    String user_id;
    @JSONField(name="user_name")
    String user_name;
    @JSONField(name="user_icon")
    String user_icon;
    @JSONField(name="user_icon_time")
    String user_icon_time;
    Bitmap photo[];
    /**
     *
     * @param pet_show_id 萌宠秀id
     * @param pet_show_photo 萌宠秀图片数量，通过他和萌宠秀时间一起可以获得萌宠秀的图片，放在photo【】中
     * @param pet_show_context 萌宠秀内容
     * @param pet_show_time 萌宠秀图片发表的时间
     * @param user_id 用户id
     * @param user_name 用户昵称
     * @param user_icon 用户头像
     * @param user_icon_time 用户发布头像的时间
     * @param photo 萌宠秀图片数组
     */
    public PetShow(String pet_show_id, int pet_show_photo, String pet_show_context, String pet_show_time, String user_id, String user_name, String user_icon, String user_icon_time, Bitmap[] photo) {
        this.pet_show_id = pet_show_id;
        this.pet_show_photo = pet_show_photo;
        this.pet_show_context = pet_show_context;
        this.pet_show_time = pet_show_time;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_icon = user_icon;
        this.user_icon_time = user_icon_time;
        this.photo = photo;
    }
    public String getPet_show_time() {
        return pet_show_time;
    }

    public void setPet_show_time(String pet_show_time) {
        this.pet_show_time = pet_show_time;
    }


    public String getPet_show_id() {
        return pet_show_id;
    }

    public void setPet_show_id(String pet_show_id) {
        this.pet_show_id = pet_show_id;
    }

    public int getPet_show_photo() {
        return pet_show_photo;
    }

    public void setPet_show_photo(int pet_show_photo) {
        this.pet_show_photo = pet_show_photo;
    }

    public String getPet_show_context() {
        return pet_show_context;
    }

    public void setPet_show_context(String pet_show_context) {
        this.pet_show_context = pet_show_context;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_icon_time() {
        return user_icon_time;
    }

    public void setUser_icon_time(String user_icon_time) {
        this.user_icon_time = user_icon_time;
    }

    public Bitmap[] getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "PetShow{" +
                "pet_show_id='" + pet_show_id + '\'' +
                ", pet_show_photo=" + pet_show_photo +
                ", pet_show_context='" + pet_show_context + '\'' +
                ", pet_show_time='" + pet_show_time + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_icon='" + user_icon + '\'' +
                ", user_icon_time='" + user_icon_time + '\'' +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}
