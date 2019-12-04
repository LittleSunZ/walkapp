package com.zxc.walk.entity;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String dealtime;
    private String dealuserid;
    private String id;
    private String imageurl;
    private String nickname;
    private int number;
    private String phone;
    private double price;
    private int status;
    private double totalfee;
    private int transactiontype;
    private String userid;
    private double yongjin;

    public void setDealtime(String dealtime) {
        this.dealtime = dealtime;
    }

    public String getDealtime() {
        return dealtime;
    }

    public void setDealuserid(String dealuserid) {
        this.dealuserid = dealuserid;
    }

    public String getDealuserid() {
        return dealuserid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }


    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }


    public void setTransactiontype(int transactiontype) {
        this.transactiontype = transactiontype;
    }

    public int getTransactiontype() {
        return transactiontype;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(double totalfee) {
        this.totalfee = totalfee;
    }

    public double getYongjin() {
        return yongjin;
    }

    public void setYongjin(double yongjin) {
        this.yongjin = yongjin;
    }
}
