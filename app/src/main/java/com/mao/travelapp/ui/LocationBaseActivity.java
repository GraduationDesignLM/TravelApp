package com.mao.travelapp.ui;

import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.mao.travelapp.App;
import com.mao.travelapp.bean.AddressBean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class LocationBaseActivity extends BaseActivity {

    //定位相关
    LocationClient locationClient;
    BDLocationListener locationListener;
    Geocoder geocoder;

    private GetLatitudeAndLongitudeCallBack getLatitudeAndLongitudeCallBack;
    private static final String TAG = "LocationBaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initLocation();
    }

    private void initView() {
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

    /**
     * @param address place which need to get latitude and longitude.
     */
    public void getLatitudeAndLongitudeByAddress(String address, final GetLatitudeAndLongitudeCallBack callBack) {
        try {
            address = URLDecoder.decode(address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String strAddress = "http://api.map.baidu.com/geocoder/v2/?ak=" + App.Baidu_AK + "&address=" + address + "&output=json&mcode=" + App.SHA1_PACKAGE;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(strAddress);
        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                AddressBean addressBean = gson.fromJson(response.body().charStream(), AddressBean.class);
//                Message message = Message.obtain();
//                if ((addressBean == null) || (addressBean.getStatus() != 0)) {
//                    message.what = PublishMainActivity.REQUEST_GET_LATITUDE_LONGITUDE_FAIL;
//                } else {
//                    message.what = PublishMainActivity.REQUEST_GET_LATITUDE_LONGITUDE_SUCCESS;
//                    message.obj = addressBean;
//                }
//                locationHandler.sendMessage(message);
                    callBack.handleData(addressBean);
            }
        });
    }

    interface GetLatitudeAndLongitudeCallBack {
        public void handleData(AddressBean addressBean);
    }

    public void setGetLatitudeAndLongitudeCallBack(GetLatitudeAndLongitudeCallBack callBack) {
        getLatitudeAndLongitudeCallBack = callBack;
    }


}
