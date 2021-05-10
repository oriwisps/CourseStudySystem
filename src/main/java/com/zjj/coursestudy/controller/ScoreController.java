package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.Course;
import com.zjj.coursestudy.entity.EvaluationItem;
import com.zjj.coursestudy.entity.Score;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.model.RespBean;
import com.zjj.coursestudy.service.CourseService;
import com.zjj.coursestudy.service.EvaluationItemService;
import com.zjj.coursestudy.service.ScoreService;
import com.zjj.coursestudy.service.UserService;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.AveMedVo;
import com.zjj.coursestudy.vo.CourseScoreVo;
import com.zjj.coursestudy.vo.ScoreStatisticsVo;
import com.zjj.coursestudy.vo.ScoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserService userService;

    @Autowired
    private EvaluationItemService evaluationItemService;

    @Autowired
    private CourseService courseService;

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
     * 查询学生用户的某课程成绩
     * @param courseID
     * @param token
     * @return
     */
    @PostMapping("/queryUserCourse")
    public RespBean getUserCourseScore(@RequestParam(value = "courseID") int courseID,
                                       @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("查询失败");
        List<ScoreVo> scoreVoList = new ArrayList<>();
        String studentName = JwtUtil.getUserName(token);

        User student = userService.getUserByName(studentName);
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

    /**
     * 查询某课程的所有学生成绩及排名
     * @param courseID
     * @return
     */
    @PostMapping("/stusScore")
    public RespBean getAllStuScore(@RequestParam(value = "courseID")int courseID){
        RespBean respBean = RespBean.requestError("查询失败");

        List<Score> scores;
        List<CourseScoreVo> courseScoreVos = new ArrayList<>();
        Course course = courseService.getCourseByID(courseID);
        if(course != null){
            for(User student: course.getStudents()){
                scores = scoreService.getScoresByStudentAndCourse(student, courseID);
                courseScoreVos.add(new CourseScoreVo(student, scores));
            }
            courseScoreVos.sort(Comparator.comparing(CourseScoreVo::getTotalScore).reversed());
            for(int i = 1; i <= courseScoreVos.size(); i++){
                courseScoreVos.get(i - 1).setRank(i);
            }
            respBean = RespBean.ok("查询成功", courseScoreVos);
        }
        return respBean;
    }

    /**
     * 课程成绩分析
     * @param courseID
     * @return
     */
    @PostMapping("/statistics")
    public RespBean getItemAvg(@RequestParam(value = "courseID") int courseID){
        RespBean respBean = RespBean.requestError("查询失败");
        int sum = 0;
        int median;
        int ave;
        int totalSum = 0;
        int totalMed;
        List<Score> scores;
        List<Integer> totalList = new ArrayList<>();
        ScoreStatisticsVo statistics = new ScoreStatisticsVo();
        AveMedVo aveVo = new AveMedVo("平均分");
        AveMedVo medVo = new AveMedVo("中位数");

        Course course = courseService.getCourseByID(courseID);
        if(course != null){
            if(course.getEvaluationItems() == null || course.getStudents() == null){
                return respBean;
            }
            for(EvaluationItem e: course.getEvaluationItems()){
                scores = scoreService.getScoresByEvaluationItem(e);
                sum = 0;
                for (Score s: scores){
                    sum += s.getScore();
                }
                ave = sum / scores.size();
                scores.sort(Comparator.comparing(Score::getScore));
                if(scores.size() % 2 == 1){
                    median = (int)scores.get(scores.size() / 2).getScore();
                }
                else{
                    median = (int)((scores.get(scores.size() / 2).getScore() + scores.get(scores.size() / 2 - 1).getScore()) / 2);
                }
                aveVo.getData().add(ave);
                medVo.getData().add(median);
                statistics.getLabels().add(e.getName());
            }
            for(User student: course.getStudents()){
                List<Score> stu;
                int stuSum = 0;
                stu = scoreService.getScoresByStudentAndCourse(student, courseID);
                for(Score s: stu){
                    stuSum += s.getScore() * s.getEvaluationItem().getProportion();
                }
                totalList.add(stuSum);
            }
            for(Integer i: totalList){
                totalSum += i;
            }
            totalList.sort(Comparator.comparing(Integer::intValue));
            if(totalList.size() % 2 == 1){
                totalMed = totalList.get(totalList.size() / 2);
            }
            else{
                totalMed = (totalList.get(totalList.size() / 2) + totalList.get(totalList.size() / 2 - 1)) / 2;
            }
            aveVo.getData().add(totalSum / totalList.size());
            medVo.getData().add(totalMed);
            statistics.getLabels().add("总分");
            statistics.getDatasets().add(aveVo);
            statistics.getDatasets().add(medVo);
            respBean = RespBean.ok("查询成功", statistics);
        }

        return respBean;
    }

    /**
     * 获取课程成绩分布
     * @param courseID
     * @return
     */
    @PostMapping("/scoredist")
    public RespBean getScoreDistribution(@RequestParam(value = "courseID") int courseID){
        RespBean respBean = RespBean.requestError("查询失败");

        List<Integer> totalScore = new ArrayList<>();
        List<Score> stu;
        Course course = courseService.getCourseByID(courseID);
        if(course != null){
            for(User student: course.getStudents()){
                int stuSum = 0;
                stu = scoreService.getScoresByStudentAndCourse(student, courseID);
                for(Score s: stu){
                    stuSum += s.getScore() * s.getEvaluationItem().getProportion();
                }
                totalScore.add(stuSum);
            }
            int[] distribution = {0,0,0,0,0};
            for(Integer i: totalScore){
                if(i < 60){
                    distribution[0] += 1;
                }
                else if(i < 70){
                    distribution[1] += 1;
                }
                else if(i < 80){
                    distribution[2] += 1;
                }
                else if(i < 90){
                    distribution[3] += 1;
                }
                else{
                    distribution[4] += 1;
                }
            }
            respBean = RespBean.ok("查询成功", distribution);
        }
        return respBean;
    }

}
