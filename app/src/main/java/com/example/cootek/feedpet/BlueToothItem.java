package com.example.cootek.feedpet;

/**
 * Created by COOTEK on 2017/8/26.
 */

public class BlueToothItem {
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof BlueToothItem && ((BlueToothItem) obj).getAddress().equals(getAddress());
    }
}
