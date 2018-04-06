package com.a1074718775qq.yichong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.bean.PetShow;
import com.a1074718775qq.yichong.utils.PostToOss;
import com.a1074718775qq.yichong.widget.MyGridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.Util;

/**
 * create by 刘晓童
 * on 2018/2/1 0001
 */
public class PetShowRvAdapter extends RecyclerView.Adapter<PetShowRvAdapter.PetShowViewHolder>
{
    Context mContext;
    private List<PetShow> petShows;
//    导入oss方法
    PostToOss oss;
    //构造函数

    public PetShowRvAdapter(List<PetShow> petShows, Context mContext)
    {
        this.petShows=petShows;
        this.mContext=mContext;
        oss=new PostToOss(mContext);
        if (mContext!=null)
        {
            oss.initOss();
        }
    }
    public class PetShowViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        CircleImageView icon;
        TextView date;
        TextView nick;
        TextView comment;
        MyGridView photo;
        public PetShowViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.pet_show_cardview_item);
            icon = itemView.findViewById(R.id.community_usericon);
            nick = itemView.findViewById(R.id.community_usernick);
            date=itemView.findViewById(R.id.community_date);
            comment = itemView.findViewById(R.id.community_comment);
            photo=itemView.findViewById(R.id.community_photo);
        }
    }
    @Override
    public PetShowRvAdapter.PetShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        oss=new PostToOss(mContext);
        oss.initOss();
        View view = LayoutInflater.from(mContext).inflate(R.layout.pet_show_cardview,parent,false);
        return new PetShowViewHolder(view);
    }
//往cardview里面加数据
    @Override
    public void onBindViewHolder(PetShowRvAdapter.PetShowViewHolder holder, int position) {
//        判断是否需要下载头像
        if (petShows.get(position).getUser_icon()=="0")//若没有头像
        {
            holder.icon.setImageResource(R.drawable.iconphoto);
        }
        else {
            Bitmap bitmap=getIcon(petShows.get(position).getUser_id(), petShows.get(position).getUser_icon_time());
            holder.icon.setImageBitmap(bitmap);
        }
//        获取用户发布的图片
        getPhoto(petShows.get(position).getUser_id(),petShows.get(position).getPet_show_time(),petShows.get(position).getPet_show_photo(),petShows.get(position));
        holder.date.setText(getData(petShows.get(position).getPet_show_time()));
        holder.nick.setText(petShows.get(position).getUser_name());
        holder.comment.setText(petShows.get(position).getPet_show_context());
        List<Map<String, Bitmap>>  listitem = new ArrayList<Map<String, Bitmap>>();
        for (int i = 0; i < petShows.get(position).getPhoto().length; i++) {
            Map<String, Bitmap> showitem = new HashMap<String, Bitmap>();
            showitem.put("photo",petShows.get(position).getPhoto()[i]);
            listitem.add(showitem);
        }
        //创建一个simpleAdapter
        SimpleAdapter myAdapter = new SimpleAdapter(mContext, listitem, R.layout.griditem_addpic, new String[]{"photo"}, new int[]{R.id.imageView1});
        myAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object bitmapData, String s) {
                if(view instanceof ImageView && bitmapData instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) bitmapData);
                    return true;
                }
                return false;
            }
        });
        MyGridView gridView = holder.photo;
        gridView.setAdapter(myAdapter);
    }
//获取用户头像方法
    public Bitmap getIcon(String id,String iconTime)
    {
        Bitmap receive;
        receive=oss.download("user_icon/"+id+"/"+iconTime+".bmp");
        return receive;
    }
//将时间转化为指定格式的方法
    public String getData(String time)
    {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        long date=Long.valueOf(time);
        Date d1=new Date(date);
        String t1=format.format(d1);
        return t1;
    }
//获取用户的头像保存在Petshow里面
    public void getPhoto(String id, String time, int num,PetShow petShow)
    {
        Bitmap[] bit=new Bitmap[num];
        for (int i=0;i<num;i++)
        {
           bit[i]=oss.download("pet_show/"+id+"/img_"+time+"_"+i+".bmp");
        }
        petShow.setPhoto(bit);
    }


    @Override
    public int getItemCount() {
        return petShows.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    //添加数据
    public void addItem(List<PetShow> showDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        showDatas.addAll(petShows);
        petShows.removeAll(petShows);
        petShows.addAll(showDatas);
        //需要异步
        notifyDataSetChanged();
    }

    public void addMoreItem(List<PetShow> showDatas) {
        petShows.addAll(showDatas);
    }
}
