package com.a1074718775qq.yichong.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.a1074718775qq.yichong.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.a1074718775qq.yichong.utils.HttpUtils;
import com.a1074718775qq.yichong.utils.LocationFromGaode;
import com.a1074718775qq.yichong.utils.PostToOss;
import com.a1074718775qq.yichong.widget.MyDialog;
import com.a1074718775qq.yichong.widget.MyGridView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;


public class AdoptPetActivity extends AppCompatActivity implements OnItemClickListener,MyDialog.OnButtonClickListener {
    private Context mContext=AdoptPetActivity.this;
    private Button returnButton,upload;
    private EditText edittext;
    private MyDialog dialog;// 图片选择对话框
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private MyGridView gridView; // 网格显示缩略图
    private final int IMAGE_OPEN = 4; // 打开图片标记
    private String pathImage; // 选择图片路径
    private Bitmap bmp; // 导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter; // 适配器'
    //照相机的相关变量
    private  Uri contentUri;
    private File file;
    //    用户id
    private int user_id;
    //    收养内容
    private String petContext;
    //    发布时间
    private long currentTime;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
     private TextView address;
    private  ArrayList<Bitmap> bit=new ArrayList<>();//保存bitmap
    PostToOss up=new PostToOss(mContext);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_adopt_pet);
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
        initData();
        findView();
        onClick();
        getAddress();
    }

    private void getAddress() {
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
                        String province=amapLocation.getProvince();//省信息
                        String city=amapLocation.getCity();//城市信息
                        String block=amapLocation.getDistrict();//城区信息
                        String street= amapLocation.getStreet();//街道信息
                        String streetNumber=amapLocation.getStreetNum();//街道门牌号信息
                        String addresstext=province+city+block+street+streetNumber;
                        address.setText(addresstext);
                        if (progress.isShowing())
                            progress.dismiss();
                    } else {
                        Toast.makeText(mContext,"定位失败,请检查定位设置",Toast.LENGTH_SHORT).show();
                        address.setText("定位失败");
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

    private void onClick() {
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.getText()=="定位失败")
                {
                    Toast.makeText(mContext,"为了方便更好的救助，请您打开定位",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (!edittext.getText().toString().equals("") && simpleAdapter.getCount() >= 1) {
                        final ProgressDialog progress = new ProgressDialog(mContext);
                        progress.setMessage("正在发布...");
                        progress.setCanceledOnTouchOutside(false);
                        progress.show();
//                  获取用户id
                        SharedPreferences sp = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
                        String userId = sp.getString("userId", null);
                        user_id = Integer.parseInt(userId);
//                  获取寻宠发布的内容
                        petContext = edittext.getText().toString();
//                获取当前时间
                        currentTime = System.currentTimeMillis();
                        //               获取适配器所包含的所有图片数量
                        int count = bit.size();
                        up.initOss();
                        for (int i = 0; i < count; i++) {
                            up.upload("adopt_pet/" + user_id + "/img" + "_" + currentTime + "_" + i + ".bmp", bit.get(i));
                        }
                        //创建一个Map对象
                        Map<String, Object> map = new HashMap<>();
                        map.put("adopt_photo", count);
                        map.put("adopt_context", petContext);
                        map.put("adopt_time", currentTime);
                        map.put("user_id", user_id);
                        //转成JSON数据
                        final String json = JSON.toJSONString(map, true);
                        Log.e("json", json);
                        try {
                            HttpUtils.doPostAsy(getString(R.string.adoptInterface), json, new HttpUtils.CallBack() {
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
                                        finish();
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
                    } else {
                        Toast.makeText(mContext, "请输入内容和图片", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    private void findView() {
        returnButton=findViewById(R.id.abopt_pet_return_button);
        upload=findViewById(R.id.adopt_pet_upload);
        edittext=findViewById(R.id.adopt_pet_text);
        address=findViewById(R.id.adopt_pet_address);
    }

    private void init() {
        gridView = (MyGridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);
        dialog = new MyDialog(this);
        dialog.setOnButtonClickListener(this);
        // activity中调用其他activity中组件的方法
        LayoutInflater layout = this.getLayoutInflater();
        View view = layout.inflate(R.layout.select_photo, null);
    }
    private void initData() {
        /*
         * 载入默认图片添加图片加号
         */
        bmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.gridview_addpic); // 加号
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, imageItem,
                R.layout.griditem_addpic, new String[] { "itemImage" },
                new int[] { R.id.imageView1 });
        simpleAdapter.setViewBinder(new ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(simpleAdapter);
    }
    public void camera() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,contentUri);
        startActivityForResult(intent, PHOTOHRAPH);
    }


    public void gallery() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_OPEN);

    }


    public void cancel() {
        // TODO Auto-generated method stub
        dialog.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            // 设置文件保存路径这里放在跟目录下
            startPhotoZoom(contentUri);
        }
        if (data == null)
            return;

        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
                bit.add(photo);
                // 将图片放入gridview中
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("itemImage", photo);
                imageItem.add(map);
                simpleAdapter = new SimpleAdapter(this, imageItem,
                        R.layout.griditem_addpic, new String[] { "itemImage" },
                        new int[] { R.id.imageView1 });
                simpleAdapter.setViewBinder(new ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data,
                                                String textRepresentation) {
                        // TODO Auto-generated method stub
                        if (view instanceof ImageView && data instanceof Bitmap) {
                            ImageView i = (ImageView) view;
                            i.setImageBitmap((Bitmap) data);
                            return true;
                        }
                        return false;
                    }
                });
                gridView.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

        }
        // 打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            startPhotoZoom(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this, imageItem,
                    R.layout.griditem_addpic, new String[] { "itemImage" },
                    new int[] { R.id.imageView1 });
            simpleAdapter.setViewBinder(new ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            // 刷新后释放防止手机休眠后自动添加
            pathImage = null;
            dialog.dismiss();
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // TODO Auto-generated method stub
        if (imageItem.size() == 10) { // 第一张为默认图片

            if (position!=0) {
                dialog(position);
            }else{
                Toast.makeText(AdoptPetActivity.this, "图片数9张已满",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (position == 0) { // 点击图片位置为+ 0对应0张图片
            // 选择图片
            dialog.show();
        } else {
            dialog(position);
        }

    }

    /*
     * Dialog对话框提示用户删除操作 position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new Builder(AdoptPetActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
        startActivityForResult(intent, PHOTORESOULT);
    }
}
