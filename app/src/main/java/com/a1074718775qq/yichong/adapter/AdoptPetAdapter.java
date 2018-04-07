package com.a1074718775qq.yichong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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
import com.a1074718775qq.yichong.bean.AdoptPet;
import com.a1074718775qq.yichong.bean.FindPet;
import com.a1074718775qq.yichong.utils.PostToOss;
import com.a1074718775qq.yichong.widget.MyGridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xuruyu on 2018/4/7.
 */

public class AdoptPetAdapter extends RecyclerView.Adapter<AdoptPetAdapter.AdoptPetViewHolder> {
    @NonNull
    Context mContext;
    private List<AdoptPet> adoptPets;
    //    导入oss方法
    PostToOss oss;
    public AdoptPetAdapter(@NonNull Context mContext, List<AdoptPet> adoptPets) {
        this.mContext = mContext;
        this.adoptPets = adoptPets;
        oss=new PostToOss(mContext);
        if (mContext!=null)
        {
            oss.initOss();
        }
    }

    public AdoptPetAdapter.AdoptPetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.adopt_view_cardview_item,parent,false);
        oss=new PostToOss(mContext);
        oss.initOss();
        return new AdoptPetAdapter.AdoptPetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdoptPetAdapter.AdoptPetViewHolder holder, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //        判断是否需要下载头像
                if (adoptPets.get(position).getUser_icon()=="0")//若没有头像
                {
                    holder.icon.setImageResource(R.drawable.iconphoto);
                }
                else
                {
                    final Bitmap bitmap = getIcon(adoptPets.get(position).getUser_id(), adoptPets.get(position).getUser_icon_time());
                    holder.icon.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.icon.setImageBitmap(bitmap);
                        }
                    },20);
                }
                holder.date.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.date.setText(getData(adoptPets.get(position).getAdopt_pet_time()));
                    }
                },10);
                holder.nick.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.nick.setText(adoptPets.get(position).getUser_name());
                    }
                },10);
                holder.comment.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.comment.setText(adoptPets.get(position).getAdopt_pet_context());
                    }
                },10);
                holder.address.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.address.setText(adoptPets.get(position).getAdopt_pet_address());
                    }
                },10);
                getPhoto(adoptPets.get(position).getUser_id(), adoptPets.get(position).getAdopt_pet_time(), adoptPets.get(position).getAdopt_pet_photo(), adoptPets.get(position));
                List<Map<String, Bitmap>>  listitem = new ArrayList<Map<String, Bitmap>>();
                for (int i = 0; i < adoptPets.get(position).getPhoto().length; i++) {
                    Map<String, Bitmap> showitem = new HashMap<String, Bitmap>();
                    showitem.put("photo",adoptPets.get(position).getPhoto()[i]);
                    listitem.add(showitem);
                }
                //创建一个simpleAdapter
                final SimpleAdapter myAdapter = new SimpleAdapter(mContext, listitem, R.layout.griditem_addpic, new String[]{"photo"}, new int[]{R.id.imageView1});
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
                holder.photo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MyGridView gridView = holder.photo;
                        gridView.setAdapter(myAdapter);
                    }
                },500);
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return adoptPets.size();
    }

    public class AdoptPetViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        CircleImageView icon;
        TextView date;
        TextView nick;
        TextView comment;
        TextView address;
        MyGridView photo;
        public AdoptPetViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.adopt_pet_cardview);
            icon = itemView.findViewById(R.id.adopt_pet_usericon);
            nick = itemView.findViewById(R.id.adopt_pet_usernick);
            date=itemView.findViewById(R.id.adopt_pet_date);
            comment = itemView.findViewById(R.id.adopt_pet_comment);
            address=itemView.findViewById(R.id.adopt_pet_address);
            photo=itemView.findViewById(R.id.adopt_pet_photo);
        }
    }

    //获取用户头像方法
    public Bitmap getIcon(final String id, final String iconTime)
    {
        final Bitmap receive;
        receive =oss.download("user_icon/"+id+"/"+iconTime+".bmp");
        Log.d("123","user_icon/"+id+"/"+iconTime+".bmp");
        return receive;
    }

    //将时间转化为指定格式的方法
    public String getData(final String time)
    {
        final String t1;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        long date=Long.valueOf(time);
        Date d1=new Date(date);
        t1 =format.format(d1);
        return t1;
    }

    //获取用户的头像保存在Petshow里面
    public void getPhoto(final String id, final String time, final int num, final AdoptPet adoptPet)
    {
        Bitmap[] bit=new Bitmap[num];
        for (int i=0;i<num;i++)
        {
            bit[i]=oss.download("adopt_pet/"+id+"/img_"+time+"_"+i+".bmp");
        }
        adoptPet.setPhoto(bit);
    }

    //添加数据
    public void addItem(List<AdoptPet> showDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        adoptPets.removeAll(adoptPets);
        adoptPets.addAll(showDatas);
        //需要异步
        notifyDataSetChanged();
    }

}
