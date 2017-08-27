package com.example.cootek.feedpet.bean;

import java.util.List;

/**
 * Created by COOTEK on 2017/8/27.
 */

public class ResultBean {

    /**
     * status : ok
     * data : [[21,8],[21,8]]
     */

    private String status;
    private List<List<Integer>> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<List<Integer>> getData() {
        return data;
    }

    public void setData(List<List<Integer>> data) {
        this.data = data;
    }
}
