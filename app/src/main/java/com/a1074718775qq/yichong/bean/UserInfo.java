package com.a1074718775qq.yichong.bean;

import android.graphics.Bitmap;

/**
 * create by 刘晓童
 * on 2018/2/2 0002
 */
public class UserInfo {
    private int user_id;
    private Bitmap user_icon;
    private String user_name;
    private String user_phone;
    private String user_city;
    private String user_love_pet;
    private String user_feed_year;
    private String user_log_time;
    private String user_qq;
    private String user_wx;
    private String user_wb;
    private long user_icon_time;
    public UserInfo()
    {

    }
    public UserInfo( int user_id,Bitmap user_icon,String user_name,String user_phone,String user_city,String user_love_pet,String user_feed_year,String user_log_time,String user_qq, String user_wx,String user_wb)
    {
        this.user_id=user_id;
        this.user_icon=user_icon;
        this.user_name=user_name;
        this.user_phone=user_phone;
        this.user_city=user_city;
        this.user_love_pet=user_love_pet;
        this.user_feed_year=user_feed_year;
        this.user_log_time=user_log_time;
        this.user_qq=user_qq;
        this.user_wx=user_wx;
        this.user_wb=user_wb;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_feed_year() {
        return user_feed_year;
    }

    public Bitmap getUser_icon() {
        return user_icon;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_city() {
        return user_city;
    }

    public String getUser_log_time() {
        return user_log_time;
    }

    public String getUser_love_pet() {
        return user_love_pet;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_qq() {
        return user_qq;
    }

    public String getUser_wx() {
        return user_wx;
    }

    public String getUser_wb() {
        return user_wb;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public void setUser_feed_year(String user_feed_year) {
        this.user_feed_year = user_feed_year;
    }

    public void setUser_icon(Bitmap user_icon) {
        this.user_icon = user_icon;
    }

    public void setUser_log_time(String user_log_time) {
        this.user_log_time = user_log_time;
    }

    public void setUser_love_pet(String user_love_pet) {
        this.user_love_pet = user_love_pet;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setUser_qq(String user_qq) {
        this.user_qq = user_qq;
    }

    public void setUser_wb(String user_wb) {
        this.user_wb = user_wb;
    }

    public void setUser_wx(String user_wx) {
        this.user_wx = user_wx;
    }
    public long getUser_icon_time() {
        return user_icon_time;
    }

    public void setUser_icon_time(long user_icon_time) {
        this.user_icon_time = user_icon_time;
    }
    @Override
    public String toString() {
        return "UserInfo[user_id=" + user_id + ", user_icon=" + user_icon +",user_name="+user_name +",user_phone="+user_phone+",user_city="+user_city+",user_love_pet="+user_love_pet+",user_feed_year="+user_feed_year+",user_log_time="+user_log_time+",user_qq="+user_qq+",user_wx="+user_wx+",user_wb="+user_wb+"]";
    }


}
