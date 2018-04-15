package com.a1074718775qq.yichong.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.datebase.MyDatebaseHelper;
import com.a1074718775qq.yichong.utils.NetworkUtil;
import com.a1074718775qq.yichong.widget.PermissionsChecker;

public class StartActivity extends AppCompatActivity {
    Context mContext=StartActivity.this;
    //网络工具
    NetworkUtil network;
    MyDatebaseHelper sql;
    Button skip;
    private static final int REQUEST_CODE = 0; // 请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            //Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,//摄像头权限
            Manifest.permission.ACCESS_COARSE_LOCATION,//网络定位
            Manifest.permission.ACCESS_FINE_LOCATION//GPS定位
    };
    private int countSeconds = 6;//倒计时e秒数
    @SuppressLint("HandlerLeak")
    private Handler mCountHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (countSeconds > 0) {
                --countSeconds;
                skip.setText("跳过(" + countSeconds + ")");
                mCountHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //加载启动页面
        final View view = View.inflate(this, R.layout.activity_start, null);
        setContentView(view);
        mPermissionsChecker = new PermissionsChecker(this);

        /*沉浸式标题栏*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //淡入淡出
        final AlphaAnimation aa = new AlphaAnimation(1.0f,1.0f);
        //设置持续时间
        aa.setDuration(6000);
        view.startAnimation(aa);
        startCountBack();
        skip=findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aa.cancel();
            }
        });

        aa.setAnimationListener(new Animation.AnimationListener()
        {
            //动画页面结束后要干嘛
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}

        });
    }
    //获取验证码信息,进行计时操作
    private void startCountBack() {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCountHandler.sendEmptyMessage(0);
            }
        });
    }

    private void redirectTo(){
        //判断用户是否登录过
       SharedPreferences sp = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
      if (sp.getString("userId",null) == null || (sp.getLong("deadline",0) < System.currentTimeMillis()) )
       //if(false)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
    }
