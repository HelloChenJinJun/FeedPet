package com.example.cootek.feedpet.adapter;

import com.example.commonlibrary.baseadapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.BaseWrappedViewHolder;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.feedpet.R;
import com.example.cootek.feedpet.ui.TimeSportRecyclerItem;

/**
 * Created by Vivian on 2017/8/26.
 */

public class TimeSportRecyclerViewAdapter extends BaseRecyclerAdapter<TimeSportRecyclerItem,BaseWrappedViewHolder>{
    @Override
    protected int getLayoutId() {
        return R.layout.item_time_sport_recycler_view;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, TimeSportRecyclerItem data) {
        CommonLogger.e("数据"+data.toString());
        holder.setImageResource(R.id.item_image,data.getResId()).setText(R.id.item_text,data.getName())
                .setOnItemClickListener();
    }
}
