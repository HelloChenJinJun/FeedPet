package com.example.cootek.feedpet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.feedpet.ui.AddPetInfoActivity;
import com.example.cootek.feedpet.ui.TimeSportActivity;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {


    private BluetoothAdapter mBluetoothAdapter;
    private BlueToothBroadCastReceiver blueToothBroadCastReceiver;
    private BluePresenter bluePresenter;
    private Button addPetInfo;
    private TextView tvTimeTogether;
    private TextView tvSportTime;
    private ImageView upImageView;

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
        addPetInfo= (Button) findViewById(R.id.add_pet_info);
        tvTimeTogether= (TextView) findViewById(R.id.tv_time_together);
        tvSportTime= (TextView) findViewById(R.id.tv_sport_time);
        upImageView= (ImageView) findViewById(R.id.main_up);
        setOnClickListener();
    }

    public void setOnClickListener(){
        addPetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, AddPetInfoActivity.class);
                startActivity(intent);
            }
        });
        upImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,TimeSportActivity.class);
                startActivity(intent);
            }
        });
    }

    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c6666");

    @Override
    protected void initData() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//每搜索到一个设备就会发送一个该广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//当全部搜索完后发送该广播
        filter.setPriority(Integer.MAX_VALUE);//设置优先级
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE);
// 注册蓝牙搜索广播接收者，接收并处理搜索结果
        blueToothBroadCastReceiver = new BlueToothBroadCastReceiver();
        registerReceiver(blueToothBroadCastReceiver, filter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 如果蓝牙设置使能未打开，请求打开设备
        if (!mBluetoothAdapter.isEnabled()) {
            // 打开蓝牙设备
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enable, 1);


            //使蓝牙设备可见，方便配对
            Intent in = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(in);

            findDevice();

        } else {
            //使蓝牙设备可见，方便配对
            Intent in = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(in);
            //设配器中寻找已匹配过的设备
            findDevice();
        }

        RxBusManager.getInstance().registerEvent(BlueToothEvent.class,
                new Consumer<BlueToothEvent>() {
                    @Override
                    public void accept(@NonNull BlueToothEvent blueToothEvent) throws Exception {
//                        这边通过设备id从服务器那边获取用户id
                        Toast.makeText(MainActivity.this, "接受到距离信息", Toast.LENGTH_SHORT).show();
                        CommonLogger.e("信息:" + blueToothEvent.getDistance());

                        if (blueToothEvent.getStatus() == BlueToothEvent.CONNECTED) {
                            try {
                                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(blueToothEvent.getDeviceAddress());
                                BluetoothSocket bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                                bluetoothSocket.connect();
                            } catch (IOException e) {
                                CommonLogger.e("连接失败" + e.getMessage());
                                e.printStackTrace();
                            }

                        } else if (blueToothEvent.getStatus() == BlueToothEvent.DISCONNECTED) {

                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        CommonLogger.e("接受信息出错" + throwable.getMessage());
                    }
                });

    }

    private void findDevice() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (BluetoothDevice device : pairedDevices) {
                stringBuilder.append(device.getName() + ":" + device.getAddress() + "\n");
            }
            CommonLogger.e("已有信息：" + stringBuilder.toString());
        }
    }

    @Override
    public void updateData(Object o) {

    }

//
//    @OnClick(R.id.tv_activity_main_search)
//    public void onViewClicked() {
//        CommonLogger.e("11ssss");
//        if (mBluetoothAdapter.isDiscovering()) {
//            mBluetoothAdapter.cancelDiscovery();
//        }
//        mBluetoothAdapter.startDiscovery();
//    }

    private class BlueToothBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            CommonLogger.e("接收到");
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                CommonLogger.e("设备名字" + device.getName() + "设备id" + device.getAddress());
                if (device.getAddress() != null && device.getAddress().equals("1C:48:CE:E3:CA:F7")) {
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    double distane = BlueToothUtil.getDistance(rssi);
                    String deviceName = device.getName();
                    String deviceAddress = device.getAddress();
                    BlueToothEvent blueToothEvent = new BlueToothEvent();
                    blueToothEvent.setStatus(BlueToothEvent.CONNECTED);
                    blueToothEvent.setDeviceAddress(deviceAddress);
                    blueToothEvent.setDeviceName(deviceName);
                    blueToothEvent.setDistance(distane);
                    RxBusManager.getInstance().post(blueToothEvent);
                }
            } else if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                CommonLogger.e("断开啦啦啦");
                if (device.getAddress() != null && device.getAddress().equals("1C:48:CE:E3:CA:F7")) {
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    double distane = BlueToothUtil.getDistance(rssi);
                    String deviceName = device.getName();
                    String deviceAddress = device.getAddress();
                    BlueToothEvent blueToothEvent = new BlueToothEvent();
                    blueToothEvent.setStatus(BlueToothEvent.CONNECTED);
                    blueToothEvent.setDeviceAddress(deviceAddress);
                    blueToothEvent.setDeviceName(deviceName);
                    blueToothEvent.setDistance(distane);
//                    RxBusManager.getInstance().post(blueToothEvent);
                }
            } else if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                CommonLogger.e("STATE_设备名字" + device.getName() + "设备id" + device.getAddress() + "bound" + intent.getIntExtra(BluetoothDevice
                        .EXTRA_BOND_STATE, 0));
                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
                CommonLogger.e("状态" + state);

            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (blueToothBroadCastReceiver != null) {
            unregisterReceiver(blueToothBroadCastReceiver);
        }
    }
}
