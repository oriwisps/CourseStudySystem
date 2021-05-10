package com.zjj.coursestudy.vo;

import com.zjj.coursestudy.entity.Course;

public class CourseVo {

    private int courseID;
    private String courseCode;
    private String courseName;
    private String teacherName;
    private String teacherContact;
    private boolean end;
    private String state;

    public CourseVo(Course course){
        courseID = course.getID();
        courseName = course.getName();
        courseCode = course.getInvitationCode();
        teacherName = course.getTeacherName();
        teacherContact = course.getTeacherContact();
        end = course.isEnding();
        if(end){
            state = "已结束";
        }
        else {
            state = "未结束";
        }
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherContact() {
        return teacherContact;
    }

    public void setTeacherContact(String teacherContact) {
        this.teacherContact = teacherContact;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
