package com.zxc.walk.entity;

import java.io.Serializable;

public class Shop implements Serializable {

    private String id;
    private String companyid;
    private String image0;
    private String image1;
    private String image2;
    private String time;
    private float yuluprice;
    private float yulunumber;
    private String marks;
    private String stocks;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getImage0() {
        return image0;
    }

    public void setImage0(String image0) {
        this.image0 = image0;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getYuluprice() {
        return yuluprice;
    }

    public void setYuluprice(float yuluprice) {
        this.yuluprice = yuluprice;
    }

    public float getYulunumber() {
        return yulunumber;
    }

    public void setYulunumber(float yulunumber) {
        this.yulunumber = yulunumber;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
