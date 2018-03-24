package com.a1074718775qq.yichong.activity;



import android.annotation.SuppressLint;
import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    //Fragment Object
    private HomeFragment fragment1;
    private WelfareFragment fragment2;
    private CommunityFragment fragment3;
    private UserFragment fragment4;
    private FragmentManager fManager;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //防止程序长时间后台导致fragment重叠。
        if (savedInstanceState != null) {
            fManager = getSupportFragmentManager();//重新创建Manager，防止此对象为空
            fManager.popBackStackImmediate(null, 1);//弹出所有fragment
        }
        findView();
        onClick();
        main_home.performClick();//模拟第一次点击
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    private void onClick() {
        main_user.setOnClickListener(this);
        main_home.setOnClickListener( this);
        main_welfare.setOnClickListener(this);
        main_community.setOnClickListener(this);
    }

    private void findView() {
        main_home= findViewById(R.id.main_home);
        main_welfare= findViewById(R.id.main_welfare);
        main_community= findViewById(R.id.main_community);
        main_user= findViewById(R.id.main_user);
        FrameLayout ly_content = findViewById(R.id.ly_content);
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
        /*
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
    public void onAttachFragment(Fragment fragment) {
        if (fragment1 == null && fragment instanceof HomeFragment)
            fragment1 = (HomeFragment) fragment;
        if (fragment2 == null && fragment instanceof WelfareFragment)
            fragment2 = (WelfareFragment) fragment;
        if (fragment3 == null && fragment instanceof CommunityFragment)
            fragment3 = (CommunityFragment) fragment;
        if (fragment4 == null && fragment instanceof UserFragment)
            fragment4 = (UserFragment) fragment;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
