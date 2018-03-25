package com.a1074718775qq.yichong.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.a1074718775qq.yichong.datebase.BitmapBytes;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandler;
import java.util.Random;

/**
 * create by 刘晓童
 * on 2018/2/10 0010
 */
//将图片上传给oss，包括上传方法和下载方法，对方法的封装
public class PostToOss {
    Bitmap bitmap;
    Context context;
    String endpoint = "oss-cn-beijing.aliyuncs.com";
    OSS oss;
    BitmapBytes bit;
    // 更多信息可查看sample 中 sts 使用方式(https://github.com/aliyun/aliyun-oss-android-sdk/tree/master/app/src/main/java/com/alibaba/sdk/android/oss/app)
    OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIGiaVMzfmHfnj", "YcIT2xhhTSPHOazZbXLkTYc7yFjPdD");
   public PostToOss()
   {

   }
   public PostToOss(Context context)
   {
       this.context=context;
   }
    //初始化oss
    public void initOss() {
    //该配置类如果不设置，会有默认配置，具体可看该类
    ClientConfiguration conf = new ClientConfiguration();
    conf.setConnectionTimeout(15*1000); // 连接超时，默认15秒
    conf.setSocketTimeout(15*1000); // socket超时，默认15秒
    conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
    conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
    //OSSLog.enableLog();
    oss= new OSSClient(context, endpoint, credentialProvider);
    bit=new BitmapBytes();
    }

    public void upload(String filename, Bitmap img)
    {
        byte[] datas = bit.bitmapIntobytes(img);
        Log.v("上传数据的长度",datas.toString());
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("ipet-image", filename,datas );
        try {
            PutObjectResult putResult = oss.putObject(put);
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
    }

    public Bitmap download(String objectname) {
        final GetObjectRequest get = new GetObjectRequest("ipet-image", objectname);
                try {
                    // 同步执行下载请求，返回结果
                    GetObjectResult getResult = oss.getObject(get);
                    // 获取文件输入流
                    InputStream inputStream = getResult.getObjectContent();
                    long len=getResult.getContentLength();
                    int size=Long.valueOf(len).intValue();
                    if (inputStream != null && size > 0 ) {
                        byte[] b = new byte[size];
                        try {
                            inputStream.read(b);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.v("下载数据的长度",b.toString());
                        bitmap = bit.bytesIntobitmap(b);
                        Log.v("bitmap",bitmap.toString());
                        // 下载后可以查看文件元信息
                        ObjectMetadata metadata = getResult.getMetadata();
                        Log.d("查看文件元信息", metadata.getContentType());
                    }
                } catch (ClientException e) {
                    // 本地异常如网络异常等
                    e.printStackTrace();
                } catch (ServiceException e) {
                    // 服务异常
                    Log.e("RequestId", e.getRequestId());
                    Log.e("ErrorCode", e.getErrorCode());
                    Log.e("HostId", e.getHostId());
                    Log.e("RawMessage", e.getRawMessage());
                }
        return bitmap;
    }
}