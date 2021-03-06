package com.zjj.coursestudy.vo;

import com.zjj.coursestudy.entity.User;

public class UserInfoVo {

    private String userID;
    private String userName;
    private String role;
    private String phone;
    private String email;

    public UserInfoVo(User user){
        userID = user.getID();
        userName = user.getName();
        role = user.getRole();
        phone = user.getPhone();
        email = user.getEmail();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
