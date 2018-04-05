package com.a1074718775qq.yichong.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.a1074718775qq.yichong.datebase.BitmapBytes;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import java.io.InputStream;

/**
 * create by 刘晓童
 * on 2018/2/10 0010
 */
//将图片上传给oss，包括上传方法和下载方法，对方法的封装
public class PostToOss {
    private Bitmap bitmap;
    private Context context;
    private OSS oss;
    private BitmapBytes bit;
    /**
     * 加入key
     */
    // 更多信息可查看sample 中 sts 使用方式(https://github.com/aliyun/aliyun-oss-android-sdk/tree/master/app/src/main/java/com/alibaba/sdk/android/oss/app)
    private OSSCredentialProvider credentialProvider;

    public PostToOss(Context context)
   {
       this.context=context;
       credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIGiaVMzfmHfnj", "YcIT2xhhTSPHOazZbXLkTYc7yFjPdD");
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
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        oss= new OSSClient(context, endpoint, credentialProvider);
    bit=new BitmapBytes();
    }

    public void upload(String filename, Bitmap img)
    {
        byte[] datas = bit.bitmapIntobytes(img);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("ipet-image", filename,datas);
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

    public Bitmap download(String objectname){
        GetObjectRequest get = new GetObjectRequest("ipet-image", objectname);
        try {
            OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
                @Override
                public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                    // 请求成功
                    InputStream inputStream = result.getObjectContent();
                    bitmap=BitmapFactory.decodeStream(inputStream);
                }
                @Override
                public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace();
                    }
                    if (serviceException != null) {
                        // 服务异常
                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                    }
                }
            });
            task.getResult();
        }
        catch (ClientException | ServiceException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }
}