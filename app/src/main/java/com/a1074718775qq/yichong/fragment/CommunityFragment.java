package com.a1074718775qq.yichong.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.activity.PetShowActivity;
import com.a1074718775qq.yichong.adapter.PetShowRvAdapter;
import com.a1074718775qq.yichong.bean.PetShow;
import com.a1074718775qq.yichong.utils.HttpUtils;
import com.a1074718775qq.yichong.utils.NetworkUtil;
import com.a1074718775qq.yichong.widget.BannerViewHolder;
import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.youth.xframe.cache.XCache;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class CommunityFragment extends Fragment {
    Context mContext=getActivity();
    //对象列表
    private ArrayList<PetShow> fullShow=new ArrayList<>();
    View view;
    Button writePetShow;
    MZBannerView mMZBanner;
    private XRefreshView refreshview;
    //对萌宠秀初始化
    RecyclerView rv;
    PetShowRvAdapter adapter;
    //网络工具
    NetworkUtil network;
    //萌宠秀id
    private static int show_id=0;
    //      缓存工具
    XCache mcache;
    //加入轮播图的图片，后期会用网络加入
    int RES[]={R.drawable.photo1,R.drawable.photo2,R.drawable.photo3};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CommunityFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_community, container, false);
        findView();
        onClick();
        //设置是否可以上拉刷新
        refreshview.setPullLoadEnable(true);
        //初始化轮播图
        initBanner();
        //      缓存工具  70M
        mcache=XCache.get(this.getActivity(),1000 * 1000 * 70,200);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //        如果第0条petshow已经缓存，则直接取出来
                if (mcache.getAsObject("petshow0")!=null) {
                    ArrayList<PetShow>  shows=new ArrayList<PetShow>();
                    for (int i=0;i<5;i++)
                    {
                        PetShow petShow=(PetShow) mcache.getAsObject("petshow"+i);
                        if (null!=petShow) {
                            shows.add(petShow);
                            show_id++;
                        }
                    }
                    refreshview.stopLoadMore();
                    fullShow.addAll(shows);
                    initCardview(shows);
                }
                else {
                    //如果有网则请求服务器加载
                    if (network.isNetworkAvailable(getActivity())) {
                        try {
                            requestFromsql();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "无法连接网络，请检查网络连接！", Toast.LENGTH_LONG).show();
                        refreshview.stopLoadMore();
                    }
                }
            }
        }).start();
        return view;
    }

//从数据库中加载信息
    private void requestFromsql() {
        //转成JSON数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建一个Map对象
                Map<String, Integer> map = new HashMap<>();
                map.put("pet_show_id", show_id);
                final String json = JSON.toJSONString(map, true);
                try {
                    HttpUtils.doPostAsy(getString(R.string.GetPetShowInterface), json, new HttpUtils.CallBack() {
                        public void onRequestComplete(final String result) {
                            List<PetShow> show = JSON.parseArray(result.trim(), PetShow.class);
                            fullShow.addAll(show);
                            if (show.size() != 0) {
       //                        写入缓存,命名为petshow加id
                                for(int i=show_id,j=0;i<show_id+show.size();i++,j++)
                                {
//                            缓存保存1天
                                    mcache.put("petshow"+i,show.get(j),1*XCache.TIME_DAY);
                                }
                                //判断是不是初始化，如果是，则初始化
                                if (show_id == 0) {
                                    initCardview(show);
                                    show_id = show_id + show.size();
                                } else {
                                    //如果不是则在rv里面继续增加
                                    addCardview(show);
                                    show_id = show_id + show.size();
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
        }).start();
    }

    private void addCardview(final List<PetShow> show) {
        if(show != null) {
            adapter.addMoreItem(show);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
    }
    }

    private void initCardview(final List<PetShow> show) {
        //添加布局管理器
        final LinearLayoutManager lm=new LinearLayoutManager(mContext, VERTICAL, false);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rv.setLayoutManager(lm);
                rv.setNestedScrollingEnabled(false);//禁止滑动
                //添加适配器
                adapter = new PetShowRvAdapter(show,getActivity(),mcache);
                rv.setAdapter(adapter);
            }
        });
    }



//    初始化轮播图
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
        //设置时间
        mMZBanner.setDuration(4000);
    }

    public void onPause() {
        super.onPause();
        mMZBanner.pause();//暂停轮播
    }

    public void onResume() {
        super.onResume();
        mMZBanner.start();//开始轮播
    }


    private void onClick() {
        writePetShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PetShowActivity.class);
                startActivity(intent);
            }
        });

//        刷新监听
        refreshview.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {

            }

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
            //上拉加载事件，每次加载五条萌宠秀
            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mcache.getAsObject("petshow"+show_id)!=null)
                        {
                            ArrayList<PetShow> shows=new ArrayList<PetShow>();
                            for (int i=show_id;i<5+show_id;i++)
                            {
                                PetShow petShow=(PetShow) mcache.getAsObject("petshow"+i);
                                if (null!=petShow) {
                                    shows.add(petShow);
                                    show_id++;
                                }
                            }
                            fullShow.addAll(shows);
                            addCardview(shows);
                        }
                        else
                        {
                            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                                try {
                                    requestFromsql();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getActivity(), "无法连接网络", Toast.LENGTH_LONG).show();
                            }
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
    private void findView() {
        mMZBanner = view.findViewById(R.id.community_banner);
        writePetShow=view.findViewById(R.id.write_pet_show);
        rv=view.findViewById(R.id.pet_show_list);
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
