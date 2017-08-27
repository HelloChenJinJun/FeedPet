package com.example.cootek.feedpet.event;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class BlueToothEvent {
    private double distance;
    private String deviceAddress;
    private String deviceName;
    public static final int CONNECTED = 0;
    public static final int DISCONNECTED = 1;
    private int status = 0;


    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
