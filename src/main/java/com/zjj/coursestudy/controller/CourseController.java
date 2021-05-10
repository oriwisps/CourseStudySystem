package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.*;
import com.zjj.coursestudy.model.RespBean;
import com.zjj.coursestudy.service.*;
import com.zjj.coursestudy.utils.CourseCodeUtil;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.CourseVo;
import com.zjj.coursestudy.vo.EvaluationItemVo;
import com.zjj.coursestudy.vo.KeyWordVo;
import com.zjj.coursestudy.vo.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EvaluationItemService evaluationItemService;

    @Autowired
    private KeyWordService keyWordService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScoreService scoreService;

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
     * 学生用户加入课程
     * @param courseID
     * @param token
     * @return
     */
    @PostMapping("/addcourse")
    public RespBean addCourse(@RequestParam(value = "courseID") int courseID,
                              @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("加入失败");

        User student = userService.getUserByName(JwtUtil.getUserName(token));
        Course course = courseService.getCourseByID(courseID);

        if(student != null && course != null){
            course.getStudents().add(student);
            courseService.saveCourse(course);
            respBean = RespBean.ok("加入成功");
        }
        return respBean;
    }

    /**
     * 查询学生已加入课程
     * @param token
     * @return
     */
    @GetMapping("/student")
    public RespBean getStudentCourses(@RequestHeader(value = "token") String token,
                                         @RequestParam(value = "end") boolean end){
        RespBean respBean;
        List<CourseVo> courseVoList = new ArrayList<>();

        String studentName = JwtUtil.getUserName(token);
        Set<Course> studentCourses = courseService.getStudentCourses(studentName, end);
        if(studentCourses != null){
            for (Course course: studentCourses) {
                courseVoList.add(new CourseVo(course));
            }
        }
        courseVoList.sort(Comparator.comparing(CourseVo::getCourseID));
        respBean = RespBean.ok("查询成功", courseVoList);
        return respBean;
    }

    @GetMapping("/sallcourse")
    public RespBean getStudentAllCourses(@RequestHeader(value = "token") String token){
        RespBean respBean;
        List<CourseVo> courseVoList = new ArrayList<>();

        String studentName = JwtUtil.getUserName(token);
        Set<Course> studentCourses = courseService.getStudentCourses(studentName, true);
        studentCourses.addAll(courseService.getStudentCourses(studentName, false));
        if(studentCourses != null){
            for (Course course: studentCourses) {
                courseVoList.add(new CourseVo(course));
            }
        }
        courseVoList.sort(Comparator.comparing(CourseVo::getCourseID));
        respBean = RespBean.ok("查询成功", courseVoList);
        return respBean;
    }

    /**
     * 查询教师已创建课程中未结束课程或已结束课程
     * @param token
     * @return
     */
    @GetMapping("/teacher")
    public RespBean getTeacherNotEndCourses(@RequestParam(value = "end") boolean end,
                                            @RequestHeader(value = "token") String token){
        RespBean respBean;
        List<CourseVo> courseVoList = new ArrayList<>();
        List<CourseVo> temp = new ArrayList<>();
        List<Course> courseList;

        String teacherName = JwtUtil.getUserName(token);
        User teacher = userService.getUserByName(teacherName);
        Set<Course> teacherCourses = courseService.getCoursesByTeacherAndEnding(teacher, end);
        if(teacherCourses != null){
            courseList = new ArrayList<>(teacherCourses);
            courseList.sort(Comparator.comparing(Course::getID));
            for (Course course: courseList) {
                temp.add(new CourseVo(course));
            }
            courseVoList.addAll(temp);
        }
        respBean = RespBean.ok("查询成功", courseVoList);
        return respBean;
    }

    @GetMapping("/tallcourse")
    public RespBean getTeacherAllCourses(@RequestHeader(value = "token") String token){
        RespBean respBean;
        List<CourseVo> courseVoList = new ArrayList<>();
        List<CourseVo> temp = new ArrayList<>();

        String teacherName = JwtUtil.getUserName(token);
        User teacher = userService.getUserByName(teacherName);
        Set<Course> teacherCourses = courseService.getCoursesByTeacherAndEnding(teacher, true);
        teacherCourses.addAll(courseService.getCoursesByTeacherAndEnding(teacher, false));
        if(teacherCourses != null){
            List<Course> courseList = new ArrayList<>(teacherCourses);
            courseList.sort(Comparator.comparing(Course::getID));
            for (Course course: courseList) {
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
        RespBean respBean = RespBean.requestError("查询失败");
        List<StudentVo> studentVoList = new ArrayList<>();

        Set<User> students = courseService.getCourseAllStudent(ID);

        if(students != null){
            List<User> studentList = new ArrayList<>(students);
            studentList.sort(Comparator.comparing(User::getName));
            for (User student: studentList) {
                studentVoList.add(new StudentVo(student));
            }
            respBean = RespBean.ok("查询成功", studentVoList);
        }
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
            List<EvaluationItem> evaluationItemList = new ArrayList<>(course.getEvaluationItems());
            evaluationItemList.sort(Comparator.comparing(EvaluationItem::getID));
            for (EvaluationItem item: evaluationItemList) {
                evaluationItemVoList.add(new EvaluationItemVo(item));
            }
        }
        respBean = RespBean.ok("查询成功", evaluationItemVoList);
        return respBean;
    }

    /**
     * 修改或新增课程学业评价项
     * @return
     */
    @PostMapping("/updateitem")
    public RespBean updateItems(@RequestParam(value = "courseID") Integer courseID,
                                @RequestParam(value = "itemID", required = false) Integer itemID,
                                @RequestParam(value = "itemName") String itemName,
                                @RequestParam(value = "description") String description,
                                @RequestParam(value = "proportion") Double proportion){
        RespBean respBean = RespBean.requestError("修改失败");
        boolean isNew = true;
        Course course = courseService.getCourseByID(courseID);
        if(course != null){
            EvaluationItem newItem = new EvaluationItem();
            EvaluationItem oldItem;
            if(itemID != null){
                isNew = false;
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
                if(isNew){
                    for(EvaluationItem e: course.getEvaluationItems()){
                        if(e.getName().equals(newItem.getName())){
                            newItem = e;
                            break;
                        }
                    }
                    for(User student: course.getStudents()){
                        Score score = new Score();
                        score.setScore(0);
                        score.setEvaluationItem(newItem);
                        score.setStudent(student);
                        scoreService.saveScore(score);
                    }
                }
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
    @PostMapping("/deleteitem")
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
            List<KeyWord> keyWordList = new ArrayList<>(course.getKeyWords());
            keyWordList.sort(Comparator.comparing(KeyWord::getID));
            for (KeyWord k :keyWordList) {
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
    @PostMapping("/deletekey")
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

    /**
     * 创建课程
     * @param courseName 课程名
     * @param teacherName 教师名（课程）
     * @param teacherContact 教师联系方式（课程）
     * @param token
     * @return
     */
    @PostMapping("/create")
    public RespBean createCourse(@RequestParam(value = "courseName") String courseName,
                                 @RequestParam(value = "teacherName", required = false, defaultValue = "") String teacherName,
                                 @RequestParam(value = "teacherContact", required = false, defaultValue = "") String teacherContact,
                                 @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("创建失败");
        String userName = JwtUtil.getUserName(token);

        User teacher = userService.getUserByName(userName);
        Course course = new Course();
        course.setTeacher(teacher);
        course.setName(courseName);
        if(teacherName.equals("")){
            course.setTeacherName(teacher.getName());
        }
        else{
            course.setTeacherName(teacherName);
        }
        if(teacherContact.equals("")){
            course.setTeacherContact(teacher.getEmail());
        }
        else{
            course.setTeacherContact(teacherContact);
        }
        course.setEnding(false);
        course.setInvitationCode(CourseCodeUtil.createCode());
        course = courseService.saveCourse(course);
        if(course != null){
            respBean = RespBean.ok("创建成功");
            return respBean;
        }
        return respBean;
    }

    /**
     * 课程信息修改
     * @param courseID 课程ID
     * @param courseName 课程名
     * @param teacherName 教师名
     * @param teacherContact 教师联系方式
     * @param end 课程状态
     * @return
     */
    @PostMapping("/update")
    public RespBean updateCourse(@RequestParam(value = "courseID") int courseID,
                                 @RequestParam(value = "courseName", required = false, defaultValue = "") String courseName,
                                 @RequestParam(value = "teacherName", required = false, defaultValue = "") String teacherName,
                                 @RequestParam(value = "teacherContact", required = false, defaultValue = "") String teacherContact,
                                 @RequestParam(value = "end", required = false, defaultValue = "") Boolean end){
        RespBean respBean = RespBean.requestError("修改失败");

        Course course = courseService.getCourseByID(courseID);
        if(!courseName.equals("")){
            course.setName(courseName);
        }
        if(!teacherName.equals("")){
            course.setTeacherName(teacherName);
        }
        if(!teacherContact.equals("")){
            course.setTeacherContact(teacherContact);
        }
        if(end != null){
            course.setEnding(end);
        }
        course = courseService.saveCourse(course);
        if(course != null){
            respBean = RespBean.ok("修改成功");
            return respBean;
        }
        return respBean;
    }

    /**
     * 获取课程可添加的关键字（教师创建并未添加到课程中的关键字）
     * @param courseID 课程ID
     * @param token
     * @return
     */
    @PostMapping("/getkey")
    public RespBean getKeyword(@RequestParam("courseID")int courseID,
                               @RequestHeader("token")String token){
        RespBean respBean = RespBean.requestError("查询失败");
        String teacherName = JwtUtil.getUserName(token);
        User teacher = userService.getUserByName(teacherName);
        Course course = courseService.getCourseByID(courseID);
        List<KeyWordVo> keyWordVoList = new ArrayList<>();

        if(teacher != null && course != null){
            List<KeyWord> teacherKey = new ArrayList<>(teacher.getKeyWords());
            teacherKey.sort(Comparator.comparing(KeyWord::getID));
            for (KeyWord k: teacherKey) {
                if(!course.getKeyWords().contains(k)){
                    keyWordVoList.add(new KeyWordVo(k));
                }
            }
            respBean = RespBean.ok("查询成功",keyWordVoList);
            return respBean;
        }
        return respBean;
    }

    /**
     * 删除课程中的学生
     * @param courseID
     * @param studentID
     * @return
     */
    @PostMapping("/deleteStu")
    public RespBean deleteStu(@RequestParam(value = "courseID") int courseID,
                              @RequestParam(value = "studentID") String studentID){
        RespBean respBean = RespBean.requestError("删除失败");

        User student = userService.getUserByID(studentID);
        Course course = courseService.getCourseByID(courseID);

        if(student != null && course != null){
            course.getStudents().remove(student);
            courseService.saveCourse(course);
            respBean = RespBean.ok("删除成功");
        }
        return respBean;
    }

    /**
     * 学生用户退出课程
     * @param courseID
     * @param token
     * @return
     */
    @PostMapping("/outcourse")
    public RespBean OutCourse(@RequestParam(value = "courseID") int courseID,
                              @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("退出失败");

        Course course = courseService.getCourseByID(courseID);
        User student = userService.getUserByName(JwtUtil.getUserName(token));

        if(course != null && student != null){
            course.getStudents().remove(student);
            courseService.saveCourse(course);
            respBean = RespBean.ok("退出成功");
        }
        return respBean;
    }

}
