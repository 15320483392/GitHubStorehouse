package com.wt.talentpool.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/28.
 */
public class TalentPool {
    private int id;
    private String name;
    private String sex;
    private int age;
    private String peoples;
    private String phone;
    private String address;
    private Date interview_time;
    private int black_list;
    private String remark;
    private Date edit_time;

    public TalentPool() {
        super();
    }
    public TalentPool(int id, String name, String sex, int age, String peoples, String phone, String address, Date interview_time, int black_list, String remark, Date edit_time) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.peoples = peoples;
        this.phone = phone;
        this.address = address;
        this.interview_time = interview_time;
        this.black_list = black_list;
        this.remark = remark;
        this.edit_time = edit_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPeoples() {
        return peoples;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getInterview_time() {
        return interview_time;
    }

    public void setInterview_time(Date interview_time) {
        this.interview_time = interview_time;
    }

    public int getBlack_list() {
        return black_list;
    }

    public void setBlack_list(int black_list) {
        this.black_list = black_list;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(Date edit_time) {
        this.edit_time = edit_time;
    }

    @Override
    public String toString() {
        return "TalentPool:{" +
                "'id':" + id +
                ",'name':" + name +
                ",'sex':" + sex +
                ",'age':" + age +
                ",'peoples':" + peoples +
                ",'phone':" + phone +
                ",'address':" + address +
                ",'interview_time':" + interview_time +
                ",'black_list':" + black_list +
                ",'remark':" + remark +
                ",'edit_time':" + edit_time +
                '}';
    }
}
