package com.zjj.coursestudy.service;

import com.zjj.coursestudy.entity.Course;
import com.zjj.coursestudy.entity.User;

import java.util.List;
import java.util.Set;

public interface CourseService {

    Course getCourseByCode(String code);

    Course getCourseByID(int ID);

    Set<Course> getStudentAllCourses(String name);

    Set<Course> getStudentCourses(String name, boolean end);

    Set<Course> getTeacherAllCourses(String name);

    Set<User> getCourseAllStudent(int ID);

    Course saveCourse(Course course);

    Set<Course> getCoursesByTeacherAndEnding(User teacher, boolean end);
}
