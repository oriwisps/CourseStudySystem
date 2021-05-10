package com.zjj.coursestudy;

import com.zjj.coursestudy.dao.*;
import com.zjj.coursestudy.entity.*;
import com.zjj.coursestudy.service.EvaluationItemService;
import com.zjj.coursestudy.utils.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SpringBootTest
class CoursestudyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private EvaluationItemDao evaluationItemDao;

    @Autowired
    private ScoreDao scoreDao;

    @Autowired
    private KeyWordDao keyWordDao;

    @Autowired
    private ExerciseDao exerciseDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private DiscussionDao discussionDao;

    @Test
    void daoTest(){
        User student = new User();
        student.setID(UUIDUtil.createUUID());
        student.setRole("student");
        student.setEmail("ddd@163.com");
        student.setName("luvz");
        student.setPhone("15927563368");
        student.setPassword("luv123456789");
        //userDao.save(student);

        User teacher = new User();
        teacher.setID(UUIDUtil.createUUID());
        teacher.setRole("teacher");
        teacher.setEmail("ghlcf@163.com");
        teacher.setName("sakura");
        teacher.setPhone("17371445273");
        teacher.setPassword("luv123456789");
        //userDao.save(teacher);

        Course course = new Course();
        course.setEnding(false);
        course.setName("高等数学1");
        course.setTeacher(teacher);
        course.setTeacherName(teacher.getName());
        course.setTeacherContact(teacher.getEmail());
        course.setInvitationCode("DUJHGTK");
        //courseDao.save(course);


        Course c = courseDao.findById(1).get();
        EvaluationItem evaluationItem1 = new EvaluationItem();
        evaluationItem1.setCourse(c);
        evaluationItem1.setDescription("评价项描述");
        evaluationItem1.setName("课后作业");
        evaluationItem1.setProportion(0.2);
        evaluationItemDao.save(evaluationItem1);

        EvaluationItem evaluationItem2 = new EvaluationItem();
        evaluationItem2.setCourse(c);
        evaluationItem2.setDescription("评价项描述");
        evaluationItem2.setName("期末考试");
        evaluationItem2.setProportion(0.8);
        evaluationItemDao.save(evaluationItem2);

    }

    @Test
    @Transactional
    @Rollback(value = false)
    void queryTest(){
        KeyWord keyWord = keyWordDao.findKeyWordByID(10);
        for(Exercise e: keyWord.getExercises()){
            System.out.print(e.getContent());
        }

    }

    @Test
    @Transactional
    @Rollback(value = false)
    void addTest(){
        User student = userDao.findById("83ff28a1e8a44669a18ff60fb63037df").get();
        Course course = courseDao.getOne(1);

        for (Course c: student.getCourses()) {
            System.out.println(c.getName());
        }

    }

    @Test
    void scoreTest(){
        User student = userDao.findById("83ff28a1e8a44669a18ff60fb63037df").get();
        Course course = courseDao.getCourseByID(1);
        List<Score> scoreList = scoreDao.getScoresByCourseAndStudent(student.getID(), course.getID());
        for (Score s: scoreList) {
            System.out.println(s.getScore());
        }
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void exerciseTest(){
        List<Exercise> exerciseList = exerciseDao.autoGetExercises("e",1);
        for (Exercise e: exerciseList) {
            System.out.println(e.getContent());
        }

    }

    @Test
    void answerTest(){

        List<Answer> answer = answerDao.getAnswersByStudent("83ff28a1e8a44669a18ff60fb63037df");
        for(Answer a: answer){
            System.out.print(a.getContent());
        }

    }

    @Test
    void discussionTest(){

        Discussion discussion = discussionDao.findById(1).get();
        System.out.println(discussion.getSender().getName());
        System.out.println(discussion.getReceiver().getName());
        System.out.println(discussion.getTime());
        System.out.println(discussion.getContent());

    }

    @Autowired
    private EvaluationItemService evaluationItemService;

    @Test
    void deleteTest(){

    }
}
