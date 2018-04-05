package com.a1074718775qq.yichong.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.bean.UserInfo;
import com.a1074718775qq.yichong.datebase.MyDatebaseHelper;
import com.a1074718775qq.yichong.utils.HttpUtils;
import com.a1074718775qq.yichong.utils.LocationFromGaode;
import com.a1074718775qq.yichong.utils.PostToOss;
import com.a1074718775qq.yichong.widget.MyDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,MyDialog.OnButtonClickListener{
    Context mContext=UserInfoActivity.this;
    private Button addresButton,returnButton,confirmButton;
    private MyDialog dialog;// 图片选择对话框
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    //照相机的相关变量
    private Uri contentUri;
    private File file;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    PostToOss up=new PostToOss(mContext);
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //记录获取到的地址
    private  String address;
    //保存bitmap
    private ArrayList<Bitmap> bit=new ArrayList<>();
//    用户头像
    private CircleImageView icon;
//    用户昵称，手机号
    EditText user_nick,user_phone;
    String userNick,userCity,userPet,userDate,userId,iconTime,userIcon;
//    用户城市
    TextView addressText;
//    用户喜欢的宠物和养宠年限
    Spinner user_pet,user_date;
    //sqlite数据库
    private MyDatebaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_info);
        /*
         * 防止键盘挡住输入框 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        file=new File(mContext.getExternalCacheDir(), "temp.jpg");
        //判断安卓版本从而申请照相机
        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
            contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.a1074718775qq.yichong.provider", file);
        }else{
            contentUri = Uri.fromFile(file);
        }
        init();
        findView();
        onClick();
        initinfo();
    }

    private void initinfo() {
        //  获取用户id
        SharedPreferences sp = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        String userId=sp.getString("userId",null);
        db = new MyDatebaseHelper(mContext, "userInfo.db", 1);
//        查询用户的头像，用户的昵称，用户的城市，用户的宠物
        UserInfo user=db.getUserFragment(db,mContext,userId);
        icon.setImageBitmap(user.getUser_icon());
        user_nick.setHint(user.getUser_name());
        user_phone.setHint(user.getUser_phone());
        addressText.setText(user.getUser_city());
        setSpinnerItemSelectedByValue(user_pet,user.getUser_love_pet());
        setSpinnerItemSelectedByValue(user_date,user.getUser_feed_year());
    }

    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }

    private void init() {
        dialog = new MyDialog(this);
        dialog.setOnButtonClickListener(this);
        // activity中调用其他activity中组件的方法
        LayoutInflater layout = this.getLayoutInflater();
        View view = layout.inflate(R.layout.select_photo, null);
    }
//按钮监听
    private void onClick() {
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        icon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                showChoosePicDialog();
            }
        });

        addresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    final ProgressDialog progress = new ProgressDialog(mContext);
                    progress.setMessage("正在定位...");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                    //实例化定位回调监听器
                    mLocationListener = new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation amapLocation)
                        {
                            if (amapLocation!= null) {
                                if (amapLocation.getErrorCode() == 0) {
                                    //可在其中解析amapLocation获取相应内容。
                                    //String country=amapLocation.getCountry();//国家信息
                                    //String province=amapLocation.getProvince();//省信息
                                    String city=amapLocation.getCity();//城市信息
                                    String block=amapLocation.getDistrict();//城区信息
                                   // String street= amapLocation.getStreet();//街道信息
                                    //String streetNumber=amapLocation.getStreetNum();//街道门牌号信息
                                    address=city+block;
                                    addressText.setText(address);
                                    if (progress.isShowing())
                                        progress.dismiss();
                                } else {
                                    Toast.makeText(mContext,"定位失败",Toast.LENGTH_SHORT).show();
                                    addressText.setText("定位失败");
                                    if (progress.isShowing())
                                        progress.dismiss();
                                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                                    Log.e("AmapError", "location Error, ErrCode:"
                                            + amapLocation.getErrorCode() + ", errInfo:"
                                            + amapLocation.getErrorInfo());
                                }
                            }
                        }
                    };
                    //获取定位来触发监听器
                    new LocationFromGaode(mContext,mLocationListener).getLocation();
                }
            }
        });

        //完成按钮监听事件
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(mContext);
                progress.setMessage("正在上传...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                db=new MyDatebaseHelper(mContext,"userInfo.db", 1);
                //  获取用户id
                SharedPreferences sp = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
                userId=sp.getString("userId",null);
                if(user_nick.getText().toString().equals(""))
                {
                    userNick=user_nick.getHint().toString();
                }
                else
                {
                    userNick=user_nick.getText().toString();
                }
                userCity= (String) addressText.getText();
                userPet=user_pet.getSelectedItem().toString();
                userDate=user_date.getSelectedItem().toString();
//                如果没有上传头像
                if (bit.size()==0)
                {
//                  查询本地数据库中的头像是否为默认的，如果是，则说明用户仍然使用默认头像，否则用户使用的是以前的头像
//                    通过用户的id来查询出用户的icontime和userIcon的值
                   if(db.getUserIconInfo(db, mContext, userId).equals("0"))
                   {
//                      没有用户头像且用户没有改变默认头像
                       userIcon="0";
                       iconTime="0";
                   }
                   else
                   {
//                       没有用户头像但是用户上传过头像
                       userIcon="1";
                       iconTime=db.getUserIconInfo(db,mContext,userId);
                   }
                }
                else
                {
//                    更改头像
//                    获取需要上传的信息
                    iconTime= String.valueOf(System.currentTimeMillis());
                    userIcon="1";
                    up.initOss();
                    up.upload("user_icon/"+userId+"/"+iconTime+ ".bmp",bit.get(0));
                }
//                将数据上传到数据库
                //创建一个Map对象
                Map<String, String> map = new HashMap<>();
                map.put("user_id",userId);
                map.put("user_icon",userIcon);
                map.put("user_name", userNick);
                map.put("user_city", userCity);
                map.put("user_love_pet", userPet);
                map.put("user_feed_year", userDate);
                map.put("user_icon_time", iconTime);
                //转成JSON数据
                final String json = JSON.toJSONString(map, true);
                try {
                    HttpUtils.doPostAsy(getString(R.string.UpuserInfoInterface), json, new HttpUtils.CallBack() {
                        public void onRequestComplete(final String result) {
                            Log.e("返回结果", result);
                            JSONObject jsonObject = JSON.parseObject(result.trim());
                            final String psresult = jsonObject.getString("result");
                            if (psresult.equals("上传成功")) {
                                if (progress.isShowing())
                                    progress.dismiss();
                                //解决在子线程中调用Toast的异常情况处理
                                Looper.prepare();
                                Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                if (progress.isShowing())
                                    progress.dismiss();
                                Looper.prepare();
                                Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    });
                } catch (Exception e) {
                    if (progress.isShowing())
                        progress.dismiss();
                    Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
//              将数据写到本地
                db.insertIntoSqlite(db,mContext,map);
                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                intent.putExtra("data","refresh");
                LocalBroadcastManager.getInstance(UserInfoActivity.this).sendBroadcast(intent);
                sendBroadcast(intent);
//              返回到个人信息界面
                finish();
            }
        });
    }
//    组件绑定
    private void findView() {
        addresButton=findViewById(R.id.user_getaddress);
        returnButton=findViewById(R.id.user_info_return_button);
        addressText=findViewById(R.id.user_city);
        icon=findViewById(R.id.user_icon);
        user_nick=findViewById(R.id.user_nick);
        user_phone=findViewById(R.id.user_phone);
        user_pet=findViewById(R.id.user_pet);
        user_date=findViewById(R.id.user_date);
        confirmButton=findViewById(R.id.user_commit);

    }

//选择图片的对话框
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
                               // Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(contentUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
//    将图片显示到头像框
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            icon.setImageBitmap(photo);
            bit.add(photo);//将头像的bitmap保存
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        intent.putExtra("return-data", true);
        startActivityForResult(intent,CROP_SMALL_PICTURE);
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialog.show();
    }
    public void camera() {

    }
    public void gallery() {

    }
    public void cancel() {

    }
    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        if (db != null) {
            db.close();
        }
    }
}
