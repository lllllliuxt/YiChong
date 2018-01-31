package com.yichong.dialog;

/**
 * Created by xuruyu on 2018/1/30.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;

import com.a1074718775qq.yichong.R;

/**
* 对话框实现类
* @author xuruyu
*
*/
 public class MyDialog extends Dialog implements OnClickListener {
     public MyDialog(Context context) {
         super(context,R.style.Theme_AppCompat_Dialog);
         //初始化布局
         setContentView(R.layout.select_photo);
         Window dialogWindow = getWindow();
         dialogWindow.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
         dialogWindow.setGravity(Gravity.BOTTOM);
         setCanceledOnTouchOutside(true);
         findViewById(R.id.btn_camera).setOnClickListener(this);
         findViewById(R.id.btn_gallery).setOnClickListener(this);
         findViewById(R.id.btn_cancel).setOnClickListener(this);
     }
     @Override
     public void onClick(View v) {
         switch (v.getId()) {
             case R.id.btn_camera:
                 onButtonClickListener.camera();
                 break;
             case R.id.btn_gallery:
                 onButtonClickListener.gallery();
                 break;
             case R.id.btn_cancel:
                 onButtonClickListener.cancel();
                 break;
             default:
                 break;
         }
     }
     /**
     * 按钮的监听器
     * @author Orathee
     * @date 2014年3月20日 下午4:28:39
     */
     public interface OnButtonClickListener{
         void camera();
         void gallery();
         void cancel();
     }
     private OnButtonClickListener onButtonClickListener;
     public OnButtonClickListener getOnButtonClickListener() {
         return onButtonClickListener;
     }
     public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
         this.onButtonClickListener = onButtonClickListener;
     }
 }
