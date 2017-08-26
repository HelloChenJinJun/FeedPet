package com.example.cootek.feedpet.ui;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vivian on 2017/8/26.
 */

public class TimeSportRecyclerItem {
    private int resId;
    private String name;


    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "TimeSportRecyclerItem{" +
                "resId=" + resId +
                ", name='" + name + '\'' +
                '}';
    }



}
