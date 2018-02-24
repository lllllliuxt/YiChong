package com.a1074718775qq.yichong.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.a1074718775qq.yichong.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Toast;

import com.a1074718775qq.yichong.utils.HttpUtils;
import com.a1074718775qq.yichong.utils.PostToOss;
import com.a1074718775qq.yichong.widget.find_pet_dialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FindPetActivity extends AppCompatActivity implements OnItemClickListener,find_pet_dialog.OnButtonClickListener{
    Context mContext=FindPetActivity.this;
    private find_pet_dialog dialog;// 图片选择对话框
    public static final int NONE = 0;
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private GridView gridView; // 网格显示缩略图
    private final int IMAGE_OPEN = 4; // 打开图片标记
    private String pathImage; // 选择图片路径
    private Bitmap bmp; // 导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter; // 适配器
    private Button returnButton;
    private Button upload;
    private EditText editText;
    private  ArrayList<Bitmap> bit=new ArrayList<>();//保存bitmap
    //    用户id
    private int user_id;
    //    萌宠秀内容
    private String petContext;
    //    发布时间
    private long currentTime;
    PostToOss up=new PostToOss(mContext);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_pet);
        /*
         * 防止键盘挡住输入框 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findView();
        onClick();
        init();
        initData();
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
                if(!editText.getText().toString().equals("")&& simpleAdapter.getCount()>=1)
                {
                    final ProgressDialog progress = new ProgressDialog(mContext);
                    progress.setMessage("正在发布...");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
//                获取用户id
                    SharedPreferences sp = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
                    String userId=sp.getString("userId",null);
                    user_id = Integer.parseInt(userId);
//                  获取寻宠发布的内容
                    petContext = editText.getText().toString();
//                获取当前时间
                    currentTime = System.currentTimeMillis();
                    //               获取适配器所包含的所有图片数量
                    int count = bit.size();
                    up.initOss();
                    for (int i = 0; i <count; i++) {
                        up.upload("find_pet/"+user_id+"/img"+"_"+currentTime+"_"+i+ ".bmp",bit.get(i));
                    }
                    //创建一个Map对象
                    Map<String, Object> map = new HashMap<>();
                    map.put("find_pet_photo",count);
                    map.put("find_pet_context", petContext);
                    map.put("find_pet_time", currentTime);
                    map.put("user_id", user_id);
                    //转成JSON数据
                    final String json = JSON.toJSONString(map, true);
                    Log.e("json",json);
                    try {
                        HttpUtils.doPostAsy(getString(R.string.findpetInterface), json, new HttpUtils.CallBack() {
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
                }
                else
                {
                    Toast.makeText(mContext,"请输入内容和图片",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void findView() {
        returnButton=findViewById(R.id.find_pet_return_button);
        upload=findViewById(R.id.find_pet_confirm);
        editText=findViewById(R.id.find_pet_edittext);
    }


    private void init() {
        gridView = (GridView) findViewById(R.id.find_prt_gridView);
        gridView.setOnItemClickListener(this);
        dialog = new find_pet_dialog(this);
        dialog.setOnButtonClickListener(this);
        // activity中调用其他activity中组件的方法
        LayoutInflater layout = this.getLayoutInflater();
        View view = layout.inflate(R.layout.find_pet_picture, null);
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
        if (data == null)
            return;

        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
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
                Toast.makeText(FindPetActivity.this, "图片数9张已满",
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
        AlertDialog.Builder builder = new Builder(FindPetActivity.this);
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
