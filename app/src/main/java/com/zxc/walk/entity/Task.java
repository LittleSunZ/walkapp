package com.zxc.walk.entity;

import java.io.Serializable;

public class Task implements Serializable {

    private String id;
    private int showlever;
    private String days;
    private String yulunumber;
    private String sendyulus;
    private String runingnumbers;
    private String status;
    private String time;
    private String isdeafault;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getShowlever() {
        return showlever;
    }

    public void setShowlever(int showlever) {
        this.showlever = showlever;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getYulunumber() {
        return yulunumber;
    }

    public void setYulunumber(String yulunumber) {
        this.yulunumber = yulunumber;
    }

    public String getSendyulus() {
        return sendyulus;
    }

    public void setSendyulus(String sendyulus) {
        this.sendyulus = sendyulus;
    }

    public String getRuningnumbers() {
        return runingnumbers;
    }

    public void setRuningnumbers(String runingnumbers) {
        this.runingnumbers = runingnumbers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsdeafault() {
        return isdeafault;
    }

    public void setIsdeafault(String isdeafault) {
        this.isdeafault = isdeafault;
    }
}
