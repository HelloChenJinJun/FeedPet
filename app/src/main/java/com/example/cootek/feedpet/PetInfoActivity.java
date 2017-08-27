package com.example.cootek.feedpet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.mvp.BaseActivity;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.feedpet.bean.UserBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by COOTEK on 2017/8/27.
 * 宠物信息注册界面
 */

public class PetInfoActivity extends BaseActivity {


    private static final int REQUEST_CODE_BLUE_TOOTH = 10;
    @BindView(R.id.et_activity_pet_info_nick)
    EditText nick;
    @BindView(R.id.et_activity_pet_info_type)
    EditText tpye;
    @BindView(R.id.et_activity_pet_info_age)
    EditText age;
    @BindView(R.id.tv_activity_pet_info_date)
    TextView time;
    @BindView(R.id.tv_activity_pet_info_search)
    TextView search;
    @BindView(R.id.btn_activity_pet_info)
    Button add;
    private int currentDay = 4;
    private int currentMonth = 9;
    private int currentYear = 1995;
    private UserBean userBean;
    private AlertDialog progressDialog;
    private boolean hasSuccess = false;


    @Override
    public void updateData(Object o) {

    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pet_info;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        List<UserBean> list = MainApplication.getMainComponent().getDaoSession().getUserBeanDao().queryBuilder().list();
        if (list != null && list.size() > 0) {
            userBean = list.get(0);
        } else {
            userBean = new UserBean();
        }
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("宠物信息");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }


    @OnClick({R.id.tv_activity_pet_info_date, R.id.tv_activity_pet_info_search, R.id.btn_activity_pet_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_activity_pet_info_date:
                openDatePicker();
                break;
            case R.id.tv_activity_pet_info_search:
                Intent intent = new Intent(this, BlueToothSearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_BLUE_TOOTH);
                break;
            case R.id.btn_activity_pet_info:
                if (TextUtils.isEmpty(nick.getText().toString()) ||
                        TextUtils.isEmpty(age.getText().toString()) ||
                        TextUtils.isEmpty(tpye.getText().toString()) || search.isClickable()
                        || time.getText().toString().equals("点击添加")) {
                    ToastUtils.showShortToast("内容不能为空");
                } else {
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog.Builder(this).setMessage("正在上传....").create();
                    }
                    progressDialog.show();
                    try {
                        userBean.setAge(Integer.parseInt(age.getText().toString().trim()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        userBean.setAge(0);
                    }
                    userBean.setNickname(nick.getText().toString().trim());
                    userBean.setBreed(tpye.getText().toString().trim());
                    new AsyncTask<UserBean, Void, UserBean>() {
                        @Override
                        protected UserBean doInBackground(UserBean... params) {
                            UserBean bean = params[0];
                            OkHttpClient okHttpClient = MainApplication.getAppComponent().getOkHttpClient();
                            Gson gson = new Gson();
                            RequestBody requestBody = RequestBody.create(MediaType.parse("text"), gson.toJson(bean));
                            Request request = new Request.Builder().url("http://10.0.40.164:8888/add_pet").post(requestBody)
                                    .build();
                            try {
                                okHttpClient.newCall(request).execute();
                                return new UserBean();
                            } catch (IOException e) {
                                e.printStackTrace();
                                CommonLogger.e("请求出错啦啦");
                            }
                            return null;
                        }


                        @Override
                        protected void onPostExecute(UserBean userBean) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                if (userBean != null) {
                                    CommonLogger.e("成功啦啦");
                                    hasSuccess = true;
                                    Intent intent = new Intent();
                                    intent.putExtra("time", userBean.getTime());
                                    intent.putExtra("distance", userBean.getDistance());
                                    setResult(RESULT_OK, intent);
                                    MainApplication.getMainComponent().getDaoSession().getUserBeanDao().insertOrReplace(PetInfoActivity.this.userBean);
                                } else {
                                    hasSuccess = false;
                                    CommonLogger.e("出错啦啦");
                                }
                            }
                        }
                    }.execute(userBean);
                }
                BlueToothBroadCastReceiver.setAuto(true);
                BluetoothAdapter.getDefaultAdapter().startDiscovery();
                break;
        }
    }

    private void openDatePicker() {

        CustomDatePickerDialog dialog = new CustomDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentYear = year;
                currentMonth = month;
                currentDay = dayOfMonth;
                updateUserInfo();
            }
        }, currentYear, currentMonth, currentDay);
        dialog.show();
    }

    private void updateUserInfo() {
        String time = currentYear + "-" + currentMonth + "-" + currentDay;
        this.time.setText(time);
        userBean.setRaise_time(time);
    }


    @Override
    public void finish() {
        super.finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BLUE_TOOTH) {
            if (resultCode == RESULT_OK) {
                String deviceId = data.getStringExtra("deviceId");
                String deviceName = data.getStringExtra("deviceName");
                if (userBean.getUser_id() == null) {
                    userBean.setUser_id("1");
                }
                userBean.setName("123");
                userBean.setDevice(deviceId);
                userBean.setDeviceName(deviceName);
                search.setClickable(false);
                search.setText(deviceId);
                search.setEnabled(false);
            }
        }
    }
}
