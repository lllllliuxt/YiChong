package com.a1074718775qq.yichong.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.a1074718775qq.yichong.R;
import com.a1074718775qq.yichong.utils.LocationFromGaode;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

public class UserInfoActivity extends AppCompatActivity {
    Context mContext=UserInfoActivity.this;
    private Button addresButton;
    private Button returnButton;
    private TextView addressText;
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
        setContentView(R.layout.activity_user_info);
        findView();
        onClick();
    }

    private void onClick() {
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void findView() {
        addresButton=findViewById(R.id.user_getaddress);
        returnButton=findViewById(R.id.user_info_return_button);
        addressText=findViewById(R.id.user_city);
    }

}
