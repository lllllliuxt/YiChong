package com.a1074718775qq.yichong.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.activity.AdoptPetActivity;
import com.a1074718775qq.yichong.activity.FindPetActivity;
import com.a1074718775qq.yichong.adapter.WelfareRvAdapter;
import com.a1074718775qq.yichong.bean.WelfareProject;

import java.util.ArrayList;
import java.util.List;

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
public class WelfareFragment extends Fragment implements OnQueryTextListener {
    //上下文
    Context mContext=getActivity();
    private Button feedpet,pethome;
    private List<WelfareProject> welfareProject;//对象列表
    RecyclerView rv;
    SearchView search;
    View view;
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
        initCardview();
        return view;
    }
//监听事件
    private void onClick() {
       // 收养萌宠监听器
        feedpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AdoptPetActivity.class);
                startActivity(intent);
            }
        });
        //宝贝回家监听器
        pethome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FindPetActivity.class);
                startActivity(intent);
            }
        });
        //为搜索框加监听器
       search.setOnQueryTextListener(this);
    }

    private void initCardview() {
        //初始化cardview的数据
        initializeData();
        //添加布局管理器
        rv.setLayoutManager(new LinearLayoutManager(mContext, VERTICAL, false));
        rv.setNestedScrollingEnabled(false);//禁止滑动
        //添加适配器
        WelfareRvAdapter adapter = new WelfareRvAdapter(welfareProject,mContext);
        rv.setAdapter(adapter);
    }
//初始化cardview的数据
    private void initializeData() {
        welfareProject = new ArrayList<>();
        welfareProject.add(new WelfareProject(R.drawable.photo1,"老太太与狗狗朝夕相伴多年，狗狗忽然跳楼，却不见老太太露面","2017/12/30"));
        welfareProject.add(new WelfareProject(R.drawable.photo2,"主人晒出猫咪剪发时的表情，微博网友瞬间笑喷：让这眼神委屈到哭！","2018/1/12"));
        welfareProject.add(new WelfareProject(R.drawable.photo3,"小偷入室偷窃，打伤金毛，事后为何还要主人赔偿？","2016/5/20"));
    }
    private void findView() {
        feedpet=view.findViewById(R.id.welfare_feedpet);
        pethome=view.findViewById(R.id.welfare_pethome);
        rv=view.findViewById(R.id.welfare_recyclerview);
        search=view.findViewById(R.id.welfare_search);
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
