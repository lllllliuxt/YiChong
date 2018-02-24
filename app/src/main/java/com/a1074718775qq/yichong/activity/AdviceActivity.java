package com.a1074718775qq.yichong.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdviceActivity extends AppCompatActivity {
    Context mContext=AdviceActivity.this;
    Button returnToUser;
    private EditText editText;
    private Button upload;
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
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")) {
                    final ProgressDialog progress = new ProgressDialog(mContext);
                    progress.setMessage("正在提交...");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                    //                获取用户id
                    SharedPreferences sp = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
                    String userId = sp.getString("userId", null);
                    int user_id = Integer.parseInt(userId);
                    //                  获取寻宠发布的内容
                    String advice = editText.getText().toString();
                    //                获取当前时间
                    long currentTime = System.currentTimeMillis();
                    //创建一个Map对象
                    Map<String, Object> map = new HashMap<>();
                    map.put("advice_context", advice);
                    map.put("advice_date", currentTime);
                    map.put("user_id", user_id);
                    //转成JSON数据
                    final String json = JSON.toJSONString(map, true);
                    try {
                        HttpUtils.doPostAsy(getString(R.string.adviceInterface), json, new HttpUtils.CallBack() {
                            public void onRequestComplete(final String result) {
                                Log.e("返回结果", result);
                                JSONObject jsonObject = JSON.parseObject(result.trim());
                                final String psresult = jsonObject.getString("result");
                                if (psresult.equals("上传成功")) {
                                    if (progress.isShowing())
                                        progress.dismiss();
                                    //解决在子线程中调用Toast的异常情况处理
                                    Looper.prepare();
                                    Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Looper.loop();
                                } else {
                                    if (progress.isShowing())
                                        progress.dismiss();
                                    Looper.prepare();
                                    Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        });
                    } catch (Exception e) {
                        if (progress.isShowing())
                            progress.dismiss();
                        Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(mContext,"请输入内容",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void findView() {
        returnToUser=findViewById(R.id.advice_return_button);
        editText=findViewById(R.id.advice_text);
        upload=findViewById(R.id.advice_confirm);
    }
}
