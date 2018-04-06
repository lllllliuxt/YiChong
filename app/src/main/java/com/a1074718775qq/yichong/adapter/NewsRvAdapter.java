package com.a1074718775qq.yichong.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.bean.PetNews;
import com.youth.xframe.cache.XCache;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class NewsRvAdapter extends RecyclerView.Adapter<NewsRvAdapter.NewsViewHolder> {
    Context mContext;
    private List<PetNews> news;
    XCache mcache;
    public NewsRvAdapter()
    {

    }
    public NewsRvAdapter(List<PetNews> news, Context mContext,XCache mcache)
    {
        this.news=news;
        this.mContext=mContext;
        this.mcache=mcache;
    }
    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        ImageView image;
        TextView title;
        TextView introduction;

        NewsViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.news_cardview);
            image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            introduction = itemView.findViewById(R.id.news_introduction);
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
    public void onBindViewHolder(final NewsRvAdapter.NewsViewHolder holder, final int position) {
//    如果有图片，则直接加载缓存里面的图片
        Bitmap temBmp=mcache.getAsBitmap("newsImage"+position);
        if(temBmp!=null)
        {
            holder.image.setImageBitmap(temBmp);
        }
        else {
            //新建线程加载图片信息，发送到消息队列中
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    final Bitmap bmp = getURLimage(news.get(position).getNews_picture());
//                    写入缓存
                    mcache.put("newsImage"+position,bmp,2*XCache.TIME_DAY);
                    holder.image.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.image.setImageBitmap(bmp);
                        }
                    });
                }
            }).start();
        }
        holder.title.setText(news.get(position).getNews_title());
        holder.introduction.setText(news.get(position).getNews_introduce());
    }
    @Override
    public int getItemCount() {
        return news.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    //添加数据
    public void addItem(List<PetNews> newDatas) {
        //mTitles.add(position, data);
       //notifyItemInserted(position);
        newDatas.addAll(news);
        news.removeAll(news);
        news.addAll(newDatas);
        //需要异步
        notifyDataSetChanged();
    }

    public void addMoreItem(List<PetNews> newDatas) {
        news.addAll(newDatas);
    }

    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
