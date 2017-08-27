package com.example.cootek.feedpet.mvp;

import android.os.AsyncTask;

import com.example.commonlibrary.mvp.BaseModel;
import com.example.commonlibrary.mvp.IView;
import com.example.commonlibrary.mvp.RxBasePresenter;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.feedpet.bean.GetPetInfo;
import com.example.cootek.feedpet.MainApplication;
import com.example.cootek.feedpet.bean.ResultBean;
import com.example.cootek.feedpet.bean.UserBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class BluePresenter extends RxBasePresenter {
    public BluePresenter(IView iView, BaseModel baseModel) {
        super(iView, baseModel);
    }


    public void loadUserInfo() {
        List<UserBean> list = MainApplication.getMainComponent().getDaoSession().getUserBeanDao().queryBuilder().list();
        UserBean userBean = new UserBean();
        if (list != null && list.size() > 0) {
            userBean = list.get(0);
        }
        new AsyncTask<UserBean, Void, Response>() {
            @Override
            protected Response doInBackground(UserBean... params) {
                UserBean bean = params[0];
                OkHttpClient okHttpClient = MainApplication.getAppComponent().getOkHttpClient();
                Gson gson = new Gson();
                GetPetInfo getPetInfo = new GetPetInfo();
                getPetInfo.setDevice(bean.getDevice());
                getPetInfo.setUser_id(bean.getUser_id());
                RequestBody requestBody = RequestBody.create(MediaType.parse("text"), gson.toJson(getPetInfo));
                Request request = new Request.Builder().url("http://10.0.40.164:8888/query_period_distance").post(requestBody)
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    return response;
                } catch (IOException e) {
                    e.printStackTrace();
                    CommonLogger.e("请求出错啦啦");
                }
                return null;
            }


            @Override
            protected void onPostExecute(Response userBean) {
                if (userBean != null) {
                    ResultBean resultBean = null;
                    try {
                        CommonLogger.e("成功啦啦");
                        Gson gson = new Gson();
                        resultBean = gson.fromJson(userBean.body().string(), ResultBean.class);
                        if (resultBean == null) {
                            CommonLogger.e("为空");
                        }
                    } catch (IOException | IllegalStateException exception) {
                        exception.printStackTrace();
                        CommonLogger.e("解析出错啦" + exception.getMessage());
                    }
                    iView.updateData(resultBean);
                } else {
                    CommonLogger.e("出错啦啦");
                    iView.showError("出错啦", null);
                }
            }
        }.execute(userBean);
    }


}
