package com.example.cootek.feedpet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.EmptyLayout;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.feedpet.bean.ResultBean;
import com.example.cootek.feedpet.bean.UserBean;
import com.example.cootek.feedpet.event.BlueToothEvent;
import com.example.cootek.feedpet.mvp.BluePresenter;
import com.example.cootek.feedpet.ui.MainBaseActivity;
import com.example.cootek.feedpet.ui.TimeSportActivity;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends MainBaseActivity {


    private static final int REQUEST_CODE_USER_BEAN = 11;
    private BluetoothAdapter mBluetoothAdapter;
    private BlueToothBroadCastReceiver blueToothBroadCastReceiver;
    private BluePresenter bluePresenter;
    private Button addPetInfo;
    private TextView tvTimeTogether;
    private TextView tvSportTime;
    private ImageView upImageView;
    private RelativeLayout container;


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
        container = (RelativeLayout) findViewById(R.id.main_activity);
        addPetInfo = (Button) findViewById(R.id.add_pet_info);
        tvTimeTogether = (TextView) findViewById(R.id.tv_time_together);
        tvSportTime = (TextView) findViewById(R.id.tv_sport_time);
        upImageView = (ImageView) findViewById(R.id.main_up);
        setOnClickListener();
    }

    public void setOnClickListener() {
        addPetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PetInfoActivity.class);
                startActivityForResult(intent, REQUEST_CODE_USER_BEAN);
            }
        });
        upImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TimeSportActivity.class);
                startActivity(intent);
            }
        });
    }

    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c6666");

    @Override
    protected void initData() {
        bluePresenter = new BluePresenter(this, null);
        List<UserBean> list = MainApplication.getMainComponent().getDaoSession().getUserBeanDao().queryBuilder().list();
        if (list != null && list.size() > 0) {
            UserBean userBean = list.get(0);
//            updateView();
        }
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

        RxBusManager.getInstance().registerEvent(RefreshEvent.class,
                new Consumer<RefreshEvent>() {
                    @Override
                    public void accept(@NonNull RefreshEvent refreshEvent) throws Exception {
                        CommonLogger.e("刷新数据到啦啦");
                        updateView(refreshEvent.getTime(), refreshEvent.getDistance());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        CommonLogger.e("接受信息出错" + throwable.getMessage());
                    }
                });
        if (MainApplication.getMainComponent().getDaoSession().getUserBeanDao().queryBuilder().list().size() != 0) {
            upImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLoad();
                    bluePresenter.loadUserInfo();
                    container.post(new Runnable() {
                        @Override
                        public void run() {
                            CommonLogger.e("换");
                            container.setBackgroundResource(getRandom());
                            container.postDelayed(this, 10000);
                        }
                    });
                }
            }, 200);
        }
    }

    private int getRandom() {
        Random random = new Random();
        int position = random.nextInt(3);
        CommonLogger.e("数" + position);
        switch (position) {
            case 0:
                return R.drawable.pet_happy;
            case 1:
                return R.drawable.pet_normal;
            case 2:
                return R.drawable.pet_sad;
        }
        return R.drawable.pet_happy;
    }


    private AlertDialog progressDialog;

    private void showLoad() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog.Builder(this).setMessage("正在刷新....").create();
        }
        progressDialog.show();

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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        CommonLogger.e("这里哈哈哈");

        if (o != null) {
            ResultBean resultBean = (ResultBean) o;
            updateView(resultBean.getData().get(0).get(1), resultBean.getData().get(0).get(0));
            UserBean userBean = MainApplication.getMainComponent().getDaoSession().getUserBeanDao().queryBuilder()
                    .where(UserBeanDao.Properties.User_id.eq("1")).list().get(0);
            if (((ResultBean) o).getData().get(0).get(1) > userBean.getTime()) {
                userBean.setTime(resultBean.getData().get(0).get(1));
            }
            if (((ResultBean) o).getData().get(0).get(0) > userBean.getDistance()) {
                userBean.setDistance(resultBean.getData().get(0).get(0));
            }
            MainApplication.getMainComponent().getDaoSession().getUserBeanDao().insertOrReplace(userBean);
        }


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


    @Override
    protected void onResume() {
        super.onResume();
        bluePresenter.loadUserInfo();
    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_USER_BEAN) {
                CommonLogger.e("这里嗯嗯呃");
                updateView(data.getLongExtra("time", 0), data.getIntExtra("distance", 0));
            }
        }
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void updateView(long one, int two) {
        CommonLogger.e("这里返回啦啦");
        if (tvTimeTogether != null) {
            tvTimeTogether.setText(one + "分钟");
        }
        if (tvSportTime != null) {
            tvSportTime.setText(two + "米");
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
