package com.zxc.walk.entity;

import java.io.Serializable;

public class Friend implements Serializable {

    private String fuserid;
    private String suserid;
    private String tuserid;
    private String userid;

    public String getFuserid() {
        return fuserid;
    }

    public void setFuserid(String fuserid) {
        this.fuserid = fuserid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSuserid() {
        return suserid;
    }

    public void setSuserid(String suserid) {
        this.suserid = suserid;
    }

    public String getTuserid() {
        return tuserid;
    }

    public void setTuserid(String tuserid) {
        this.tuserid = tuserid;
    }
}
