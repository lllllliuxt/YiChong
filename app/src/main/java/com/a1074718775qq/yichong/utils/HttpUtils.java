package com.a1074718775qq.yichong.utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * create by 刘晓童
 * on 2018/2/4 0004
 */
public class HttpUtils {
    //设置网络连接超时时间
    private static final int TIMEOUT_IN_MILLIONS = 5000;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    //设置请求结果回调
    public interface CallBack {
        void onRequestComplete(String result);
    }

    public static void doGetAsy(final String urlStr, final CallBack callBack) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                try {
                    result = doGet(urlStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (callBack != null) {
                    callBack.onRequestComplete(result);
                }
            }
        }).start();
    }


    /**
     * 异步post请求
     *
     * @param urlStr    url字符串
     * @param json      请求参数
     * @param callBack  以接口对象为参数
     */
    public static void doPostAsy(final String urlStr, final String json, final CallBack callBack)throws Exception  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                try {
                    result = doPost(urlStr, json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (callBack != null) {
                    callBack.onRequestComplete(result);
                }
            }
        }).start();
    }

    /**
     * 向指定url发送post方式的请求
     *
     * @param urlStr    url字符串
     * @param json      请求参数以JSON串的格式传递
     * @return
     */
    private static String doPost(String urlStr, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(urlStr)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * get请求
     *
     * @param urlStr    url字符串
     * @return
     */
    private static String doGet(String urlStr) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
