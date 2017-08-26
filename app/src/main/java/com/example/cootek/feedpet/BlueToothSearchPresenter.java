package com.example.cootek.feedpet;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.mvp.BaseFragment;
import com.example.commonlibrary.mvp.BaseModel;
import com.example.commonlibrary.mvp.BasePresenter;
import com.example.commonlibrary.mvp.IView;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class BlueToothSearchPresenter extends BasePresenter {
    public BlueToothSearchPresenter(IView iView, BaseModel baseModel) {
        super(iView, baseModel);
    }


    public void search() {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }
}
