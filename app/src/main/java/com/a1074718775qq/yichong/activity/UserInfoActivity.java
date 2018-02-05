package com.a1074718775qq.yichong.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.utils.LocationFromGaode;
import com.a1074718775qq.yichong.widget.MyDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,MyDialog.OnButtonClickListener{
    Context mContext=UserInfoActivity.this;
    private Button addresButton;
    private Button returnButton;
    private MyDialog dialog;// 图片选择对话框
    private TextView addressText;
    private CircleImageView icon;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    //照相机的相关变量
    private Uri contentUri;
    private File file;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //记录获取到的地址
    private  String address;
    //定义一个获取定位消息的类
    LocationFromGaode getAddress;
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
    }
    private void init() {
        dialog = new MyDialog(this);
        dialog.setOnButtonClickListener(this);
        // activity中调用其他activity中组件的方法
        LayoutInflater layout = this.getLayoutInflater();
        View view = layout.inflate(R.layout.select_photo, null);
    }

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
                                } else {
                                    Toast.makeText(mContext,"定位失败",Toast.LENGTH_SHORT).show();
                                    addressText.setText("定位失败");
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
    }

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
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
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
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            icon.setImageBitmap(photo);
        }
    }
    private void findView() {
        addresButton=findViewById(R.id.user_getaddress);
        returnButton=findViewById(R.id.user_info_return_button);
        addressText=findViewById(R.id.user_city);
        icon=findViewById(R.id.user_icon);
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
}
