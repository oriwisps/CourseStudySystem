package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.Exercise;
import com.zjj.coursestudy.entity.KeyWord;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.model.RespBean;
import com.zjj.coursestudy.service.ExerciseService;
import com.zjj.coursestudy.service.KeyWordService;
import com.zjj.coursestudy.service.UserService;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.ExerciseVo;
import com.zjj.coursestudy.vo.KeyWordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/keyword")
public class KeyWordController {

    @Autowired
    private KeyWordService keyWordService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExerciseService exerciseService;

    /**
     * 查询教师已创建的关键字
     * @param token
     * @return
     */
    @GetMapping("/teacher")
    public RespBean getTeacherKeyWord(@RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("查询失败");
        String teacherName = JwtUtil.getUserName(token);
        List<KeyWordVo> keyWordVoList = new ArrayList<>();

        User teacher = userService.getUserByName(teacherName);
        if(teacher != null && teacher.getRole().equals("teacher")){
            for (KeyWord k: teacher.getKeyWords()) {
                keyWordVoList.add(new KeyWordVo(k));
            }
            respBean = RespBean.ok("查询成功", keyWordVoList);
            return respBean;
        }
        return respBean;
    }

    /**
     * 新建关键字
     * @param keyWordName 关键字名
     * @param description 关键字描述
     * @param token
     * @return
     */
    @PostMapping("/add")
    public RespBean addKeyWord(@RequestParam(value = "name") String keyWordName,
                               @RequestParam(value = "description") String description,
                               @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("新建失败");
        String teacherName = JwtUtil.getUserName(token);

        User teacher = userService.getUserByName(teacherName);
        if(teacher != null && teacher.getRole().equals("teacher")){
            KeyWord keyWord = new KeyWord();
            keyWord.setName(keyWordName);
            keyWord.setDescription(description);
            keyWord.setTeacher(teacher);
            keyWord = keyWordService.saveKeyWord(keyWord);
            if(keyWord != null){
                respBean = RespBean.ok("新建成功");
                return respBean;
            }
        }
        return respBean;
    }

    /**
     * 删除关键字
     * @param keyWordID
     * @return
     */
    @DeleteMapping("/delete")
    public RespBean deleteKeyWord(@RequestParam(value = "keyWordID") Integer keyWordID){
        RespBean respBean;
        if(keyWordService.deleteKeyWord(keyWordID)){
            respBean = RespBean.ok("删除成功");
            return respBean;
        }
        respBean = RespBean.requestError("删除失败");
        return respBean;
    }

    /**
     * 查询关键字的测验题
     * @param keyWordID 关键字ID
     * @return
     */
    @PostMapping("/exercise")
    public RespBean getKeyWordExercises(@RequestParam(value = "keyWordID") Integer keyWordID){
        RespBean respBean = RespBean.requestError("查询失败");
        List<ExerciseVo> exerciseVoList = new ArrayList<>();
        KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
        if(keyWord != null){
            for (Exercise e: keyWord.getExercises()) {
                exerciseVoList.add(new ExerciseVo(e));
            }
            respBean = RespBean.ok("查询成功",exerciseVoList);
            return respBean;
        }
        return respBean;
    }

    /**
     * 自动获取测验题
     * @param keyWord
     * @param number
     * @return
     */
    @PostMapping("/getexercise")
    public RespBean autoGetExercises(@RequestParam(value = "keyWord") String keyWord,
                                     @RequestParam(value = "number") Integer number){
        RespBean respBean = RespBean.requestError("获取失败");
        List<Exercise> exerciseList = keyWordService.autoGetExercises(keyWord, number);
        List<ExerciseVo> exerciseVoList = new ArrayList<>();
        if(!exerciseList.isEmpty()){
            if(exerciseList.size() < number){
                //此处调用爬虫
            }
            for (Exercise e: exerciseList) {
                exerciseVoList.add(new ExerciseVo(e));
            }
            if(exerciseVoList.size() < number){
                respBean = RespBean.ok("获取成功，缺少" + (number - exerciseVoList.size()) + "题", exerciseVoList);
            }
            else {
                respBean = RespBean.ok("获取成功", exerciseVoList);
            }
            return respBean;
        }
        return respBean;
    }

    /**
     * 添加测验题
     * @param keyWordID
     * @param content
     * @param answer
     * @param analysis
     * @return
     */
    @PostMapping("/addexercise")
    public RespBean addExercise(@RequestParam(value = "keyWordID") Integer keyWordID,
                                @RequestParam(value = "content") String content,
                                @RequestParam(value = "answer") String answer,
                                @RequestParam(value = "analysis") String analysis){
        RespBean respBean = RespBean.requestError("添加失败");

        KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
        if(keyWord != null){
            Exercise exercise = new Exercise();
            exercise.setContent(content);
            exercise.setAnswer(answer);
            exercise.setAnalysis(analysis);
            exercise.setUpdated(false);
            exercise = exerciseService.saveExercise(exercise);
            if(exercise != null){
                keyWord.getExercises().add(exercise);
                keyWord = keyWordService.saveKeyWord(keyWord);
                if(keyWord != null){
                    respBean = RespBean.ok("添加成功");
                    return respBean;
                }
            }
        }
        return respBean;
    }

    /**
     * 修改测验题
     * @param keyWordID
     * @param exerciseID
     * @param content
     * @param answer
     * @param analysis
     * @return
     */
    @PostMapping("/updateexercise")
    public RespBean updateExercise(@RequestParam(value = "keyWordID") Integer keyWordID,
                                   @RequestParam(value = "exerciseID") Integer exerciseID,
                                   @RequestParam(value = "content") String content,
                                   @RequestParam(value = "answer") String answer,
                                   @RequestParam(value = "analysis") String analysis){
        RespBean respBean = RespBean.requestError("修改失败");
        KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
        if(keyWord != null){
            Exercise exercise = exerciseService.getExerciseByID(exerciseID);
            if(exercise.isUpdated()){
                exercise.setContent(content);
                exercise.setAnswer(answer);
                exercise.setAnalysis(analysis);
                exerciseService.saveExercise(exercise);
            }
            else{
                Exercise updateExercise = new Exercise();
                updateExercise.setAnalysis(analysis);
                updateExercise.setAnswer(answer);
                updateExercise.setContent(content);
                updateExercise.setUpdated(true);
                exerciseService.saveExercise(updateExercise);
                keyWord.getExercises().remove(exercise);
                keyWord.getExercises().add(updateExercise);
                keyWordService.saveKeyWord(keyWord);
            }
            respBean = RespBean.ok("修改成功");
            return respBean;
        }
        return respBean;
    }

    /**
     * 删除测验题
     * @param keyWordID
     * @param exerciseID
     * @return
     */
    @DeleteMapping("/deleteexercise")
    public RespBean deleteExercise(@RequestParam(value = "keyWordID") Integer keyWordID,
                                   @RequestParam(value = "exerciseID") Integer exerciseID){
        RespBean respBean = RespBean.requestError("删除失败");
        KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
        Exercise exercise = exerciseService.getExerciseByID(exerciseID);
        if(keyWord != null){
            if(keyWord.getExercises().contains(exercise)){
                keyWord.getExercises().remove(exercise);
                keyWord = keyWordService.saveKeyWord(keyWord);
                if(exercise.isUpdated() && exercise.getKeyWords().size() == 0){
                    exerciseService.deleteExercise(exerciseID);
                }
                if(keyWord != null){
                    respBean = RespBean.ok("删除成功");
                    return respBean;
                }
            }
        }
        return respBean;
    }
}
