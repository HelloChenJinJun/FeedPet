package com.example.cootek.feedpet.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Vivian on 2017/8/26.
 */

public class TimeSportPagerAdapter extends PagerAdapter {
    private List<View> mListView;
    public TimeSportPagerAdapter(List<View> mListView){
        this.mListView=mListView;
    }
    @Override
    public int getCount() {
        return mListView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListView.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListView.get(position), 0);//添加页卡
        return mListView.get(position);
    }
}
