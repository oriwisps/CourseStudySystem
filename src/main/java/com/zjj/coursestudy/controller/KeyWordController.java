package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.*;
import com.zjj.coursestudy.model.RespBean;
import com.zjj.coursestudy.service.*;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.ExerciseVo;
import com.zjj.coursestudy.vo.KeyWordVo;
import com.zjj.coursestudy.vo.StudentVo;
import com.zjj.coursestudy.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/keyword")
public class KeyWordController {

    @Autowired
    private KeyWordService keyWordService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AnswerService answerService;

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
            List<KeyWord> keyWordList = new ArrayList<>(teacher.getKeyWords());
            keyWordList.sort(Comparator.comparing(KeyWord::getID));
            for (KeyWord k: keyWordList) {
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
        Map<String, Integer> map = new HashMap<>();
        String teacherName = JwtUtil.getUserName(token);

        User teacher = userService.getUserByName(teacherName);
        if(teacher != null && teacher.getRole().equals("teacher")){
            KeyWord keyWord = new KeyWord();
            keyWord.setName(keyWordName);
            keyWord.setDescription(description);
            keyWord.setTeacher(teacher);
            keyWord = keyWordService.saveKeyWord(keyWord);
            map.put("keyID", keyWord.getID());
            if(keyWord != null){
                respBean = RespBean.ok("新建成功", map);
                return respBean;
            }
        }
        return respBean;
    }

    /**
     * 关键字信息修改
     * @param keyWordName
     * @param description
     * @param keywordID
     * @return
     */
    @PostMapping("/update")
    public RespBean updateKeyword(@RequestParam(value = "name") String keyWordName,
                                  @RequestParam(value = "description") String description,
                                  @RequestParam(value = "keyWordID") int keywordID){
        RespBean respBean = RespBean.requestError("修改失败");

        KeyWord keyWord = keyWordService.getKeyWordByID(keywordID);
        if(keyWord != null){
            keyWord.setName(keyWordName);
            keyWord.setDescription(description);
            keyWord = keyWordService.saveKeyWord(keyWord);
            if(keyWord != null){
                respBean = RespBean.ok("修改成功");
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
    @PostMapping("/delete")
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
            List<Exercise> exerciseList = new ArrayList<>(keyWord.getExercises());
            exerciseList.sort(Comparator.comparing(Exercise::getID));
            for (Exercise e: exerciseList) {
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
                                     @RequestParam(value = "number") Integer number,
                                     @RequestParam(value = "keyWordID")int keyWordID){
        RespBean respBean = RespBean.requestError("获取失败");
        List<Exercise> exerciseList = keyWordService.autoGetExercises(keyWord, number);
        List<ExerciseVo> exerciseVoList = new ArrayList<>();
        KeyWord keyEntity = keyWordService.getKeyWordByID(keyWordID);
        int length;
        if(keyEntity != null){
            Iterator<Exercise> iterator = exerciseList.iterator();
            while (iterator.hasNext()){
                Exercise e = iterator.next();
                for(Exercise ke: keyEntity.getExercises()){
                    if(e.getID() == ke.getFather() || e.getID() == ke.getID()){
                        iterator.remove();
                    }
                }
            }
            for(Exercise ke: keyEntity.getExercises()){
                exerciseVoList.add(new ExerciseVo(ke));
            }
            length = exerciseList.size();
            for (Exercise e: exerciseList) {
                keyEntity.getExercises().add(e);
                keyWordService.saveKeyWord(keyEntity);
                exerciseVoList.add(new ExerciseVo(e));
            }
            exerciseVoList.sort(Comparator.comparing(ExerciseVo::getExerciseID));
            if(length < number){
                respBean = RespBean.ok("获取成功，缺少" + (number - length) + "题", exerciseVoList);
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
                updateExercise.setFather(exerciseID);
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
    @PostMapping("/deleteexercise")
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

    /**
     * 测验题完成人数统计
     * @param keyWordID
     * @param courseID
     * @return
     */
    @PostMapping("/exercisesSum")
    public RespBean getStuKeyExercises(@RequestParam(value = "keywordID") int keyWordID,
                                       @RequestParam(value = "courseID") int courseID){
        RespBean respBean = RespBean.requestError("查询失败");

        Course course = courseService.getCourseByID(courseID);
        KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
        Map<String, Object> map = new HashMap<>();
        List<StudentVo> subStudentList = new ArrayList<>();
        List<StudentVo> notSubStudentList = new ArrayList<>();
        int submitNum = 0;
        if(course != null && keyWord != null){
            Exercise exercise = new Exercise();
            for(Exercise e: keyWord.getExercises()){
                exercise = e;
                break;
            }
            if(exercise == null){
                return respBean;
            }
            for (User student: course.getStudents()){
                Answer answer = answerService.getStuAnswer(student.getID(), exercise.getID());
                if(answer != null && answer.isSubmit()){
                    subStudentList.add(new StudentVo(student));
                    submitNum++;
                }
                else{
                    notSubStudentList.add(new StudentVo(student));
                }
            }
            map.put("submitNum", submitNum);
            map.put("notSubmitNum", course.getStudents().size() - submitNum);
            map.put("subStudentList", subStudentList);
            map.put("notSubStudentList", notSubStudentList);
            respBean = RespBean.ok("查询成功", map);
        }
        return respBean;
    }
}
