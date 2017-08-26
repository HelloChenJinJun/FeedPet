package com.example.cootek.feedpet;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class BlueEvent {


    public static final int CONNECTED = 0;
    public static final int DISCONNECTED = 1;
    private int status;


    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
