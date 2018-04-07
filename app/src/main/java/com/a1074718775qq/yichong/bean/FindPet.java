package com.a1074718775qq.yichong.bean;

import android.graphics.Bitmap;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

/**
 * Created by xuruyu on 2018/4/6.
 */

public class FindPet {
    @JSONField(name="find_pet_id")
    String find_pet_id;
    @JSONField(name="find_pet_photo")
    int find_pet_photo;
    @JSONField(name="find_pet_context")
    String find_pet_context;
    @JSONField(name="find_pet_time")
    String find_pet_time;
    @JSONField(name="user_id")
    String user_id;
    @JSONField(name="user_name")
    String user_name;
    @JSONField(name="user_icon")
    String user_icon;
    @JSONField(name="user_icon_time")
    String user_icon_time;

    Bitmap photo[];

    public FindPet(String find_pet_id, int find_pet_photo, String find_pet_context, String find_pet_time, String user_id, String user_name, String user_icon, String user_icon_time, Bitmap[] photo) {
        this.find_pet_id = find_pet_id;
        this.find_pet_photo = find_pet_photo;
        this.find_pet_context = find_pet_context;
        this.find_pet_time = find_pet_time;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_icon = user_icon;
        this.user_icon_time = user_icon_time;
        this.photo = photo;
    }

    public FindPet() {
    }

    public String getFind_pet_id() {
        return find_pet_id;
    }

    public void setFind_pet_id(String find_pet_id) {
        this.find_pet_id = find_pet_id;
    }

    public int getFind_pet_photo() {
        return find_pet_photo;
    }

    public void setFind_pet_photo(int find_pet_photo) {
        this.find_pet_photo = find_pet_photo;
    }

    public String getFind_pet_context() {
        return find_pet_context;
    }

    public void setFind_pet_context(String find_pet_context) {
        this.find_pet_context = find_pet_context;
    }

    public String getFind_pet_time() {
        return find_pet_time;
    }

    public void setFind_pet_time(String find_pet_time) {
        this.find_pet_time = find_pet_time;
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
        return "FindPet{" +
                "find_pet_id='" + find_pet_id + '\'' +
                ", find_pet_photo=" + find_pet_photo +
                ", find_pet_context='" + find_pet_context + '\'' +
                ", find_pet_time='" + find_pet_time + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_icon='" + user_icon + '\'' +
                ", user_icon_time='" + user_icon_time + '\'' +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}
