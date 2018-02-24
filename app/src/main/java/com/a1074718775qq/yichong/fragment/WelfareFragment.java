package com.a1074718775qq.yichong.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.activity.AdoptPetActivity;
import com.a1074718775qq.yichong.activity.FindPetActivity;
import com.a1074718775qq.yichong.adapter.WelfareRvAdapter;
import com.a1074718775qq.yichong.bean.WelfareProject;
import com.a1074718775qq.yichong.utils.HttpUtils;
import com.a1074718775qq.yichong.utils.NetworkUtil;
import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.widget.SearchView.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WelfareFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WelfareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  WelfareFragment extends Fragment implements OnQueryTextListener{
    //上下文
    View feedpet,pethome;//LinearLayout
    Context mContext=getActivity();
    private ArrayList<WelfareProject> welfareFull;//对象列表
    RecyclerView rv;
    SearchView search;
    XRefreshView refreshview;
    View view;
    //网络工具
    NetworkUtil network;
//    福利适配器
WelfareRvAdapter adapter;
//收养所id
    private int welfare_id=0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WelfareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WelfareFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WelfareFragment newInstance(String param1, String param2) {
        WelfareFragment fragment = new WelfareFragment();
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
        view = inflater.inflate(R.layout.fragment_welfare, container, false);
        findView();
        onClick();
        welfareFull=new ArrayList<>();
        //设置是否可以上拉刷新
        refreshview.setPullLoadEnable(true);
        //请求服务器加载新闻
        network=new NetworkUtil();
        //如果有网则请求服务器加载
        if(network.isNetworkAvailable(getActivity()))
        {
            try {
                requestFromsql();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getActivity(),"无法连接网络",Toast.LENGTH_LONG).show();
            refreshview.stopLoadMore();
        }
        return view;
    }
    //请求数据库
    private void requestFromsql() {
        //把当前的news_id发给服务器，返回新闻对象
        //创建一个Map对象
        Map<String, Integer> map = new HashMap<>();
        map.put("welfare_id", welfare_id);
        //转成JSON数据
        final String json = JSON.toJSONString(map,true);
        try {
            HttpUtils.doPostAsy(getString(R.string.WelfareInterface), json, new HttpUtils.CallBack() {
                public void onRequestComplete(final String result) {
                    List<WelfareProject>  welfare = JSON.parseArray(result.trim(), WelfareProject.class);
                    welfareFull.addAll(welfare);
                    if (welfare.size() != 0) {
                        //判断是不是初始化，如果是，则初始化
                        if (welfare_id == 0) {
                            initCardview(welfare);
                            welfare_id = welfare_id + welfare.size();
                        } else {
                            //如果不是则在rv里面继续增加
                            addCardview(welfare);
                            welfare_id = welfare_id + welfare.size();
                        }
                    } else {
                        refreshview.setHideFooterWhenComplete(true);
                    }
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),"网络异常,请检查网络连接",Toast.LENGTH_LONG).show();
        }
    }

    //监听事件
    private void onClick() {
        //为搜索框加监听器
       search.setOnQueryTextListener(this);
//       收养萌宠
       feedpet.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getActivity(),AdoptPetActivity.class);
               startActivity(intent);
           }
       });
//       宝贝回家
        pethome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),FindPetActivity.class);
                startActivity(intent);
            }
        });
        refreshview.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {

            }

            //下拉刷新事件监听，暂时用不到
            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshview.stopRefresh();
                        long lastRefreshTime = refreshview.getLastRefreshTime();
                    }
                }, 2000);
            }

            //上拉加载事件，每次加载五条信息
            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(network.isNetworkAvailable(getActivity()))
                        {
                            try {
                                requestFromsql();
                                //写入缓存
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else
                        {
                            Toast.makeText(getActivity(),"无法连接网络",Toast.LENGTH_LONG).show();
                        }
                        refreshview.stopLoadMore();

                    }
                }, 2000);
            }

            @Override
            public void onRelease(float direction) {

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        });
    }
//初始化cardview
    private void initCardview(final List<WelfareProject>  welfare) {
        final LinearLayoutManager lm=new LinearLayoutManager(mContext, VERTICAL, false);
        //初始化cardview的数据
        //添加布局管理器
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rv.setLayoutManager(lm);
                rv.setNestedScrollingEnabled(false);//禁止滑动
                //添加适配器
                adapter = new WelfareRvAdapter(welfare,getActivity());
                rv.setAdapter(adapter);
            }
        });
    }
    //如果是下拉刷新的话
    private void addCardview(List<WelfareProject> welfare) {
        adapter.addMoreItem(welfare);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void findView() {
        rv=view.findViewById(R.id.welfare_recyclerview);
        search=view.findViewById(R.id.welfare_search);
        feedpet=(LinearLayout)view.findViewById(R.id.welfare_feedpet);
        pethome=(LinearLayout)view.findViewById(R.id.welfare_pethome);
        refreshview=view.findViewById(R.id.refreshview);
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
        mListener = null;
    }
    // 单击搜索按钮时激发该方法
    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getActivity(), "您的选择是:" + query, Toast.LENGTH_SHORT).show();
        return false;
    }
    // 用户输入字符时激发该方法
    @Override
    public boolean onQueryTextChange(String newText) {
     if (!TextUtils.isEmpty(newText))
        {
            Toast.makeText(getActivity(),newText, Toast.LENGTH_SHORT).show();
        }
        else
        {

        }
        return false;
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
