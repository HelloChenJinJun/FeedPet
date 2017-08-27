package com.example.cootek.feedpet;

import android.content.SharedPreferences;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.feedpet.bean.DistanceItem;
import com.example.cootek.feedpet.bean.UserBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class MainApplication extends BaseApplication implements LocationChangedListener {

    private static MainComponent mainComponent;
    private SharedPreferences sharedPreferences;
    private double preLatitude;
    private double preLongtitude;

    @Override
    public void onCreate() {
        super.onCreate();
        initMain();
    }

    private void initMain() {
        sharedPreferences = getSharedPreferences("time", MODE_PRIVATE);
        mainComponent = DaggerMainComponent.builder().mainModule(new MainModule()).appComponent(getAppComponent()).build();
        LocationManager.getInstance().registerLocationListener(this);
    }


    public static MainComponent getMainComponent() {
        return mainComponent;

    }


    private long totalDistance;

    @Override
    public void onLocationChanged(List<String> addressList, double latitude, double longitude) {
        if (sharedPreferences != null && sharedPreferences.getBoolean("isNear", false)) {
//            在旁边的前提下
            CommonLogger.e("获取到最新的距离啦啦");
            final int distance = getDistance(longitude, latitude);
            CommonLogger.e("获取到最新的距离啦啦::" + distance);
            List<UserBean> list = MainApplication.getMainComponent().getDaoSession().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.User_id.eq("1")).list();
            if (list != null && list.size() > 0) {
                UserBean userBean = list.get(0);
                userBean.setDistance(distance + userBean.getDistance());
                getMainComponent().getDaoSession().getUserBeanDao().insertOrReplace(userBean);
                RefreshEvent refreshEvent = new RefreshEvent();
                refreshEvent.setTime((int) userBean.getTime());
                refreshEvent.setDistance(userBean.getDistance());
                RxBusManager.getInstance().post(refreshEvent);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DistanceItem distanceItem = new DistanceItem();
                    UserBean bean = getMainComponent().getDaoSession().getUserBeanDao().queryBuilder().list().get(0);
                    distanceItem.setFlag(0);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    distanceItem.setEnd_time(simpleDateFormat.format(new Date(System.currentTimeMillis())));
                    distanceItem.setUser_id(bean.getUser_id());
                    distanceItem.setDevice(bean.getDevice());
                    distanceItem.setDistance(distance + "");
                    OkHttpClient okHttpClient = getAppComponent().getOkHttpClient();
                    Gson gson = new Gson();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("text"), gson.toJson(distanceItem));
                    Request request = new Request.Builder().url("http://10.0.40.164:8888/add_period_distance")
                            .post(requestBody).build();
                    try {
                        okHttpClient.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                        CommonLogger.e("发送距离信息失败" + e.getMessage());
                    }
                }
            }).start();
            preLongtitude = longitude;
            preLatitude = latitude;
        }
    }

    @Override
    public void onLocationFailed(int errorId, String errorMsg) {
        CommonLogger.e("定位失败" + errorMsg + errorId);
    }


    public int getDistance(double longitude, double latitude) {
        if (preLatitude != 0 && preLongtitude != 0) {
            int distance = (int) AMapUtils.calculateLineDistance(new LatLng(preLatitude, preLongtitude), new LatLng(latitude, longitude));
            return distance;
        } else {
            return 0;
        }
    }
}
