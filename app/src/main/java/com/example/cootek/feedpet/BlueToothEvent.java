package com.example.cootek.feedpet;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class BlueToothEvent {
    private double distance;

    public BlueToothEvent(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
