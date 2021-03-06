package com.zjj.coursestudy.service;

import com.zjj.coursestudy.entity.Score;
import com.zjj.coursestudy.entity.User;

import java.util.List;

public interface ScoreService {

    Score saveScore(Score score);

    List<Score> getScoresByStudent(User student);

    List<Score> getScoresByStudentAndCourse(User student, int courseID);
}
