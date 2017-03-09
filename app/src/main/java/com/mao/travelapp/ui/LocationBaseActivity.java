package com.mao.travelapp.ui;

import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.Locale;

public abstract class LocationBaseActivity extends BaseActivity {

    //定位相关
    LocationClient locationClient;
    BDLocationListener locationListener;
    Geocoder geocoder;

    private static final String TAG = "LocationBaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLocation();
    }

    private void initLocation() {
        geocoder = new Geocoder(getApplicationContext(), Locale.CHINA);
        locationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
        locationListener = new MyLocationListener();
        locationClient.registerLocationListener(locationListener);
    }

    public void requestLocation() {
        locationClient.start();
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            int TYPE = bdLocation.getLocType();
            if (TYPE == BDLocation.TypeGpsLocation || TYPE == BDLocation.TypeCacheLocation || TYPE == BDLocation.TypeNetWorkLocation) {
                receiveLocation(bdLocation);
            } else if (TYPE == BDLocation.TypeCriteriaException || TYPE == BDLocation.TypeNetWorkException) {
                Toast.makeText(LocationBaseActivity.this, "你的网络是否开启了", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LocationBaseActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
            }
            locationClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    abstract void receiveLocation(BDLocation location);

    public void setLocationClientOption(LocationClientOption option) {
        if (option != null) {
            locationClient.setLocOption(option);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient.isStarted()) {
            locationClient.stop();
        }
    }
}
