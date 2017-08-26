package com.example.cootek.feedpet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.WrappedLinearLayoutManager;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.cootek.feedpet.R;
import com.example.cootek.feedpet.adapter.TimeSportPagerAdapter;
import com.example.cootek.feedpet.adapter.TimeSportRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vivian on 2017/8/26.
 */

public class TimeSportActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TextView timeTextView,sportTextView;
    private List<View> viewList;
    private SuperRecyclerView contentRecycler;
    private TimeSportRecyclerViewAdapter adapter;
    private List<TimeSportRecyclerItem> itemList;
    private TimeSportRecyclerItem recyclerItem;
    private int[] imageRes={R.drawable.item_clean,R.drawable.walk_item,R.drawable.feed_item
            ,R.drawable.health_item,R.drawable.train_item};
    private String[] textRes={"清洁指南","运动指南","喂食指南","健康指南","训练指南"};
    private final static String ITEM_GUIDE="ITEM_GUIDE";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initView();
        TimeSportPagerAdapter adapter=new TimeSportPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
    }

    public void initView(){
        viewList=new ArrayList<>();
        itemList=new ArrayList<>();
        for (int i=0;i<5;i++){
            recyclerItem=new TimeSportRecyclerItem();
            recyclerItem.setResId(imageRes[i]);
            recyclerItem.setName(textRes[i]);
            itemList.add(recyclerItem);
        }
        contentRecycler= (SuperRecyclerView) findViewById(R.id.content_recyclerview);
        contentRecycler.setLayoutManager(new WrappedLinearLayoutManager(this));
        contentRecycler.setAdapter(adapter=new TimeSportRecyclerViewAdapter());
        adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent=new Intent();
                intent.putExtra(ITEM_GUIDE,position);
                intent.setClass(TimeSportActivity.this,PetGuideDetailActivity.class);
                startActivity(intent);
            }
        });

        viewPager= (ViewPager) findViewById(R.id.content_viewpager);
        timeTextView= (TextView) findViewById(R.id.time_text_view);
        sportTextView= (TextView) findViewById(R.id.sport_text_view);
        LayoutInflater inflater=getLayoutInflater();
        viewList.add(inflater.inflate(R.layout.viewpager_time,null));
        viewList.add(inflater.inflate(R.layout.viewpager_sport,null));
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                adapter.addData(itemList);
            }
        });
    }
}
