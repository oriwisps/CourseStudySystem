package com.zjj.coursestudy.vo;

import com.zjj.coursestudy.entity.User;

public class StudentVo {

    private String studentID;
    private String studentName;
    private String phone;
    private String email;

    public StudentVo(User user){
        if(user.getRole().equals("student")){
            studentID = user.getID();
            studentName = user.getName();
            phone = user.getPhone();
            email = user.getEmail();
        }
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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
