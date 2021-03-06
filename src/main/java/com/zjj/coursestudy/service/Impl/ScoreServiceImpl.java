package com.zjj.coursestudy.service.Impl;

import com.zjj.coursestudy.dao.ScoreDao;
import com.zjj.coursestudy.entity.Score;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreDao scoreDao;

    public Score saveScore(Score score){
        return scoreDao.save(score);
    }

    public List<Score> getScoresByStudent(User student){
        return scoreDao.getScoresByStudent(student);
    }

    public List<Score> getScoresByStudentAndCourse(User student, int courseID){
        return scoreDao.getScoresByCourseAndStudent(student.getID(), courseID);
    }

}
