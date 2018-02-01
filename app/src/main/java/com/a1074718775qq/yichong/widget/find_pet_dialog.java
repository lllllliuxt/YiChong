package com.a1074718775qq.yichong.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.a1074718775qq.yichong.R;

public class find_pet_dialog extends Dialog implements View.OnClickListener {
    Context mContext;

    public find_pet_dialog(@NonNull Context context) {
        super(context);
        //初始化布局
        setContentView(R.layout.find_pet_picture);
        Window dialogWindow = getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);

        findViewById(R.id.btn_gallery1).setOnClickListener(this);
        findViewById(R.id.btn_cancel1).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_gallery1:
                onButtonClickListener.gallery();
                break;
            case R.id.btn_cancel1:
                onButtonClickListener.cancel();
                break;

            default:
                break;
        }
    }
    /**
     52      * 按钮的监听器
     53      * @author xuruyu
     54      * @date 2018/1/31
     55      */
    public interface OnButtonClickListener{
        void gallery();
        void cancel();
    }
    private find_pet_dialog.OnButtonClickListener onButtonClickListener;

    public find_pet_dialog.OnButtonClickListener getOnButtonClickListener() {
        return onButtonClickListener;
    }

    public void setOnButtonClickListener(find_pet_dialog.OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}

