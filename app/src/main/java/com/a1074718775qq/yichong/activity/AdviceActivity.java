package com.a1074718775qq.yichong.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.a1074718775qq.yichong.R;

public class AdviceActivity extends AppCompatActivity {
    Button returnToUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        findView();
        onClick();
    }

    private void onClick() {
        returnToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findView() {
        returnToUser=findViewById(R.id.advice_return_button);
    }
}
