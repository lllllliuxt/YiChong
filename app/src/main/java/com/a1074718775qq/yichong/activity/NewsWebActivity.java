package com.a1074718775qq.yichong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillValue;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.a1074718775qq.yichong.R;

public class NewsWebActivity extends AppCompatActivity {
    Context mContext=NewsWebActivity.this;
    private WebView wb;
    private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
        findView();
        //获取数据
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        wb.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        WebSettings webSettings = wb.getSettings();
        //支持javaScrip
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 设置可以支持缩放
       webSettings.setSupportZoom(true);
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        String cacheDirPath = mContext.getFilesDir().getAbsolutePath()+"cache/";
        //手势缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(true);

        webSettings.setAppCachePath(cacheDirPath);
        // 1. 设置缓存路径
        webSettings.setAppCacheMaxSize(20*1024*1024);
        // 2. 设置缓存大小
        webSettings.setAppCacheEnabled(true);
        // 3. 开启Application Cache存储机制


        wb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        wb.loadUrl(url);
    }

    private void findView() {
        wb=findViewById(R.id.news_webview);
        bar=findViewById(R.id.new_probar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    protected void onDestroy() {
        if (wb != null) {
            wb.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wb.clearCache(true);
            wb.clearHistory();
            ((ViewGroup) wb.getParent()).removeView(wb);
            wb.destroy();
            wb = null;
        }
        super.onDestroy();
    }
}
