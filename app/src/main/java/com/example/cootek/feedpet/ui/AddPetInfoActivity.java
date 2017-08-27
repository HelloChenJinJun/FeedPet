package com.example.cootek.feedpet.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cootek.feedpet.R;
import com.example.cootek.feedpet.bean.PetInfo;
import com.google.gson.Gson;

/**
 * Created by Vivian on 2017/8/27.
 * 添加宠物信息
 */

public class AddPetInfoActivity extends AppCompatActivity {
    private ImageView leftButton;
    private EditText petNick;
    private EditText petKind;
    private EditText petAge;
    private EditText feedDate;
    private Spinner deviceIdSpinner;
    private ImageView providePetInfo;
    private PetInfo petInfo;
    private String deviceId;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_info_provide);
        initView();
    }

    public void initView() {
        leftButton = (ImageView) findViewById(R.id.pet_info_left_button);
        petNick = (EditText) findViewById(R.id.edit_pet_nick);
        petKind = (EditText) findViewById(R.id.edit_pet_kind);
        petAge = (EditText) findViewById(R.id.edit_pet_age);
        feedDate = (EditText) findViewById(R.id.edit_feed_date);
//        deviceIdSpinner= (Spinner) findViewById(R.id.spinner_deviceid);
        providePetInfo = (ImageView) findViewById(R.id.provide_pet_info);
//        deviceIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                deviceId= (String) deviceIdSpinner.getSelectedItem();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        providePetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deviceId == null) {
                    Toast.makeText(AddPetInfoActivity.this, "信息不完整", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public String getPetInfo() {
        petInfo = new PetInfo();
        gson = new Gson();
        petInfo.setId(001);
        petInfo.setNickname(petNick.getText().toString());
        petInfo.setDevice(deviceId);
        petInfo.setBreed(petKind.getText().toString());
        petInfo.setAge(petAge.getText().toString());
        petInfo.setRaising_time(feedDate.getText().toString());
        return gson.toJson(petInfo);
    }

}
