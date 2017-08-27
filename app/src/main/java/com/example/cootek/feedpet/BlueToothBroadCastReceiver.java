package com.example.cootek.feedpet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.feedpet.bean.BlueToothItem;
import com.example.cootek.feedpet.bean.UserBean;
import com.example.cootek.feedpet.event.BlueEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class BlueToothBroadCastReceiver extends BroadcastReceiver {
    private static List<BlueToothItem> list = new ArrayList<>();
    private static boolean isAuto = false;
    private static String pre;

    public static void setAuto(boolean auto) {
        isAuto = auto;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isAuto) {
            return;
        }
        CommonLogger.e("action" + intent.getAction());
        CommonLogger.e("pre" + pre);
        if (intent.getAction().equals("android.bluetooth.adapter.action.DISCOVERY_FINISHED")) {
            if (pre != null && pre.equals(BluetoothDevice.ACTION_FOUND)) {
                BlueToothItem blueToothItem = new BlueToothItem();
                List<UserBean> list = MainApplication.getMainComponent().getDaoSession().getUserBeanDao().queryBuilder().list();
                if (list != null && list.size() == 0) {
                    return;
                }
                UserBean userBean = list.get(0);
                blueToothItem.setName(userBean.getDeviceName());
                blueToothItem.setAddress(userBean.getDevice());
                BlueEvent blueEvent = new BlueEvent();
                if (list.contains(blueToothItem)) {
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    double distane = BlueToothUtil.getDistance(rssi);
                    if (distane < 15) {
                        blueEvent.setStatus(BlueEvent.DISCONNECTED);
                    } else {
                        blueEvent.setStatus(BlueEvent.CONNECTED);
                    }
                } else {
                    blueEvent.setStatus(BlueEvent.DISCONNECTED);
                }
                list.clear();
                RxBusManager.getInstance().post(blueEvent);
                BluetoothAdapter.getDefaultAdapter().startDiscovery();
                CommonLogger.e("又开始扫描");
            }
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
            CommonLogger.e("这里");
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            BlueToothItem item = new BlueToothItem();
            item.setName(device.getName());
            item.setAddress(device.getAddress());
            list.add(item);
        }
        pre = intent.getAction();
    }
}
