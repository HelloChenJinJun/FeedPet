package com.example.cootek.feedpet;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by COOTEK on 2017/8/26.
 */
@Entity
public class UserBean {

    @Id
    private String device;
    private String id;
    private long time;
    private String deviceName;
    private String nickname;
    private int breed;
    private int age;
    private String date;
    private String distance;


    @Generated(hash = 1649485248)
    public UserBean(String device, String id, long time, String deviceName,
            String nickname, int breed, int age, String date, String distance) {
        this.device = device;
        this.id = id;
        this.time = time;
        this.deviceName = deviceName;
        this.nickname = nickname;
        this.breed = breed;
        this.age = age;
        this.date = date;
        this.distance = distance;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getBreed() {
        return breed;
    }

    public void setBreed(int breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
