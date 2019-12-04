package com.zxc.walk.entity;

import java.io.Serializable;

public class Statistical implements Serializable {
    private double highData;
    private double lowData;
    private double nowData;
    private double raiseData;
    private String readyTotalData;
    private String totalData;

    public double getHighData() {
        return highData;
    }

    public void setHighData(double highData) {
        this.highData = highData;
    }

    public double getLowData() {
        return lowData;
    }

    public void setLowData(double lowData) {
        this.lowData = lowData;
    }

    public double getNowData() {
        return nowData;
    }

    public void setNowData(double nowData) {
        this.nowData = nowData;
    }

    public double getRaiseData() {
        return raiseData;
    }

    public void setRaiseData(double raiseData) {
        this.raiseData = raiseData;
    }

    public String getReadyTotalData() {
        return readyTotalData;
    }

    public void setReadyTotalData(String readyTotalData) {
        this.readyTotalData = readyTotalData;
    }

    public String getTotalData() {
        return totalData;
    }

    public void setTotalData(String totalData) {
        this.totalData = totalData;
    }
}
