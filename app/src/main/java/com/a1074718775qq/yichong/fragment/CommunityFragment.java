package com.a1074718775qq.yichong.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.activity.PetShowActivity;
import com.a1074718775qq.yichong.adapter.NewsRvAdapter;
import com.a1074718775qq.yichong.adapter.PetShowRvAdapter;
import com.a1074718775qq.yichong.bean.PetShow;
import com.a1074718775qq.yichong.widget.BannerViewHolder;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommunityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {
    Context mContext=getActivity();
    private List<PetShow> petShows;//对象列表
    View view;
    Button writePetShow;
    MZBannerView mMZBanner;
    //对萌宠秀初始化
    RecyclerView rv;
    PetShow petShow;
    int photo[];
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityFragment.
     */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_community, container, false);
        findView();
        onClick();
        //初始化轮播图
        initBanner();
        //初始化卡片布局
        initCardview();
        return view;
    }

    private void initCardview() {
        //初始化cardview的数据
        initializeData();
        //添加布局管理器
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, false));
        rv.setNestedScrollingEnabled(false);//禁止滑动
        //添加适配器
        PetShowRvAdapter adapter=new PetShowRvAdapter(petShows,mContext);
        rv.setAdapter(adapter);
    }

    private void initializeData() {
        photo= new int[]{R.drawable.photo1, R.drawable.photo2, R.drawable.photo3};
        petShows=new ArrayList<>();
        petShows.add(new PetShow(R.drawable.photo1,"lllllliuxt","今天天气真不错啊，真不错",photo));
        petShows.add(new PetShow(R.drawable.photo1,"lllllliuxt","今天天气真不错啊，真不错",photo));
        petShows.add(new PetShow(R.drawable.photo1,"lllllliuxt","今天天气真不错啊，真不错",photo));
    }

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
    }
    private void findView() {
        mMZBanner = view.findViewById(R.id.community_banner);
        writePetShow=view.findViewById(R.id.write_pet_show);
        rv=view.findViewById(R.id.pet_show_list);
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
