package com.example.cootek.feedpet.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by COOTEK on 2017/8/26.
 */
@Entity
public class UserBean {


    private String device;
    @Id
    private String user_id;
    private long time;
    private String deviceName;
    private String nickname;
    private String breed;
    private int age;
    private String date;
    private int distance;
    private String name;
    private String raise_time;


    @Generated(hash = 1569143572)
    public UserBean(String device, String user_id, long time, String deviceName,
                    String nickname, String breed, int age, String date, int distance, String name,
                    String raise_time) {
        this.device = device;
        this.user_id = user_id;
        this.time = time;
        this.deviceName = deviceName;
        this.nickname = nickname;
        this.breed = breed;
        this.age = age;
        this.date = date;
        this.distance = distance;
        this.name = name;
        this.raise_time = raise_time;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
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

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRaise_time() {
        return this.raise_time;
    }

    public void setRaise_time(String raise_time) {
        this.raise_time = raise_time;
    }
}
