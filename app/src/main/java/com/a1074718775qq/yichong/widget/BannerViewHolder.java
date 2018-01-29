package com.a1074718775qq.yichong.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.a1074718775qq.yichong.R;
import com.zhouwei.mzbanner.holder.MZViewHolder;

public class BannerViewHolder implements MZViewHolder<Integer> {
    private  ImageView mImageView;
    @Override
    public View createView(Context context) {
// 返回页面布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
        mImageView = (ImageView) view.findViewById(R.id.banner_image);
        return view;
    }

    @Override
    public void onBind(Context context, int i, Integer integer) {
        // 数据绑定
        mImageView.setImageResource(integer);
    }
}
