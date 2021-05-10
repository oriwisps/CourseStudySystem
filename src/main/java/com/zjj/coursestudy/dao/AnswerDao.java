package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.Answer;
import com.zjj.coursestudy.entity.KeyWord;
import com.zjj.coursestudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerDao extends JpaRepository<Answer, Integer> {

    @Query(value = "select * from answer a where a.student_id = ?1", nativeQuery = true)
    List<Answer> getAnswersByStudent(String studentID);

    @Query(value = "select * from answer a where a.student_id = ?1 and a.exercise_id = ?2", nativeQuery = true)
    Answer getStuAnswer(String studentID, int exerciseID);


}
