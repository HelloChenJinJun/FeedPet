package com.example.cootek.feedpet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.net.NetManager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by COOTEK on 2017/8/26.
 */

public abstract class MainBaseActivity extends BaseActivity {
    private SharedPreferences sharedPreferences;
    private boolean hasStart = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("time", Context.MODE_PRIVATE);
        RxBusManager.getInstance().registerEvent(BlueEvent.class, new Consumer<BlueEvent>() {
            @Override
            public void accept(@NonNull BlueEvent blueEvent) throws Exception {
                if (blueEvent.getStatus() == BlueEvent.DISCONNECTED) {
                    if (hasStart) {
                        long endTime = System.currentTimeMillis();
                        long startTime = sharedPreferences.getLong("startTime", 0);
                        sharedPreferences.edit().putBoolean("isNear", false).apply();
                        CommonLogger.e("结束计时" + startTime);
                        hasStart = false;
                        int totalTime = (int) ((endTime - startTime) / 60000);
                        CommonLogger.e("结束计时,总共" + totalTime + "分钟");
                        List<UserBean> list = MainApplication.getMainComponent().getDaoSession().getUserBeanDao().queryBuilder().list();
                        if (list != null && list.size() > 0) {
                            UserBean userBean = list.get(0);
                            userBean.setTime(userBean.getTime() + totalTime);
                            MainApplication.getMainComponent().getDaoSession().insertOrReplace(userBean);
                        }
                        upLoadData(totalTime);
                    }
                } else {
                    if (!hasStart) {
                        CommonLogger.e("开始计时");
                        hasStart = true;
                        long startTime = System.currentTimeMillis();
                        sharedPreferences.edit().putLong("startTime", startTime).putBoolean("isNear", true).apply();
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        });
    }

    protected void upLoadData(final int totalTime) {
//        CommonLogger.e("这里上传的时间为多少" + userBean.getTime() + "  分");
        new Thread(new Runnable() {
            @Override
            public void run() {
                NearItemItem nearItemItem = new NearItemItem();
                UserBean bean = MainApplication.getMainComponent().getDaoSession().getUserBeanDao().queryBuilder().list().get(0);
                nearItemItem.setDevice(bean.getDevice());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                nearItemItem.setEnd_time(simpleDateFormat.format(new Date(System.currentTimeMillis())));
                nearItemItem.setRaise_time(totalTime);
                nearItemItem.setUser_id(bean.getId());
                nearItemItem.setFlag(1);
                OkHttpClient okHttpClient = MainApplication.getAppComponent().getOkHttpClient();
                Gson gson = new Gson();
                RequestBody requestBody = RequestBody.create(MediaType.parse("text"), gson.toJson(nearItemItem));
                Request request = new Request.Builder().url("http://10.0.40.164:8888/add_period_distance").post(requestBody).build();
                try {
                    okHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    CommonLogger.e("出错啦啦" + e.getMessage());
                }
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
