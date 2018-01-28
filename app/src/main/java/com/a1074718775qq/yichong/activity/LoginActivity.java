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

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

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
    }

    private void findView() {
        phoneNumber=(EditText)findViewById(R.id.phoneNumber);
        verification=(EditText)findViewById(R.id.verification) ;
        delete=(Button)findViewById(R.id.phoneNumberDelete);
        getverification=(Button)findViewById(R.id.VerificationButton);
        loginButton= (CircularProgressButton) findViewById(R.id.loginButton);
    }
}
