package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.Course;
import com.zjj.coursestudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CourseDao extends JpaRepository<Course, Integer> {

    Course getCourseByInvitationCode(String code);

    Course getCourseByID(int ID);

    Set<Course> getCoursesByTeacher(User teacher);

    @Query(value = "select c from Course c where c.teacher = ?1 and c.isEnding = ?2")
    Set<Course> getCoursesByTeacherAndEnding(User teacher, boolean ending);
}
