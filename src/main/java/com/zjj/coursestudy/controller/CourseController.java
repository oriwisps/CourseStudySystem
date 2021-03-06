package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.Course;
import com.zjj.coursestudy.entity.EvaluationItem;
import com.zjj.coursestudy.entity.KeyWord;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.model.RespBean;
import com.zjj.coursestudy.service.CourseService;
import com.zjj.coursestudy.service.EvaluationItemService;
import com.zjj.coursestudy.service.KeyWordService;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.CourseVo;
import com.zjj.coursestudy.vo.EvaluationItemVo;
import com.zjj.coursestudy.vo.KeyWordVo;
import com.zjj.coursestudy.vo.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EvaluationItemService evaluationItemService;

    @Autowired
    private KeyWordService keyWordService;

    /**
     * 通过课程码查询课程
     * @param code 课程码
     * @return
     */
    @PostMapping("/codequery")
    public RespBean queryCourseByCode(@RequestParam(value = "code") String code){
        RespBean respBean = RespBean.requestError("查询失败");
        Course course = courseService.getCourseByCode(code);
        if(course != null){
            CourseVo courseVo = new CourseVo(course);
            respBean = RespBean.ok("查询成功", courseVo);
            return respBean;
        }
        else {
            return respBean;
        }
    }

    /**
     * 通过ID查询课程
     * @param ID 课程ID
     * @return
     */
    @PostMapping("/idquery")
    public RespBean queryCourseByID(@RequestParam(value = "courseID") int ID){
        RespBean respBean = RespBean.requestError("查询失败");
        Course course = courseService.getCourseByID(ID);
        if(course != null){
            CourseVo courseVo = new CourseVo(course);
            respBean = RespBean.ok("查询成功", courseVo);
            return respBean;
        }
        else {
            return respBean;
        }
    }

    /**
     * 查询学生已加入课程
     * @param token
     * @return
     */
    @GetMapping("/student")
    public RespBean getStudentAllCourses(@RequestHeader(value = "token") String token){
        RespBean respBean;
        List<CourseVo> courseVoList = new ArrayList<>();

        String studentName = JwtUtil.getUserName(token);
        Set<Course> studentCourses = courseService.getStudentAllCourses(studentName);
        if(studentCourses != null){
            for (Course course: studentCourses) {
                courseVoList.add(new CourseVo(course));
            }
        }
        respBean = RespBean.ok("查询成功", courseVoList);
        return respBean;
    }

    /**
     * 查询教师已创建课程
     * @param token
     * @return
     */
    @GetMapping("/teacher")
    public RespBean getTeacherAllCourses(@RequestHeader(value = "token") String token){
        RespBean respBean;
        List<CourseVo> courseVoList = new ArrayList<>();
        List<CourseVo> temp = new ArrayList<>();

        String teacherName = JwtUtil.getUserName(token);
        Set<Course> teacherCourses = courseService.getTeacherAllCourses(teacherName);
        if(teacherCourses != null){
            for (Course course: teacherCourses) {
                temp.add(new CourseVo(course));
            }
            courseVoList.addAll(temp);
        }
        respBean = RespBean.ok("查询成功", courseVoList);
        return respBean;
    }

    /**
     * 查询课程中所有学生
     * @param ID 课程ID
     * @return
     */
    @PostMapping("/allstudent")
    public RespBean getCourseAllStudents(@RequestParam(value = "courseID")int ID){
        RespBean respBean;
        List<StudentVo> studentVoList = new ArrayList<>();

        Set<User> students = courseService.getCourseAllStudent(ID);
        if(students != null){
            for (User student: students) {
                studentVoList.add(new StudentVo(student));
            }
        }
        respBean = RespBean.ok("查询成功", studentVoList);
        return respBean;
    }

    /**
     * 查询课程的学业评价项
     * @param courseID 课程ID
     * @return
     */
    @PostMapping("/queryitem")
    public RespBean getCourseAllItems(@RequestParam(value = "courseID") int courseID){
        RespBean respBean;
        List<EvaluationItemVo> evaluationItemVoList = new ArrayList<>();
        Course course = courseService.getCourseByID(courseID);
        if(course != null){
            for (EvaluationItem item: course.getEvaluationItems()) {
                evaluationItemVoList.add(new EvaluationItemVo(item));
            }
        }
        respBean = RespBean.ok("查询成功", evaluationItemVoList);
        return respBean;
    }

    /**
     * 修改课程学业评价项
     * @return
     */
    @PostMapping("/updateitem")
    public RespBean updateItems(@RequestParam(value = "courseID") Integer courseID,
                                @RequestParam(value = "itemID", required = false) Integer itemID,
                                @RequestParam(value = "itemName") String itemName,
                                @RequestParam(value = "description") String description,
                                @RequestParam(value = "proportion") Double proportion){
        RespBean respBean = RespBean.requestError("修改失败");
        Course course = courseService.getCourseByID(courseID);
        if(course != null){
            EvaluationItem newItem = new EvaluationItem();
            EvaluationItem oldItem;
            if(itemID != null){
                oldItem = evaluationItemService.getItemByID(itemID);
                if(course.getEvaluationItems().contains(oldItem)){
                    course.getEvaluationItems().remove(oldItem);
                }
                oldItem.setName(itemName);
                oldItem.setDescription(description);
                oldItem.setProportion(proportion);
                course.getEvaluationItems().add(oldItem);
            }
            else {
                newItem.setName(itemName);
                newItem.setDescription(description);
                newItem.setProportion(proportion);
                newItem.setCourse(course);
                course.getEvaluationItems().add(newItem);
            }
            course = courseService.saveCourse(course);
            if(course != null){
                respBean = RespBean.ok("修改成功");
                return respBean;
            }
        }

        return respBean;
    }

    /**
     * 删除学业评价项
     * @param itemID 评价项ID
     * @return
     */
    @DeleteMapping("/deleteitem")
    public RespBean deleteItem(@RequestParam(value = "itemID") Integer itemID){
        RespBean respBean = RespBean.requestError("删除失败");
        if(evaluationItemService.deleteItem(itemID)){
            respBean = RespBean.ok("删除成功");
            return respBean;
        }
        return respBean;
    }

    /**
     * 查询课程的关键字
     * @param courseID 课程ID
     * @return
     */
    @PostMapping("/querykey")
    public RespBean getCourseKeyWord(@RequestParam(value = "courseID") Integer courseID){
        RespBean respBean = RespBean.requestError("查询失败");
        Course course = courseService.getCourseByID(courseID);
        List<KeyWordVo> keyWordVoList = new ArrayList<>();
        if(course != null){
            for (KeyWord k :course.getKeyWords()) {
                keyWordVoList.add(new KeyWordVo(k));
            }
            respBean = RespBean.ok("查询成功",keyWordVoList);
            return respBean;
        }
        return respBean;
    }

    /**
     * 为课程添加关键字
     * @param courseID 课程ID
     * @param keyWordID 关键字ID
     * @return
     */
    @PostMapping("/addkey")
    public RespBean addCourseKeyWord(@RequestParam(value = "courseID") Integer courseID,
                                     @RequestParam(value = "keyWordID") Integer keyWordID){
        RespBean respBean = RespBean.requestError("添加失败");
        Course course = courseService.getCourseByID(courseID);
        if(course != null){
            KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
            if(keyWord != null){
                if(course.getKeyWords().contains(keyWord)){
                    respBean = RespBean.requestError("不可重复添加同一关键字");
                    return respBean;
                }
                course.getKeyWords().add(keyWord);
                course = courseService.saveCourse(course);
                if(course != null){
                    respBean = RespBean.ok("添加成功");
                    return respBean;
                }
            }
        }
        return respBean;
    }

    /**
     * 删除课程的关键字
     * @param courseID 课程ID
     * @param keyWordID 关键字ID
     * @return
     */
    @DeleteMapping("/deletekey")
    public RespBean deleteCourseKeyWord(@RequestParam(value = "courseID") Integer courseID,
                                        @RequestParam(value = "keyWordID") Integer keyWordID){
        RespBean respBean = RespBean.requestError("删除失败");
        Course course = courseService.getCourseByID(courseID);
        if(course != null){
            KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
            if(keyWord != null){
                if(course.getKeyWords().contains(keyWord)){
                    course.getKeyWords().remove(keyWord);
                    course = courseService.saveCourse(course);
                    if(course != null){
                        respBean = RespBean.ok("删除成功");
                        return respBean;
                    }
                }
            }
        }
        return respBean;
    }
}
