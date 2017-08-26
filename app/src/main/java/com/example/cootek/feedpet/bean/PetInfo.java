package com.example.cootek.feedpet.bean;

import java.util.Date;

/**
 * Created by Vivian on 2017/8/27.
 */

public class PetInfo {
    private int id;
    private String nickname;
    private String device;
    private String breed;
    private String age;
    private String raising_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRaising_time() {
        return raising_time;
    }

    public void setRaising_time(String raising_time) {
        this.raising_time = raising_time;
    }
}
