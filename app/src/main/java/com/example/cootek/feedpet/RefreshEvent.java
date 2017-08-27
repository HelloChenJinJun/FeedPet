package com.example.cootek.feedpet;

/**
 * Created by COOTEK on 2017/8/27.
 */

public class RefreshEvent {
    private int status;
    public static final int REFRESH = 10;
    private int time;
    private int distance;


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public RefreshEvent() {
        status = REFRESH;
    }


    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }
}
