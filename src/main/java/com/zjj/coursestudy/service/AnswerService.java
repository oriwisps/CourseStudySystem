package com.zjj.coursestudy.service;

import com.zjj.coursestudy.entity.Answer;
import com.zjj.coursestudy.entity.User;

import java.util.List;

public interface AnswerService {

    Answer saveAnswer(Answer answer);

    List<Answer> getAnswersByStudent(String studentID);

    Answer getStuAnswer(String studentID, int exerciseID);
}
