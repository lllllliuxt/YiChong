package com.a1074718775qq.yichong.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.bean.WelfareProject;

import java.util.List;

public class WelfareRvAdapter extends RecyclerView.Adapter<WelfareRvAdapter.WelfareViewHolder> {
    Context mContext;
    private List<WelfareProject> welfareProject;

    public WelfareRvAdapter()
    {}
    public WelfareRvAdapter(List<WelfareProject> welfareProject,Context mContext)
    {
        this.welfareProject=welfareProject;
        this.mContext=mContext;
    }
    @Override
    public WelfareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.welfare_cardview,parent,false);
        return new WelfareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WelfareViewHolder holder, int position) {
        holder.image.setImageResource(welfareProject.get(position).getImage());
        holder.title.setText(welfareProject.get(position).getTitle());
        holder.date.setText(welfareProject.get(position).getDate());
    }
    @Override
    public int getItemCount() {
        return welfareProject.size();
    }
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class WelfareViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView image;
        TextView title;
        TextView date;
        public WelfareViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.welfare_cardview);
            image = (ImageView)itemView.findViewById(R.id.welfare_image);
            title = (TextView)itemView.findViewById(R.id.welfare_title);
            date = (TextView)itemView.findViewById(R.id.welfare_date);
        }
    }
}
