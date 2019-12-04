package com.zxc.walk.entity;

import java.io.Serializable;

public class DealTable implements Serializable {
    private double data;
    private String date;

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
