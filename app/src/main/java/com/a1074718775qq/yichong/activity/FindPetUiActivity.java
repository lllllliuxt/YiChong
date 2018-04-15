package com.a1074718775qq.yichong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.adapter.FindPetAdapter;
import com.a1074718775qq.yichong.bean.FindPet;
import com.a1074718775qq.yichong.utils.HttpUtils;
import com.a1074718775qq.yichong.utils.MyRenRenCallback;
import com.a1074718775qq.yichong.utils.NetworkUtil;
import com.alibaba.fastjson.JSON;
import com.mcxtzhang.layoutmanager.swipecard.CardConfig;
import com.mcxtzhang.layoutmanager.swipecard.OverLayCardLayoutManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindPetUiActivity extends AppCompatActivity {
    private Context mContext=FindPetUiActivity.this;
    RecyclerView rv;
    TextView write;
    Button returnButton,another;
    FindPetAdapter adapter;
    //网络工具
    NetworkUtil network;
    //   宝贝回家id
    private  int find_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pet_ui);
        findview();
        onclick();
        find_id=0;
                //如果有网则请求服务器加载
                if (network.isNetworkAvailable(mContext)) {
                    try {
                        requestFromsql();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "无法连接网络，请检查网络连接！", Toast.LENGTH_LONG).show();
                }
    }
    protected void requestFromsql() {
        //转成JSON数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个Map对象
                Map<String, Integer> map = new HashMap<>();
                map.put("find_pet_id", find_id);
                final String json = JSON.toJSONString(map, true);
                try {
                    HttpUtils.doPostAsy(getString(R.string.GetFindPetInterface), json, new HttpUtils.CallBack() {
                        public void onRequestComplete(final String result) {

                            final List<FindPet> show = JSON.parseArray(result.trim(), FindPet.class);
                            if (show.size() != 0) {
                                    //判断是不是初始化，如果是，则初始化
                                    //如果不是则在rv里面继续增加
                                rv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        rv.setBackground(null);
                                    }
                                });
                                if (find_id==0)
                                {
                                    rv.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter=new FindPetAdapter(mContext,show);
                                            rv.setAdapter(adapter);
                                        }
                                    });
                                }
                                    cardUi(show);
                                    find_id = find_id + show.size();
                            }
                            else {
                                rv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        rv.setBackgroundResource(R.drawable.lose);
                                        Toast.makeText(mContext, "没有更多可加载，让我们去帮助他们寻找吧！", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                    });
                }
                catch (Exception e)
                {
                    Toast.makeText(mContext,"网络异常,请检查网络连接",Toast.LENGTH_LONG).show();
                }
            }
        }).start();
    }

    public void cardUi(final List<FindPet> show) {
        rv.post(new Runnable() {
            @Override
            public void run() {
                rv.setLayoutManager(new OverLayCardLayoutManager());
                CardConfig.initConfig(mContext);
                ItemTouchHelper.Callback callback = new MyRenRenCallback(rv,adapter, show);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                itemTouchHelper.attachToRecyclerView(rv);
                CardConfig.MAX_SHOW_COUNT = 5;
                CardConfig.SCALE_GAP= 0.07F;
                CardConfig.TRANS_Y_GAP=(int) TypedValue.applyDimension(1, 20.0F, mContext.getResources().getDisplayMetrics());
            }
        });
    }

    public void onclick() {
    write.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(mContext,FindPetActivity.class);
            startActivity(intent);
        }
    });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestFromsql();
            }
        });
    }

    public void findview() {
        rv=findViewById(R.id.find_pet_list);
        write=findViewById(R.id.find_pet_write);
        returnButton=findViewById(R.id.find_pet_return);
        another=findViewById(R.id.find_pet_another);
    }
}
