package com.example.cootek.feedpet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.commonlibrary.rxbus.RxBusManager;

/**
 * Created by COOTEK on 2017/8/26.
 */

class BlueToothBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
            double distane = BlueToothUtil.getDistance(rssi);
            BlueToothEvent blueToothEvent = new BlueToothEvent(distane);
            RxBusManager.getInstance().post(blueToothEvent);
        }
    }
}
