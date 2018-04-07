package com.a1074718775qq.yichong.fragment;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.activity.AboutUsActivity;
import com.a1074718775qq.yichong.activity.AdviceActivity;
import com.a1074718775qq.yichong.activity.LoginActivity;
import com.a1074718775qq.yichong.activity.UserInfoActivity;
import com.a1074718775qq.yichong.bean.UserInfo;
import com.a1074718775qq.yichong.datebase.MyDatebaseHelper;
import com.a1074718775qq.yichong.utils.DataCleanManager;
import com.youth.xframe.widget.XToast;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    View advice,aboutUs,clear;//LinearLayout布局监听
    Context mContext=getActivity();
    View view;
    private Button edit_info;
    private Button exit;
//    用户昵称
    private TextView user_nick;
//    用户城市
    private TextView user_city;
//    用户喜爱的宠物
    private TextView user_pet;
//    用户的头像
    private TextView cachesize;
    private CircleImageView user_icon;


    private final int CLEAN_SUC=1001;
    private final int CLEAN_FAIL=1002;
    //sqlite数据库
    private MyDatebaseHelper db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_user, container, false);
       findView();
       onClick();
       initinfo();
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   //查看缓存的大小
                   cachesize.setText(DataCleanManager.getTotalCacheSize(getActivity()));
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }).start();
       return view;
    }


//初始化我的界面的用户信息，从sqllite里面找信息
    private void initinfo() {
        //  获取用户id
        SharedPreferences sp = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String userId=sp.getString("userId",null);
        db = new MyDatebaseHelper(getActivity(), "userInfo.db", 1);
//        查询用户的头像，用户的昵称，用户的城市，用户的宠物
        UserInfo user = db.getUserFragment(db, getActivity(), userId);
        user_nick.setText(user.getUser_name());
        user_city.setText(user.getUser_city());
        user_pet.setText(user.getUser_love_pet());
        user_icon.setImageBitmap(user.getUser_icon());
    }


    private void onClick() {
        edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
            }
        });

        advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AdviceActivity.class);
                startActivity(intent);
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dlg = new AlertDialog.Builder(getActivity())
                        .setTitle("退出登录？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sp = getActivity().getSharedPreferences("userData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.apply();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                //清空源来栈中的Activity，新建栈打开相应的Activity
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .create();
                dlg.show();
            }
        });
//        清除缓存的监听
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除操作
                DataCleanManager.clearAllCache(getActivity());
                String size= (String) cachesize.getText();
//                并更新缓存的大小
                try {
                    //查看缓存的大小
                    cachesize.setText(DataCleanManager.getTotalCacheSize(getActivity()));
                    Toast.makeText(getActivity(),"已经清理了"+size+"缓存！",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void findView() {
        edit_info=view.findViewById(R.id.edit_info);
        advice= view.findViewById(R.id.user_advice);
        aboutUs= view.findViewById(R.id.user_aboutus);
        exit=view.findViewById(R.id.exit);
        clear=view.findViewById(R.id.clearCache);

        user_icon=view.findViewById(R.id.user_icon);
        user_nick=view.findViewById(R.id.user_nick);
        user_city=view.findViewById(R.id.user_city);
        user_pet=view.findViewById(R.id.user_pet);
        cachesize=view.findViewById(R.id.cachesize);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (db != null) {
            db.close();
        }
        mListener = null;
    }
//当更改完信息返回刷新界面
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                    initinfo();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
