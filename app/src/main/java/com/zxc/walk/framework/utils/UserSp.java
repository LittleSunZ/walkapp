package com.zxc.walk.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zxc.walk.entity.UserInfo;

public class UserSp {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public UserSp(Context context) {
        sharedPreferences = context.getSharedPreferences("walkapp", Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        editor = sharedPreferences.edit();
    }

    public void setUserInfo(UserInfo userInfo){
        editor.putString("userid", userInfo.getUserid());
        editor.putString("phone", userInfo.getPhone());
        editor.putString("headimage", userInfo.getHeadimage());
        editor.putString("nickname", userInfo.getNickname());
        editor.putString("name", userInfo.getName());
        editor.putInt("viplevel", userInfo.getViplevel());
        editor.putInt("activitylevel", userInfo.getActivitylevel());
        editor.putFloat("candynumber", userInfo.getCandynumber());
        editor.putString("password", userInfo.getPassword());
        editor.putString("bankname", userInfo.getBankname());
        editor.putString("cardnum", userInfo.getCardnum());
        editor.putString("suserid", userInfo.getSuserid());
        editor.putString("cardid", userInfo.getCardid());
        editor.putString("paypassword", userInfo.getPaypassword());
        editor.putString("appno", userInfo.getAppno());
        editor.putFloat("totalcandynumber", userInfo.getTotalcandynumber());
        editor.commit();
    }

    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserid(sharedPreferences.getString("userid",""));
        userInfo.setPhone(sharedPreferences.getString("phone",""));
        userInfo.setHeadimage(sharedPreferences.getString("headimage",""));
        userInfo.setNickname(sharedPreferences.getString("nickname",""));
        userInfo.setName(sharedPreferences.getString("name",""));
        userInfo.setViplevel(sharedPreferences.getInt("viplevel",0));
        userInfo.setActivitylevel(sharedPreferences.getInt("activitylevel",0));
        userInfo.setCandynumber(sharedPreferences.getFloat("candynumber",0));
        userInfo.setPassword(sharedPreferences.getString("password",""));
        userInfo.setBankname(sharedPreferences.getString("bankname",""));
        userInfo.setCardnum(sharedPreferences.getString("cardnum",""));
        userInfo.setSuserid(sharedPreferences.getString("suserid",""));
        userInfo.setCardid(sharedPreferences.getString("cardid",""));
        userInfo.setPaypassword(sharedPreferences.getString("paypassword",""));
        userInfo.setAppno(sharedPreferences.getString("appno",""));
        userInfo.setTotalcandynumber(sharedPreferences.getFloat("totalcandynumber",0));
        return userInfo;
    }

    public void removeUserInfo(){
        editor.clear();
        editor.commit();
    }
}
