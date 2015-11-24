package com.justdoit.pics.bean;

import java.io.Serializable;

/**
 * Created by mengwen on 2015/10/28.
 */
public class UserInfo implements Serializable{

    /**
     * pk : 唯一标识
     * username : 用户名
     * email : 用户email
     * avatar : 头像链接
     * sex : 性别(0:女 1:男)
     * nickname: 昵称
     * country : 国家
     * province : 省
     * city : 城市
     * birthday : 生日
     * followers_count : 粉丝数
     * grade : 等级
     */

    private int pk;
    private String username;
    private String email;
    private Object avatar;
    private String sex;
    private String nickname;
    private String country;
    private String province;
    private String city;
    private Object birthday;
    private int followers_count;
    private int grade;

    public void setPk(int pk) {
        this.pk = pk;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getPk() {
        return pk;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Object getAvatar() {
        return avatar;
    }

    public String getSex() {
        return sex;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public Object getBirthday() {
        return birthday;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getGrade() {
        return grade;
    }
}
