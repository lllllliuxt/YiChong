package com.a1074718775qq.yichong.datebase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.bean.UserInfo;
import com.a1074718775qq.yichong.utils.PostToOss;

import java.util.HashMap;
import java.util.Map;

/**
 * create by 刘晓童
 * on 2018/2/12 0012
 */
public class MyDatebaseHelper extends SQLiteOpenHelper {
    //建用户信息表
//    state字段用来local来填充，使得每次修改只修改这一条记录
    final  private String userInfo="create table userInfo(id TEXT primary key,icon BLOB,usernick TEXT,phone TEXT,city TEXT,lovepet TEXT,feedyear TEXT,icontime TEXT)";
    private BitmapBytes bb;//Bitmap和byte格式相互转化的类
    public MyDatebaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
        bb=new BitmapBytes();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(userInfo);//创建用户表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
//注册界面将用户信息插入本地数据库
    public void insertIntoSqlite(MyDatebaseHelper db,Context context,Map<String,String> map) {
//       通过icon是否为0来判断是否需要初始化
        if (map.get("user_icon").equals("0"))
        {
            //获取默认的用户头像
            Bitmap iconImage=null;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            iconImage= BitmapFactory.decodeResource(context.getResources(), R.drawable.iconphoto);
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            byte[] iconByte=bb.bitmapIntobytes(iconImage);
            if (iconImage!=null&&!iconImage.isRecycled())//回收资源
            {
                iconImage.recycle();
                iconImage=null;
            }
            //            判断本地数据库是否有记录，有则更新，否则就插入
            Cursor cursor=db.getWritableDatabase().rawQuery("select id from userInfo where id="+map.get("user_id"),null);
            if(cursor.moveToNext())//当下一行存在值的时候
            {
                db.getReadableDatabase().execSQL("update userInfo set icon=?,usernick=?,city=?,lovepet=?,feedyear=?,icontime=? where id=?",new Object[]{iconByte,map.get("user_name"),map.get("user_city"),map.get("user_love_pet"),map.get("user_feed_year"),map.get("user_icon_time"),map.get("user_id")});
            }
            else {
                db.getReadableDatabase().execSQL("insert into userInfo values(?,?,?,?,?,?,?,?)", new Object[]{map.get("user_id"), iconByte, map.get("user_name"), map.get("user_phone"), map.get("user_city"), map.get("user_love_pet"), map.get("user_feed_year"),map.get("user_icon_time")});
            }
            cursor.close();
        }
        else
        {
            String icontime=map.get("user_icon_time");
            String userid=map.get("user_id");
//            从oss上获取头像数据
            PostToOss get=new PostToOss(context);
            get.initOss();
            Bitmap iconImage=get.download("user_icon/"+userid+"/"+icontime+ ".bmp");
            byte[] iconByte=bb.bitmapIntobytes(iconImage);
//            判断本地数据库是否有记录，有则更新，否则就插入
            Cursor cursor=db.getWritableDatabase().rawQuery("select id from userInfo where id="+userid,null);
            if(cursor.moveToNext())//当下一行存在值的时候
            {
                db.getReadableDatabase().execSQL("update userInfo set icon=?,usernick=?,city=?,lovepet=?,feedyear=?,icontime=? where id=?",new Object[]{iconByte,map.get("user_name"),map.get("user_city"),map.get("user_love_pet"),map.get("user_feed_year"),map.get("user_icon_time"),userid});
            }
            else {
                db.getReadableDatabase().execSQL("insert into userInfo values(?,?,?,?,?,?,?,?)", new Object[]{map.get("user_id"), iconByte, map.get("user_name"), map.get("user_phone"), map.get("user_city"), map.get("user_love_pet"), map.get("user_feed_year"), map.get("user_icon_time")});
            }
            cursor.close();
        }
    }

    public UserInfo getUserFragment(MyDatebaseHelper db,Context context,String userid) {
            UserInfo user=new UserInfo();
//        查询对应id的用户的头像，用户的昵称，用户的城市，用户的宠物
        Cursor cursor=db.getReadableDatabase().rawQuery("select * from userInfo where id="+userid,null);
        while(cursor.moveToNext())
        {
            user.setUser_icon(bb.bytesIntobitmap(cursor.getBlob(1)));
            user.setUser_name(cursor.getString(2));
            user.setUser_phone(cursor.getString(3));
            user.setUser_city(cursor.getString(4));
            user.setUser_love_pet(cursor.getString(5));
            user.setUser_feed_year(cursor.getString(6));
        }
        cursor.close();
        return user;
    }

    public String getUserIconInfo(MyDatebaseHelper db,Context context,String userid)
    {
        String IconTime=null;
        Cursor cursor=db.getReadableDatabase().rawQuery("select * from userInfo where id="+userid,null);
        while(cursor.moveToNext())
        {
         IconTime=cursor.getString(7);
        }
        cursor.close();
        return IconTime;
    }
}
