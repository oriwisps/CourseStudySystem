package com.zjj.coursestudy.vo;

import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.utils.UUIDUtil;

public class UserSignInVo {

    private String name;
    private String role;
    private String phone;
    private String email;
    private String password;

    public User createUser(){
        User user = new User();
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setID(UUIDUtil.createUUID());
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
