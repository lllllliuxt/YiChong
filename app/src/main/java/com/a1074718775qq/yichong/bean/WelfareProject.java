package com.a1074718775qq.yichong.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * create by 刘晓童
 * on 2018/2/5 0005
 */
public class WelfareProject  implements Serializable {
    @JSONField(name="welfare_id")
    int welfare_id;
    @JSONField(name="welfare_title")
    String welfare_title;
    @JSONField(name="welfare_address")
   String welfare_address;
    @JSONField(name="welfare_phone")
    String welfare_phone;
  @JSONField(name="welfare_picture")
    String welfare_picture;
    public  WelfareProject()
    {

    }

    public int getWelfare_id() {
        return welfare_id;
    }

    public String getWelfare_address() {
        return welfare_address;
    }

    public String getWelfare_phone() {
        return welfare_phone;
    }

    public String getWelfare_title() {
        return welfare_title;
    }

    public String getWelfare_picture() {
        return welfare_picture;
    }

    public void setWelfare_address(String welfare_address) {
        this.welfare_address = welfare_address;
    }

    public void setWelfare_id(int welfare_id) {
        this.welfare_id = welfare_id;
    }

    public void setWelfare_phone(String welfare_phone) {
        this.welfare_phone = welfare_phone;
    }

    public void setWelfare_picture(String welfare_picture) {
        this.welfare_picture = welfare_picture;
    }

    public void setWelfare_title(String welfare_title) {
        this.welfare_title = welfare_title;
    }
    public String toString() {
        return "WelfareProject [welfare_id=" + welfare_id + ", welfare_title=" + welfare_title + ",welfare_address"+welfare_address+"welfare_phone"+welfare_phone+"welfare_picture"+welfare_picture+"]";
    }
}
