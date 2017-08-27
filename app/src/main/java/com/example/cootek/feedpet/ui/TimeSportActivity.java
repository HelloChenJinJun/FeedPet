package com.example.cootek.feedpet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.feedpet.MainApplication;
import com.example.cootek.feedpet.R;
import com.example.cootek.feedpet.RefreshEvent;
import com.example.cootek.feedpet.bean.UserBean;
import com.example.cootek.feedpet.UserBeanDao;
import com.example.cootek.feedpet.adapter.TimeSportPagerAdapter;
import com.example.cootek.feedpet.adapter.TimeSportRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Vivian on 2017/8/26.
 * 指南列表和数据指数展示界面
 */

public class TimeSportActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TextView timeTextView, sportTextView;
    private List<View> viewList;
    private SuperRecyclerView contentRecycler;
    private TimeSportRecyclerViewAdapter adapter;
    private List<TimeSportRecyclerItem> itemList;
    private TimeSportRecyclerItem recyclerItem;
    private int[] imageRes = {R.drawable.item_clean, R.drawable.walk_item, R.drawable.feed_item
            , R.drawable.health_item, R.drawable.train_item};
    private String[] textRes = {"清洁指南", "运动指南", "喂食指南", "健康指南", "训练指南"};
    private final static String ITEM_GUIDE = "ITEM_GUIDE";
    private TextView one;
    private TextView two;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initView();
        TimeSportPagerAdapter adapter = new TimeSportPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
    }

    public void initView() {
        viewList = new ArrayList<>();
        itemList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            recyclerItem = new TimeSportRecyclerItem();
            recyclerItem.setResId(imageRes[i]);
            recyclerItem.setName(textRes[i]);
            itemList.add(recyclerItem);
        }
        contentRecycler = (SuperRecyclerView) findViewById(R.id.content_recyclerview);
        contentRecycler.setLayoutManager(new WrappedLinearLayoutManager(this));
        contentRecycler.setAdapter(adapter = new TimeSportRecyclerViewAdapter());
        adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent();
                intent.putExtra(ITEM_GUIDE, position);
                intent.setClass(TimeSportActivity.this, PetGuideDetailActivity.class);
                startActivity(intent);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.content_viewpager);
        timeTextView = (TextView) findViewById(R.id.time_text_view);
        sportTextView = (TextView) findViewById(R.id.sport_text_view);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.viewpager_time, null);
        View view1 = inflater.inflate(R.layout.viewpager_sport, null);
        one = (TextView) view.findViewById(R.id.time_text_view);
        two = (TextView) view1.findViewById(R.id.time_text_view);
        List<UserBean> list = MainApplication.getMainComponent().getDaoSession().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.User_id.eq("1")).list();
        if (list != null && list.size() > 0) {
            UserBean userBean = list.get(0);
            refreshView((int) userBean.getTime(), userBean.getDistance());
        }
        RxBusManager.getInstance().registerEvent(RefreshEvent.class, new Consumer<RefreshEvent>() {
            @Override
            public void accept(@NonNull RefreshEvent refreshEvent) throws Exception {
                CommonLogger.e("刷新数据到啦啦" + refreshEvent.getDistance() + " time" + refreshEvent.getTime());
                refreshView(refreshEvent.getTime(), refreshEvent.getDistance());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        });
        viewList.add(view);
        viewList.add(view1);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                adapter.addData(itemList);
            }
        });
    }


    public void refreshView(int time, int distance) {
        if (one != null) {
            one.setText(time + "分");
        }
        if (two != null) {
            two.setText(distance + "米");
        }
    }
}
