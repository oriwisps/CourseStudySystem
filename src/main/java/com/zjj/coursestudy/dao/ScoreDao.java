package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.Course;
import com.zjj.coursestudy.entity.EvaluationItem;
import com.zjj.coursestudy.entity.Score;
import com.zjj.coursestudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreDao extends JpaRepository<Score, Integer> {

    List<Score> getScoresByStudent(User student);

    @Query(value = "select * from score s " +
            "where s.student_id = ?1 and s.evaluation_item_id in " +
            "(select id from evaluation_item ei where ei.course_id = ?2 order by ei.course_id)", nativeQuery = true)
    List<Score> getScoresByCourseAndStudent(String studentID, int courseID);

    List<Score> getScoresByEvaluationItem(EvaluationItem e);
}
