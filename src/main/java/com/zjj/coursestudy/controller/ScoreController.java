package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.EvaluationItem;
import com.zjj.coursestudy.entity.Score;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.model.RespBean;
import com.zjj.coursestudy.service.EvaluationItemService;
import com.zjj.coursestudy.service.ScoreService;
import com.zjj.coursestudy.service.UserService;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.ScoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserService userService;

    @Autowired
    private EvaluationItemService evaluationItemService;

    /**
     * 查询一名学生的全部成绩
     * @param token
     * @return
     */
    @GetMapping("/queryAll")
    public RespBean getStudentAllScore(@RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("查询失败");
        List<ScoreVo> scoreVoList = new ArrayList<>();

        String studentName = JwtUtil.getUserName(token);
        User student = userService.getUserByName(studentName);

        if(student != null){
            List<Score> scoreList = scoreService.getScoresByStudent(student);
            for (Score s: scoreList) {
                scoreVoList.add(new ScoreVo(s));
            }
            respBean = RespBean.ok("查询成功", scoreVoList);
            return respBean;
        }
        return respBean;
    }

    /**
     * 查询学生的某课程的成绩
     * @param studentID
     * @param courseID
     * @return
     */
    @PostMapping("/queryCourse")
    public RespBean getStudentCourseScore(@RequestParam(value = "studentID") String studentID,
                                          @RequestParam(value = "courseID") Integer courseID){
        RespBean respBean = RespBean.requestError("查询失败");
        List<ScoreVo> scoreVoList = new ArrayList<>();

        User student = userService.getUserByID(studentID);
        if(student != null){
            List<Score> scoreList = scoreService.getScoresByStudentAndCourse(student, courseID);
            for (Score s: scoreList) {
                scoreVoList.add(new ScoreVo(s));
            }
            respBean = RespBean.ok("查询成功", scoreVoList);
            return respBean;
        }
        return respBean;
    }

    /**
     * 保存学生成绩
     * @param studentID
     * @param itemID
     * @param score
     * @param scoreID
     * @return
     */
    @PostMapping("/save")
    public RespBean saveScore(@RequestParam(value = "studentID") String studentID,
                              @RequestParam(value = "itemID") Integer itemID,
                              @RequestParam(value = "score") Double score,
                              @RequestParam(value = "scoreID", required = false, defaultValue = "") Integer scoreID){
        RespBean respBean = RespBean.requestError("保存失败");
        User student = userService.getUserByID(studentID);
        EvaluationItem evaluationItem = evaluationItemService.getItemByID(itemID);
        if(student != null && evaluationItem != null){
            Score s = new Score();
            s.setStudent(student);
            s.setEvaluationItem(evaluationItem);
            s.setScore(score);
            if(scoreID != null){
                s.setID(scoreID);
            }
            s = scoreService.saveScore(s);
            if(s != null){
                respBean = RespBean.ok("保存成功");
                return respBean;
            }
        }
        return respBean;
    }
}
