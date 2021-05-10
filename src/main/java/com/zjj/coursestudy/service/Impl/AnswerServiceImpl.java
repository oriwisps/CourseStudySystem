package com.zjj.coursestudy.service.Impl;

import com.zjj.coursestudy.dao.AnswerDao;
import com.zjj.coursestudy.entity.Answer;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerDao answerDao;

    public Answer saveAnswer(Answer answer){
        return answerDao.save(answer);
    }

    public List<Answer> getAnswersByStudent(String studentID){
        List<Answer> answerList = answerDao.getAnswersByStudent(studentID);
        return answerList;
    }

    public Answer getStuAnswer(String studentID, int exerciseID){
        return answerDao.getStuAnswer(studentID, exerciseID);
    }
}
