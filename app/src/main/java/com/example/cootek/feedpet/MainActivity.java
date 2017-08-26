package com.example.cootek.feedpet;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {


    @BindView(R.id.tv_activity_main_search)
    TextView search;
    private BluetoothAdapter mBluetoothAdapter;
    private BlueToothBroadCastReceiver blueToothBroadCastReceiver;
    private BluePresenter bluePresenter;

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        RxBusManager.getInstance().registerEvent(BlueToothEvent.class,
                new Consumer<BlueToothEvent>() {
                    @Override
                    public void accept(@NonNull BlueToothEvent blueToothEvent) throws Exception {

                        Toast.makeText(MainActivity.this, "接受到距离信息", Toast.LENGTH_SHORT).show();
                        CommonLogger.e("信息:" + blueToothEvent.getDistance());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        CommonLogger.e("接受信息出错" + throwable.getMessage());
                    }
                });

    }

    @Override
    public void updateData(Object o) {

    }


    @OnClick(R.id.tv_activity_main_search)
    public void onViewClicked() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }
}
