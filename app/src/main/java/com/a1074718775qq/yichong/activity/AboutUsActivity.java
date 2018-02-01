package com.a1074718775qq.yichong.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.a1074718775qq.yichong.R;

import com.a1074718775qq.yichong.R;

public class AboutUsActivity extends AppCompatActivity {
    private Button returnToUser;
    private Button attachToUs;
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

        attachToUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=  new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:156-0201-7551"));
                startActivity(intent);
            }
        });
    }

    private void findView() {
        returnToUser=findViewById(R.id.about_us_return_button);
        attachToUs=findViewById(R.id.about_us_confirm);
    }
}
