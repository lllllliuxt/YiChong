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
        //Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.text);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] result = baos.toByteArray();
        return  result;
    }
    //将图片从数据库中取出
    public Bitmap bytesIntobitmap(byte[] in)
    {
        Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
        return bmpout;
    }
}
