package com.a1074718775qq.yichong.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.bean.PetNews;
import com.a1074718775qq.yichong.bean.PetShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * create by 刘晓童
 * on 2018/2/1 0001
 */
public class PetShowRvAdapter extends RecyclerView.Adapter<PetShowRvAdapter.PetShowViewHolder>
{
    Context mContext;
    private List<PetShow> petShows;
    //构造函数
    public PetShowRvAdapter()
    {

    }
    public PetShowRvAdapter(List<PetShow> petShows, Context mContext)
    {
        this.petShows=petShows;
        this.mContext=mContext;
    }
    @Override
    public PetShowRvAdapter.PetShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.pet_show_cardview,parent,false);
        return new PetShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetShowRvAdapter.PetShowViewHolder holder, int position) {
        holder.icon.setImageResource(petShows.get(position).getImage());
        holder.nick.setText(petShows.get(position).getNick());
        holder.comment.setText(petShows.get(position).getComment());
        List<Map<String, Object>>  listitem = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < petShows.get(position).getPhoto().length; i++) {
            Map<String, Object> showitem = new HashMap<String, Object>();
            showitem.put("photo",petShows.get(position).getPhoto()[i]);
            listitem.add(showitem);
        }
        //创建一个simpleAdapter
        SimpleAdapter myAdapter = new SimpleAdapter(mContext, listitem, R.layout.griditem_addpic, new String[]{"photo"}, new int[]{R.id.imageView1});
        GridView gridView = holder.photo;
        gridView.setAdapter(myAdapter);
    }
    @Override
    public int getItemCount() {
        return petShows.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class PetShowViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        CircleImageView icon;
        TextView nick;
        TextView comment;
        GridView photo;
        public PetShowViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.pet_show_cardview);
            icon = itemView.findViewById(R.id.community_usericon);
            nick = itemView.findViewById(R.id.community_usernick);
            comment = itemView.findViewById(R.id.community_comment);
            photo=itemView.findViewById(R.id.community_photo);
        }
    }
}
