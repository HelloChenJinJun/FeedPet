package com.example.cootek.feedpet.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.cootek.feedpet.R;

/**
 * Created by Vivian on 2017/8/27.
 */



/**
 *
 * 信息指南界面
 *
 * */
public class PetGuideDetailActivity extends AppCompatActivity {
    private int[] detailImages={R.drawable.detail_pet_clean,R.drawable.detail_pet_sport,R.drawable.detail_pet_feed
            ,R.drawable.detail_pet_health,R.drawable.detail_pet_train};
    private final static String ITEM_GUIDE="ITEM_GUIDE";
    private ImageView detailImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_pet_guide);
        detailImageView= (ImageView) findViewById(R.id.detail_image_view);
        int position=getIntent().getIntExtra(ITEM_GUIDE,0);
        detailImageView.setImageResource(detailImages[position]);
    }
}
