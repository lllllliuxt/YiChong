package com.a1074718775qq.yichong.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.utils.AMUtils;
import com.a1074718775qq.yichong.utils.VerificationCode;
import com.mob.MobSDK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.youth.xframe.XFrame.tag;

public class LoginActivity extends AppCompatActivity {
Context mContext=LoginActivity.this;
private EditText phoneNumber;
private EditText verification;
private Button delete;
private Button getverification;
private static String country="86";
private int countSeconds = 60;//倒计时秒数
    @SuppressLint("HandlerLeak")
    private Handler mCountHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (countSeconds > 0) {
                --countSeconds;
                getverification.setText("(" + countSeconds + ")秒后获取验证码");
                mCountHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                countSeconds = 60;
                getverification.setText("请重新获取验证码");
            }
        }
    };
    //对短信验证过程中的错误进行处理
    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler()
    {
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 0x00:
                    loginButton.revertAnimation();
                    Toast.makeText(mContext, "验证码输入错误", Toast.LENGTH_SHORT).show();
                    break;
                case 0x01:
                    getverification.setText("获取验证码");
                     break;
            }

        }
    };
    //圆形头像框
CircularProgressButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化验证码sdk
        MobSDK.init(this);
        findView();
        onClick();
    }

    private void onClick() {
        //删除按钮事件监听
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber.setText("");
            }
        });
        //对输入的电话号码进行判断
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 11) {
                    AMUtils.onInactive(mContext, phoneNumber);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //登录按钮事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //电话号码
                String mobile = phoneNumber.getText().toString().trim();
                //验证码
                String verifyCode = verification.getText().toString().trim();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    loginButton.startAnimation();
                if (null != verifyCode && verifyCode.length() == 4) {
                    Log.d(tag, verification.getText().toString());
                    // 注册一个事件回调，用于处理提交验证码操作的结果
                    SMSSDK.registerEventHandler(new EventHandler() {
                        public void afterEvent(int event, int result, Object data) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理验证成功的结果
                                Intent intent=new Intent(mContext,MainActivity.class);
                                startActivity(intent);
                            } else{
                                // TODO 处理错误的结果
                                Message msg=myHandler.obtainMessage(0x00);//接收到验证码但是验证失败的情况
                                msg.arg1 = event;
                                msg.arg2 = result;
                                msg.obj = data;
                                myHandler.sendMessage(msg);
                            }
                        }
                    });
                    // 触发操作
                    SMSSDK.submitVerificationCode(country, mobile, verifyCode);
                }
                else {
                    loginButton.revertAnimation();
                    Toast.makeText(mContext, "密码长度不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //获取验证码按钮事件
        getverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countSeconds == 60) {
                    //获取电话号码
                    String mobile = phoneNumber.getText().toString();
                    Log.e("tag", "mobile==" + mobile);
                    //自定义方法
                    getMobiile(mobile);
                } else {
                    Toast.makeText(mContext, "不能重复发送验证码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//获取验证码信息，判断是否有手机号码
    private void getMobiile(String mobile) {
        if ("".equals(mobile)) {
            //如果手机号为空
            Log.e("tag", "mobile=" + mobile);
            new AlertDialog.Builder(mContext).setTitle("提示").setMessage("手机号码不能为空").setCancelable(true).show();
        }
        else if (!AMUtils.isMobile(mobile)) {
            //通过自定义方法判断是不是合法电话号码
            new AlertDialog.Builder(mContext).setTitle("提示").setMessage("请输入正确的手机号码").setCancelable(true).show();
        }
        else {
            //输入了正确的电话号码，自定义方法请求验证码
            Log.e("tag", "输入了正确的手机号");
            requestVerifyCode(mobile);
        }
    }
//通过手机号获取验证码
    private void requestVerifyCode(String mobile) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    startCountBack();
                } else{
                    // TODO 处理错误的结果
                    Message msg=myHandler.obtainMessage(0x01);//发出了验证码的请求，但是没有成功收到验证码
                    msg.arg1 = event;
                    msg.arg2 = result;
                    msg.obj = data;
                    myHandler.sendMessage(msg);

                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, mobile);
    }
    //获取验证码信息,进行计时操作
    private void startCountBack() {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getverification.setText(countSeconds + "");
                mCountHandler.sendEmptyMessage(0);
            }
        });
    }
    private void findView() {
        phoneNumber= findViewById(R.id.phoneNumber);
        verification= findViewById(R.id.verification);
        delete= findViewById(R.id.phoneNumberDelete);
        getverification= findViewById(R.id.VerificationButton);
        loginButton= findViewById(R.id.loginButton);
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    };
}
