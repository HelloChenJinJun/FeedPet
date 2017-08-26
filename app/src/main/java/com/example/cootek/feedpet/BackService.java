package com.example.cootek.feedpet;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.example.commonlibrary.utils.CommonLogger;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class BackService extends Service {
    private int time;
    private Handler mHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            time = intent.getIntExtra("time", 6);
        } else {
            time = 5;
        }
        CommonLogger.e("onStartTime" + time);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        CommonLogger.e("这里处理拉克");
                        dealWork();
                        sendEmptyMessageDelayed(0, time * 1000);
                    }
                };
                mHandler.sendEmptyMessage(0);
                Looper.loop();
            }
        }).start();
        return super.onStartCommand(intent, START_FLAG_RETRY, startId);
    }

    private void dealWork() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }
}
