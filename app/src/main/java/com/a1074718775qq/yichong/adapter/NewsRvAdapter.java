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
import com.a1074718775qq.yichong.bean.PetNews;

import java.util.List;

public class NewsRvAdapter extends RecyclerView.Adapter<NewsRvAdapter.NewsViewHolder> {
    Context mContext;
    private List<PetNews> news;
    public NewsRvAdapter()
    {

    }
    public NewsRvAdapter(List<PetNews> news, Context mContext)
    {
        this.news=news;
        this.mContext=mContext;
    }
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView image;
        TextView title;
        TextView introduction;

        NewsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.news_cardview);
            image = (ImageView)itemView.findViewById(R.id.news_image);
            title = (TextView)itemView.findViewById(R.id.news_title);
            introduction = (TextView)itemView.findViewById(R.id.news_introduction);
        }
    }
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_cardview_item,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsRvAdapter.NewsViewHolder holder, int position) {
        holder.image.setImageResource(news.get(position).getImage());
        holder.title.setText(news.get(position).getTitle());
        holder.introduction.setText(news.get(position).getIntroduction());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
