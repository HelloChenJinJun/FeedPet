package com.example.cootek.feedpet;

import com.example.commonlibrary.baseadapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.BaseWrappedViewHolder;
import com.example.cootek.feedpet.bean.BlueToothItem;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class BlueToothSearchAdapter extends BaseRecyclerAdapter<BlueToothItem, BaseWrappedViewHolder> {
    @Override
    protected int getLayoutId() {
        return R.layout.item_activity_blue_tooth_search;
    }


    @Override
    protected void convert(BaseWrappedViewHolder holder, BlueToothItem data) {
        holder.setText(R.id.tv_item_activity_blue_tooth_search_name, data.getName())
                .setText(R.id.tv_item_activity_blue_tooth_search_address, data.getAddress())
                .setOnItemClickListener();
    }
}
