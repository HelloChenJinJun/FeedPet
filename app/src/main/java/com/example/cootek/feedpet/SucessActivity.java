package com.example.cootek.feedpet;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class SucessActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        BlueToothBroadCastReceiver.setAuto(true);
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
//        Intent intent = new Intent(this, BackService.class);
//        intent.putExtra("time", 10);
//        startService(intent);
    }
}
