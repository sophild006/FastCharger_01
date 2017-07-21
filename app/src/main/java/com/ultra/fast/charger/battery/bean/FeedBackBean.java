package com.ultra.fast.charger.battery.bean;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    : mist
 * Author     : scotch
 * Date       : 2017/4/21 12:48
 */

public class FeedBackBean {
    public String device_id;   //mac
    public String content;     //反馈内容
    public String pack_name; //app
    public int pack_ver;  //app版本
    public String model;   //机型
    public String os;      //系统
    public String os_ver;   //系统版本
    public String language;    //系统语言
    public String email;    //email
    public String issue; //issue

    @Override
    public String toString() {
        return "FeedBackBean{" +
                "device_id='" + device_id + '\'' +
                ", content='" + content + '\'' +
                ", pack_name='" + pack_name + '\'' +
                ", pack_ver=" + pack_ver +
                ", model='" + model + '\'' +
                ", os='" + os + '\'' +
                ", os_ver='" + os_ver + '\'' +
                ", language='" + language + '\'' +
                ", email='" + email + '\'' +
                ", issue='" + issue + '\'' +
                '}';
    }
}
