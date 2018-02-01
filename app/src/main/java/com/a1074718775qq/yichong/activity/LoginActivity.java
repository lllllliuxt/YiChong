package com.a1074718775qq.yichong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.utils.AMUtils;

import java.util.HashMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class LoginActivity extends AppCompatActivity {
Context mContext=LoginActivity.this;
EditText phoneNumber,verification;
Button delete,getverification;
CircularProgressButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    loginButton.startAnimation();
                Intent intent=new Intent(mContext,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //获取验证码按钮事件
        getverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////////老公老公我太笨了，呜呜呜……
            }
        });
    }

    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else{
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }
    private void findView() {
        phoneNumber=(EditText)findViewById(R.id.phoneNumber);
        verification=(EditText)findViewById(R.id.verification) ;
        delete=(Button)findViewById(R.id.phoneNumberDelete);
        getverification=(Button)findViewById(R.id.VerificationButton);
        loginButton= (CircularProgressButton) findViewById(R.id.loginButton);
    }
}
