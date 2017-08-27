package com.example.cootek.feedpet.bean;

/**
 * Created by COOTEK on 2017/8/27.
 */

public class NearItemItem {
    private String user_id;
    private String device;
    private int period;
    private String end_time;
    private int flag;


    public int getPeriod() {
        return period;
    }


    public void setPeriod(int period) {
        this.period = period;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }


    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
