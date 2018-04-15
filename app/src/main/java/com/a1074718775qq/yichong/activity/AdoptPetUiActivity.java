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
import com.a1074718775qq.yichong.adapter.AdoptPetAdapter;
import com.a1074718775qq.yichong.bean.AdoptPet;
import com.a1074718775qq.yichong.utils.HttpUtils;
import com.a1074718775qq.yichong.utils.MyRenRenCallback;
import com.a1074718775qq.yichong.utils.NetworkUtil;
import com.alibaba.fastjson.JSON;
import com.mcxtzhang.layoutmanager.swipecard.CardConfig;
import com.mcxtzhang.layoutmanager.swipecard.OverLayCardLayoutManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdoptPetUiActivity extends AppCompatActivity {
    private Context mContext=AdoptPetUiActivity.this;
    RecyclerView rv;
    TextView write;
    Button returnButton,another;
    AdoptPetAdapter adapter;
    //网络工具
    NetworkUtil network;
    //   收养宠物id
    private  int adopt_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_pet_ui);
        findview();
        onclick();
        adopt_id=0;
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
                map.put("adopt_pet_id", adopt_id);
                final String json = JSON.toJSONString(map, true);
                try {
                    HttpUtils.doPostAsy(getString(R.string.GetAdoptPetInterface), json, new HttpUtils.CallBack() {
                        public void onRequestComplete(final String result) {
                            final List<AdoptPet> show = JSON.parseArray(result.trim(), AdoptPet.class);
                            if (show.size() != 0) {
                                //判断是不是初始化，如果是，则初始化
                                //如果不是则在rv里面继续增加
                                rv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        rv.setBackground(null);
                                    }
                                });
                                if (adopt_id==0)
                                {
                                    rv.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter=new AdoptPetAdapter(mContext,show);
                                            rv.setAdapter(adapter);
                                        }
                                    });
                                }
                                cardUi(show);
                                adopt_id = adopt_id + show.size();
                            }
                            else {
                                rv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        rv.setBackgroundResource(R.drawable.lose);
                                        Toast.makeText(mContext, "没有更多可加载，让我们去收养这些毛孩子吧！", Toast.LENGTH_LONG).show();
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

    public void cardUi(final List<AdoptPet> show) {
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
                Intent intent=new Intent(mContext,AdoptPetActivity.class);
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
        rv=findViewById(R.id.adopt_pet_list);
        write=findViewById(R.id.adopt_pet_write);
        returnButton=findViewById(R.id.adopt_pet_return);
        another=findViewById(R.id.adopt_pet_another);
    }
}
