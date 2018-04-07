package com.a1074718775qq.yichong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.bean.WelfareProject;
import com.youth.xframe.cache.XCache;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WelfareRvAdapter extends RecyclerView.Adapter<WelfareRvAdapter.WelfareViewHolder> {
    Context mContext;
    private List<WelfareProject> welfareProject;
    XCache mcache;
    public WelfareRvAdapter()
    {}
    public WelfareRvAdapter(List<WelfareProject> welfareProject,Context mContext,XCache mcache)
    {
        this.welfareProject=welfareProject;
        this.mContext=mContext;
        this.mcache=mcache;
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
    public void onBindViewHolder(final WelfareViewHolder holder, final int position) {
        //    如果有图片，则直接加载缓存里面的图片
        Bitmap temBmp=mcache.getAsBitmap("welfareImage"+position);
        if(temBmp!=null)
        {
            holder.picture.setImageBitmap(temBmp);
        }
        else {
            //新建线程加载图片信息，发送到消息队列中
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    final Bitmap bmp = getURLimage(welfareProject.get(position).getWelfare_picture());
                    mcache.put("welfareImage"+position,bmp,15*XCache.TIME_DAY);
                    holder.picture.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.picture.setImageBitmap(bmp);
                        }
                    },50);
                }
            }).start();
        }
        holder.title.setText(welfareProject.get(position).getWelfare_title());
        holder.phone.setText(welfareProject.get(position).getWelfare_phone());
        holder.address.setText(welfareProject.get(position).getWelfare_address());
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
        ImageView picture;
        TextView title;
        TextView phone;
        TextView address;
        public WelfareViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.welfare_cardview);
            title=itemView.findViewById(R.id.welfare_title);
            picture = itemView.findViewById(R.id.welfare_image);
            phone = itemView.findViewById(R.id.welfare_phone);
            address = itemView.findViewById(R.id.welfare_address);
        }
    }

    //添加数据
    public void addItem(List<WelfareProject> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(welfareProject);
        welfareProject.removeAll(welfareProject);
        welfareProject.addAll(newDatas);
        //需要异步
        notifyDataSetChanged();
    }

    public void addMoreItem(List<WelfareProject> newDatas) {
        welfareProject.addAll(newDatas);
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
