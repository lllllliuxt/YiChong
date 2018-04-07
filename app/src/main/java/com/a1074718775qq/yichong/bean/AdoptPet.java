package com.a1074718775qq.yichong.bean;

import android.graphics.Bitmap;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

/**
 * Created by xuruyu on 2018/4/7.
 */

public class AdoptPet {
    @JSONField(name="adopt_pet_id")
    String adopt_pet_id;
    @JSONField(name="adopt_pet_photo")
    int adopt_pet_photo;
    @JSONField(name="adopt_pet_context")
    String adopt_pet_context;
    @JSONField(name="adopt_pet_time")
    String adopt_pet_time;
    @JSONField(name="user_id")
    String user_id;
    @JSONField(name="user_name")
    String user_name;
    @JSONField(name="user_icon")
    String user_icon;
    @JSONField(name="user_icon_time")
    String user_icon_time;

    Bitmap photo[];

    public AdoptPet()
    {

    }
    public AdoptPet(String adopt_pet_id, int adopt_pet_photo, String adopt_pet_context, String adopt_pet_time, String user_id, String user_name, String user_icon, String user_icon_time, Bitmap[] photo) {
        this.adopt_pet_id = adopt_pet_id;
        this.adopt_pet_photo = adopt_pet_photo;
        this.adopt_pet_context = adopt_pet_context;
        this.adopt_pet_time = adopt_pet_time;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_icon = user_icon;
        this.user_icon_time = user_icon_time;
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "AdoptPet{" +
                "adopt_pet_id='" + adopt_pet_id + '\'' +
                ", adopt_pet_photo=" + adopt_pet_photo +
                ", adopt_pet_context='" + adopt_pet_context + '\'' +
                ", adopt_pet_time='" + adopt_pet_time + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_icon='" + user_icon + '\'' +
                ", user_icon_time='" + user_icon_time + '\'' +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }

    public String getAdopt_pet_id() {
        return adopt_pet_id;
    }

    public void setAdopt_pet_id(String adopt_pet_id) {
        this.adopt_pet_id = adopt_pet_id;
    }

    public int getAdopt_pet_photo() {
        return adopt_pet_photo;
    }

    public void setAdopt_pet_photo(int adopt_pet_photo) {
        this.adopt_pet_photo = adopt_pet_photo;
    }

    public String getAdopt_pet_context() {
        return adopt_pet_context;
    }

    public void setAdopt_pet_context(String adopt_pet_context) {
        this.adopt_pet_context = adopt_pet_context;
    }

    public String getAdopt_pet_time() {
        return adopt_pet_time;
    }

    public void setAdopt_pet_time(String adopt_pet_time) {
        this.adopt_pet_time = adopt_pet_time;
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
}
