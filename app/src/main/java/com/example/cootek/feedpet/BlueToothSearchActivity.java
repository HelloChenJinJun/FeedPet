package com.example.cootek.feedpet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;

import butterknife.BindView;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class BlueToothSearchActivity extends MainBaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srcv_activity_bluetooth_search)
    SuperRecyclerView display;
    @BindView(R.id.refresh_activity_blue_tooth_search_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private BlueToothSearchAdapter adapter;
    private BlueToothSearchPresenter blueToothSearchPresenter;
    private BlueToothBroadCastReceiver receiver;
    private ProgressDialog progressDialog;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    public void updateData(Object o) {

    }

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
        return R.layout.activity_bluetooth_search;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        swipeRefreshLayout.setOnRefreshListener(this);
        blueToothSearchPresenter = new BlueToothSearchPresenter(this, null);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//每搜索到一个设备就会发送一个该广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//当全部搜索完后发送该广播
        filter.setPriority(Integer.MAX_VALUE);//设置优先级
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE);
        filter.addAction("android.bluetooth.device.action.DISAPPEARED");
// 注册蓝牙搜索广播接收者，接收并处理搜索结果
        receiver = new BlueToothBroadCastReceiver();
        registerReceiver(receiver, filter);
        adapter = new BlueToothSearchAdapter();
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                BlueToothItem item = adapter.getData(position);
                connect(item);
            }
        });
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            // 打开蓝牙设备
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enable, 1);
            //使蓝牙设备可见，方便配对
            Intent in = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(in);
        } else {
            //使蓝牙设备可见，方便配对
            Intent in = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            in.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(in);
            //设配器中寻找已匹配过的设备
        }
        display.post(new Runnable() {
            @Override
            public void run() {
                blueToothSearchPresenter.search();
            }
        });
    }

    private void connect(final BlueToothItem item) {
//        upLoadData(null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("确定该设备为你的硬件id:" + item.getAddress() + "?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        UserBean userBean = new UserBean();
                        userBean.setDevice(item.getAddress());
                        userBean.setId(getUserId(item.getAddress()));
                        MainApplication.getMainComponent().getDaoSession().getUserBeanDao().insertOrReplace(userBean);
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        Intent intent = new Intent(BlueToothSearchActivity.this, SucessActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    private String getUserId(String address) {
        return address + "uid";
    }

    @Override
    public void onRefresh() {
        adapter.clearAllData();
        adapter.notifyDataSetChanged();
        blueToothSearchPresenter.search();
        swipeRefreshLayout.setRefreshing(false);
    }

    private class BlueToothBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            CommonLogger.e("接收到" + intent.getAction());
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BlueToothItem blueToothItem = new BlueToothItem();
                blueToothItem.setAddress(device.getAddress());
                blueToothItem.setName(device.getName());
                adapter.addData(blueToothItem);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
