package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.Answer;
import com.zjj.coursestudy.entity.Exercise;
import com.zjj.coursestudy.entity.KeyWord;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.vo.RespBean;
import com.zjj.coursestudy.service.AnswerService;
import com.zjj.coursestudy.service.ExerciseService;
import com.zjj.coursestudy.service.KeyWordService;
import com.zjj.coursestudy.service.UserService;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.StuExerciseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Autowired
    private KeyWordService keyWordService;

    /**
     * 保存或提交学生答案
     * @param token
     * @return
     */
    @PostMapping("/saveAns")
    public RespBean saveAnswer(@RequestParam(value = "content") String content,
                               @RequestParam(value = "answerID", required = false) Integer answerID,
                               @RequestParam(value = "exerciseID") int exerciseID,
                               @RequestParam(value = "submit") boolean submit,
                               @RequestParam(value = "keywordID") int keywordID,
                               @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("保存失败");
        String studentName = JwtUtil.getUserName(token);

        User student = userService.getUserByName(studentName);
        if(student != null){
            Answer answer = new Answer();
            Exercise exercise = exerciseService.getExerciseByID(exerciseID);
            KeyWord keyWord = keyWordService.getKeyWordByID(keywordID);
            answer.setContent(content);
            answer.setSubmit(submit);
            answer.setExercise(exercise);
            answer.setStudent(student);
            answer.setKeyWord(keyWord);
            if(answerID != null){
                answer.setID(answerID);
            }
            answerService.saveAnswer(answer);
            respBean = RespBean.ok("保存成功");
            return respBean;
        }
        return respBean;
    }

    /**
     * 查询学生答案
     * @param keyWordID
     * @param token
     * @return
     */
    @PostMapping("/stuexercise")
    public RespBean stuGetExercise(@RequestParam(value = "keyWordID") int keyWordID,
                                   @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("查询失败");
        String studentName = JwtUtil.getUserName(token);

        User student = userService.getUserByName(studentName);
        KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
        List<Answer> answerList;
        List<StuExerciseVo> exerciseVoList = new ArrayList<>();

        if(student != null && keyWord != null){
            answerList = answerService.getAnswersByStudent(student.getID());
            if(answerList.size() == 0){
                for(Exercise e: keyWord.getExercises()){
                    exerciseVoList.add(new StuExerciseVo(e, keyWord));
                }
            }
            else{
                answerList.sort(Comparator.comparing(Answer::getExerciseID));
            }
            for(Answer a: answerList){
                for(Exercise e: keyWord.getExercises()){
                    if(a.getExercise().getID() == e.getID()){
                        exerciseVoList.add(new StuExerciseVo(a));
                    }
                }
            }
            if(exerciseVoList.size() == 0){
                for(Exercise e: keyWord.getExercises()){
                    exerciseVoList.add(new StuExerciseVo(e, keyWord));
                }
            }
            exerciseVoList.sort(Comparator.comparing(StuExerciseVo::getExerciseID));
            respBean = RespBean.ok("查询成功", exerciseVoList);
        }
        return respBean;
    }

    @PostMapping("/teaGetAnswer")
    public RespBean teaGetAnswer(@RequestParam(value = "keyWordID") int keyWordID,
                                 @RequestParam(value = "studentID") String studentID){
        RespBean respBean = RespBean.requestError("查询失败");

        User student = userService.getUserByID(studentID);
        KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
        List<Answer> answerList;
        List<StuExerciseVo> exerciseVoList = new ArrayList<>();

        if(student != null && keyWord != null){
            answerList = answerService.getAnswersByStudent(student.getID());
            if(answerList.size() == 0){
                for(Exercise e: keyWord.getExercises()){
                    exerciseVoList.add(new StuExerciseVo(e, keyWord));
                }
            }
            else{
                answerList.sort(Comparator.comparing(Answer::getExerciseID));
            }
            for(Answer a: answerList){
                for(Exercise e: keyWord.getExercises()){
                    if(a.getExercise().getID() == e.getID()){
                        exerciseVoList.add(new StuExerciseVo(a));
                    }
                }
            }
            if(exerciseVoList.size() == 0){
                for(Exercise e: keyWord.getExercises()){
                    exerciseVoList.add(new StuExerciseVo(e, keyWord));
                }
            }
            respBean = RespBean.ok("查询成功", exerciseVoList);
        }
        return respBean;
    }
}
