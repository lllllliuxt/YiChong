package com.a1074718775qq.yichong.datebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * create by 刘晓童
 * on 2018/2/12 0012
 */
//本类提供将图片转化为字节数组存入数据库和将数据库中字节转化为图片的方法
public class BitmapBytes {
    public BitmapBytes()
    {
    }
    //将图片插入数据库,传入bitmap值
    public byte[] bitmapIntobytes(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    //将图片从数据库中取出
    public Bitmap bytesIntobitmap(byte[] in)
    {
        if (in.length != 0) {
            return BitmapFactory.decodeByteArray(in, 0, in.length);
        } else {
            return null;
        }
    }
}
