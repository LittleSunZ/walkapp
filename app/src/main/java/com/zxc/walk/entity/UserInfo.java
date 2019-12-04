package com.zxc.walk.entity;

import android.text.TextUtils;

import com.zxc.walk.framework.utils.PhoneInfoTools;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private static final long serialVersionUID = -4685699746076946202L;
    private String userid;
    private String phone;
    private String headimage;
    private String nickname;
    private String name;
    private int viplevel;
    private int activitylevel;
    private float candynumber;
    private String password;
    private String bankname;
    private String cardnum;
    private String suserid;
    private String cardid;
    private String paypassword;
    private String appno;
    private float totalcandynumber;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getNickname() {
        if (TextUtils.isEmpty(nickname)) {
            return PhoneInfoTools.phoneNumerParserFourStar(phone);
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getViplevel() {
        return viplevel;
    }

    public void setViplevel(int viplevel) {
        this.viplevel = viplevel;
    }


    public int getActivitylevel() {
        return activitylevel;
    }

    public void setActivitylevel(int activitylevel) {
        this.activitylevel = activitylevel;
    }

    public float getCandynumber() {
        return candynumber;
    }

    public void setCandynumber(float candynumber) {
        this.candynumber = candynumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getSuserid() {
        return suserid;
    }

    public void setSuserid(String suserid) {
        this.suserid = suserid;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getPaypassword() {
        return paypassword;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public float getTotalcandynumber() {
        return totalcandynumber;
    }

    public void setTotalcandynumber(float totalcandynumber) {
        this.totalcandynumber = totalcandynumber;
    }

    public String getAppno() {
        return appno;
    }

    public void setAppno(String appno) {
        this.appno = appno;
    }

}
