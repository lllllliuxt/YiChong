package com.a1074718775qq.yichong.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.a1074718775qq.yichong.adapter.FindPetAdapter;
import com.a1074718775qq.yichong.bean.FindPet;
import com.mcxtzhang.layoutmanager.swipecard.RenRenCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuruyu on 2018/4/7.
 */

public class MyRenRenCallback extends RenRenCallback {
    public MyRenRenCallback(RecyclerView rv, RecyclerView.Adapter adapter, List datas) {
        super(0, 15, rv, adapter, datas);

    }


    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        //滑动的比例达到多少之后, 视为滑动
        return 0.15f;
    }
}
