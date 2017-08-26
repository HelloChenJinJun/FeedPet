package com.example.cootek.feedpet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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

import java.util.Set;

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

    @Override
    protected void initData() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//每搜索到一个设备就会发送一个该广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//当全部搜索完后发送该广播
        filter.setPriority(Integer.MAX_VALUE);//设置优先级
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
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
//            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                double distane = BlueToothUtil.getDistance(rssi);
                BlueToothEvent blueToothEvent = new BlueToothEvent(distane);
                RxBusManager.getInstance().post(blueToothEvent);
        }
        }
    }
}
