package com.a1074718775qq.yichong.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.adapter.RvAdapter;
import com.a1074718775qq.yichong.bean.PetNews;
import com.a1074718775qq.yichong.widget.BannerViewHolder;
import com.andview.refreshview.XRefreshView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    Context mContext=getActivity();
    private List<PetNews> news;//对象列表
    RecyclerView rv;
    View view;
    MZBannerView mMZBanner;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        findView();
        initBanner();
        //初始化cardview
        initCardview();
        return view;
    }

    private void initCardview() {
        //初始化cardview的数据
        initializeData();
        //添加布局管理器
        rv.setLayoutManager(new LinearLayoutManager(mContext, VERTICAL, false));
        rv.setNestedScrollingEnabled(false);//禁止滑动
        //添加适配器
        RvAdapter adapter = new RvAdapter(news,mContext);
        rv.setAdapter(adapter);
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
//初始化新闻列
    private void initializeData(){
        news = new ArrayList<>();
        news.add(new PetNews(R.drawable.photo1,"老太太与狗狗朝夕相伴多年，狗狗忽然跳楼，却不见老太太露面","她叫英，老伴去世的早，英为了供儿子念书，很长一段时间在城市的各大工地上做和水泥的工作。白天英顶着太阳在工地上拼命，....."));
        news.add(new PetNews(R.drawable.photo2,"主人晒出猫咪剪发时的表情，微博网友瞬间笑喷：让这眼神委屈到哭！","最近微博网友爆料：家里养了一只长毛猫，毛主任想着最近冬天到了嘛，由于猫只要一换季节，猫毛就会掉的家里到处都是的，所......"));
        news.add(new PetNews(R.drawable.photo3,"小偷入室偷窃，打伤金毛，事后为何还要主人赔偿？","据网友微博爆料成：事情发生在一个老小区，虽然小区没有门禁的，但是也还算和谐，基本上都没有出现过偷盗事件。由于临近年......"));
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
        mMZBanner = (MZBannerView) view.findViewById(R.id.home_banner);
        rv= (RecyclerView)view.findViewById(R.id.news_list);
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
