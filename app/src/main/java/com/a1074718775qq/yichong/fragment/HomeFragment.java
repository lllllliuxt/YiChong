
package com.a1074718775qq.yichong.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;


import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.activity.NewsWebActivity;
import com.a1074718775qq.yichong.adapter.NewsRvAdapter;
import com.a1074718775qq.yichong.bean.PetNews;
import com.a1074718775qq.yichong.utils.HttpUtils;
import com.a1074718775qq.yichong.utils.NetworkUtil;
import com.a1074718775qq.yichong.utils.RecyclerItemClickListener;
import com.a1074718775qq.yichong.widget.BannerViewHolder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static android.support.v7.widget.LinearLayoutManager.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{
    Context mContext=getActivity();
    //对象列表
    private ArrayList<PetNews> fullnews=new ArrayList<>();
    RecyclerView rv;
    View view;
    MZBannerView mMZBanner;
    private XRefreshView refreshview;
    //加入轮播图的图片，后期会用网络加入
    int RES[]={R.drawable.photo1,R.drawable.photo2,R.drawable.photo3};
    //新闻适配器
    NewsRvAdapter adapter;
    //网络工具
    NetworkUtil network;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //新闻id
    private static int news_id=0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
//在下面对fragment进行布局
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        findView();
        onClick();
        //设置是否可以上拉刷新
        refreshview.setPullLoadEnable(true);
        initBanner();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onClick() {
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
                }, 3000);
            }

            //上拉加载事件，每次加载五条新闻
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
        //每条新闻的点击事件
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                    Intent intent=new Intent(getActivity(), NewsWebActivity.class);
                    intent.putExtra("url",fullnews.get(position).getNews_url());
                    startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int posotion) {

            }
        }));

    }
   //从数据库中请求新闻
    private void requestFromsql() {
        //把当前的news_id发给服务器，返回新闻对象
        //创建一个Map对象
        Map<String, Integer> map = new HashMap<>();
        map.put("news_id", news_id);
        //转成JSON数据
        final String json = JSON.toJSONString(map,true);
        try {
            HttpUtils.doPostAsy(getString(R.string.NewsInterface), json, new HttpUtils.CallBack() {
                public void onRequestComplete(final String result) {
                    List<PetNews> news = JSON.parseArray(result.trim(), PetNews.class);
                    fullnews.addAll(news);
                    if (news.size() != 0) {
                        //判断是不是初始化，如果是，则初始化
                        if (news_id == 0) {
                            initCardview(news);
                            news_id = news_id + news.size();
                        } else {
                            //如果不是则在rv里面继续增加
                            addCardview(news);
                            news_id = news_id + news.size();
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

//初始化cardview
    private void initCardview(final List<PetNews> news) {
        //添加布局管理器
        final LinearLayoutManager lm=new LinearLayoutManager(mContext, VERTICAL, false);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rv.setLayoutManager(lm);
                rv.setNestedScrollingEnabled(false);//禁止滑动
                //添加适配器
                adapter = new NewsRvAdapter(news,mContext);
                rv.setAdapter(adapter);
            }
        });
    }

    //往cardview里加值
    private void addCardview(List<PetNews> news)
    {
        if(news != null) {
            adapter.addMoreItem(news);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    //初始化轮播图
    private void initBanner() {
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<RES.length;i++){
            list.add(RES[i]);
        }
        // 设置数据
        mMZBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
    }

    public void onPause() {
        super.onPause();
        mMZBanner.pause();//暂停轮播
    }

    public void onResume() {
        super.onResume();
        mMZBanner.start();//开始轮播
    }

    private void findView() {
        mMZBanner = view.findViewById(R.id.home_banner);
        rv= view.findViewById(R.id.news_list);
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
