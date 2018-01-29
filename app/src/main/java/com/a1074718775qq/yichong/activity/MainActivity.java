package com.a1074718775qq.yichong.activity;


import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.fragment.CommunityFragment;
import com.a1074718775qq.yichong.fragment.HomeFragment;
import com.a1074718775qq.yichong.fragment.UserFragment;
import com.a1074718775qq.yichong.fragment.WelfareFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,HomeFragment.OnFragmentInteractionListener,CommunityFragment.OnFragmentInteractionListener,UserFragment.OnFragmentInteractionListener,WelfareFragment.OnFragmentInteractionListener {
    //UI Object
    private TextView main_home;
    private TextView main_welfare;
    private TextView main_community;
    private TextView main_user;
    private FrameLayout ly_content;
    //Fragment Object
    private HomeFragment fragment1;
    private WelfareFragment fragment2;
    private CommunityFragment fragment3;
    private UserFragment fragment4;
    private FragmentManager fManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fManager=getSupportFragmentManager();
        findView();
        onClick();
        main_home.performClick();//模拟第一次点击
    }

    private void onClick() {
        main_user.setOnClickListener(this);
        main_home.setOnClickListener( this);
        main_welfare.setOnClickListener(this);
        main_community.setOnClickListener(this);
    }

    private void findView() {
        main_home=(TextView)findViewById(R.id.main_home);
        main_welfare=(TextView)findViewById(R.id.main_welfare);
        main_community=(TextView)findViewById(R.id.main_community);
        main_user=(TextView)findViewById(R.id.main_user);
        ly_content=(FrameLayout)findViewById(R.id.ly_content);
    }
    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        //如果不为空，就先隐藏起来
        if(fragment1 != null)fragmentTransaction.hide(fragment1);
        if(fragment2 != null)fragmentTransaction.hide(fragment2);
        if(fragment3 != null)fragmentTransaction.hide(fragment3);
        if(fragment4 != null)fragmentTransaction.hide(fragment4);
    }

    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        /**
         * 如果Fragment为空，就新建一个实例
         * 如果不为空，就将它从栈中显示出来
         */
        switch (v.getId()){
            case R.id.main_home:
                setSelected();
               main_home.setSelected(true);
                if(fragment1 == null){
                   fragment1= new HomeFragment();
                    fTransaction.add(R.id.ly_content,fragment1);
                }else{
                    fTransaction.show(fragment1);
                }
                break;
            case R.id.main_welfare:
                setSelected();
                main_welfare.setSelected(true);
                if(fragment2 == null){
                    fragment2= new WelfareFragment();
                    fTransaction.add(R.id.ly_content,fragment2);
                }else{
                    fTransaction.show(fragment2);
                }
                break;
            case R.id.main_community:
                setSelected();
                main_community.setSelected(true);
                if(fragment3 == null){
                    fragment3 = new CommunityFragment();
                    fTransaction.add(R.id.ly_content,fragment3);
                }else{
                    fTransaction.show(fragment3);
                }
                break;
            case R.id.main_user:
                setSelected();
               main_user.setSelected(true);
                if(fragment4 == null){
                    fragment4 = new UserFragment();
                    fTransaction.add(R.id.ly_content,fragment4);
                }else{
                    fTransaction.show(fragment4);
                }
                break;
        }
        fTransaction.commit();
    }
    //重置所有文本的选中状态
    private void setSelected(){
        main_home.setSelected(false);
        main_community.setSelected(false);
        main_welfare.setSelected(false);
        main_user.setSelected(false);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
