package com.zjj.coursestudy.service.Impl;

import com.zjj.coursestudy.dao.CourseDao;
import com.zjj.coursestudy.dao.UserDao;
import com.zjj.coursestudy.entity.Course;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private UserDao userDao;

    public Course getCourseByCode(String code){
        return courseDao.getCourseByInvitationCode(code);
    }

    public  Course getCourseByID(int ID){
        return courseDao.getCourseByID(ID);
    }

    public Set<Course> getStudentAllCourses(String name){
        User user = userDao.findByName(name);
        if(user.getRole().equals("student")){
            return user.getCourses();
        }
        return null;
    }

    public Set<Course> getTeacherAllCourses(String name){
        User user = userDao.findByName(name);
        if(user.getRole().equals("teacher")){
            return courseDao.getCoursesByTeacher(user);
        }
        return null;
    }

    public Set<User> getCourseAllStudent(int ID){
        return courseDao.getOne(ID).getStudents();
    }

    public Course saveCourse(Course course){
        return courseDao.save(course);
    }
}
