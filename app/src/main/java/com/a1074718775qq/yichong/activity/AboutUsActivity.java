package com.a1074718775qq.yichong.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.a1074718775qq.yichong.R;

import com.a1074718775qq.yichong.R;

public class AboutUsActivity extends AppCompatActivity {
    private Button returnToUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
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
        returnToUser=findViewById(R.id.about_us_return_button);
    }
}
