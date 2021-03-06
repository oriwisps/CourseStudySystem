package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.Answer;
import com.zjj.coursestudy.entity.Exercise;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.model.RespBean;
import com.zjj.coursestudy.service.AnswerService;
import com.zjj.coursestudy.service.ExerciseService;
import com.zjj.coursestudy.service.UserService;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.AnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExerciseService exerciseService;

    /**
     * 保存学生答案
     * @param answerVoList
     * @param token
     * @return
     */
    @PostMapping("/save")
    public RespBean saveAnswer(@RequestBody List<AnswerVo> answerVoList,
                               @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("保存失败");
        String studentName = JwtUtil.getUserName(token);

        User student = userService.getUserByName(studentName);
        if(student != null){
            for (AnswerVo answerVo: answerVoList) {
                Answer answer = new Answer();
                Exercise exercise = exerciseService.getExerciseByID(answerVo.getExerciseID());
                answer.setContent(answerVo.getContent());
                answer.setSubmit(answerVo.isSubmit());
                answer.setExercise(exercise);
                answer.setStudent(student);
                if(answerVo.getAnswerID() != null){
                    answer.setID(answerVo.getAnswerID());
                }
                answerService.saveAnswer(answer);
            }
            respBean = RespBean.ok("保存成功");
            return respBean;
        }
        return respBean;
    }
}
