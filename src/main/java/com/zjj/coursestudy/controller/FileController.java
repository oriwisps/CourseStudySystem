package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.Course;
import com.zjj.coursestudy.entity.Exercise;
import com.zjj.coursestudy.entity.KeyWord;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.utils.UUIDUtil;
import com.zjj.coursestudy.vo.JwtToken;
import com.zjj.coursestudy.vo.RespBean;
import com.zjj.coursestudy.service.CourseService;
import com.zjj.coursestudy.service.ExerciseService;
import com.zjj.coursestudy.service.KeyWordService;
import com.zjj.coursestudy.service.UserService;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class FileController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private KeyWordService keyWordService;

    @Autowired
    private ExerciseService exerciseService;

    /**
     * 解析Excel文件，课程导入学生
     * @param file
     * @param courseID
     * @return
     * @throws IOException
     */
    @JwtToken
    @PostMapping("/importStudent")
    public RespBean importStudent(@RequestBody MultipartFile file,
                           @RequestParam(value = "courseID") int courseID) throws IOException {

        RespBean respBean = RespBean.requestError("导入失败");
        Course course = courseService.getCourseByID(courseID);
        User student;

        if(course == null){
            return respBean;
        }
        InputStream in =null;
        try {
            //将file转InputStream
            in = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将InputStream转XLSX对象（即表格对象）
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
        //获取表格的第一页
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        //获取该页有多少数据
        int rowNum = xssfSheet.getLastRowNum();
        for(int i = 1; i <= rowNum; i++){
            //获取当前行
            XSSFRow xssfRow = xssfSheet.getRow(i);

            //以此获得该行的所有单元格
            String username = xssfRow.getCell(0).toString();

            student = userService.getUserByName(username);
            if(student != null){
                course.getStudents().add(student);
            }
            else {
                User newStudent = new User();
                newStudent.setName(username);
                newStudent.setPassword("123456");
                newStudent.setID(UUIDUtil.createUUID());
                newStudent.setRole("student");
                newStudent.setPhone(username);
                newStudent.setEmail(username + "@163.com");
                newStudent = userService.saveUser(newStudent);
                if(newStudent != null){
                    course.getStudents().add(newStudent);
                }
            }
        }
        course = courseService.saveCourse(course);
        if(course != null){
            respBean = RespBean.ok("导入成功");
        }
        return respBean;
    }

    /**
     * 解析word文件。关键字导入题目
     * @param file
     * @param keyWordID
     * @return
     * @throws IOException
     */
    @JwtToken
    @PostMapping("/importExercise")
    public RespBean importExercise(@RequestParam(value = "file") MultipartFile file,
                                   @RequestParam(value = "keyWordID") int keyWordID) throws IOException{
        RespBean respBean = RespBean.requestError("解析失败");

        String textFileName=file.getOriginalFilename();
        String now = "";
        Map<String, String> wordMap = new LinkedHashMap<>();
        KeyWord keyWord = keyWordService.getKeyWordByID(keyWordID);
        if(keyWord == null){
            return respBean;
        }
        try {
            if(textFileName.endsWith(".doc")){ //判断文件格式
                InputStream fis = file.getInputStream();
                WordExtractor wordExtractor = new WordExtractor(fis);//使用HWPF组件中WordExtractor类从Word文档中提取文本或段落
                int i=1;
                for(String words : wordExtractor.getParagraphText()){//获取段落内容
                    //wordMap.put("DOC文档，第（"+i+"）段内容",words);
                    i++;
                }
                fis.close();
            }
            if(textFileName.endsWith(".docx")){
                File uFile = new File("tempFile.docx");//创建一个临时文件
                if(!uFile.exists()){
                    uFile.createNewFile();
                }
                FileCopyUtils.copy(file.getBytes(), uFile);//复制文件内容
                OPCPackage opcPackage = POIXMLDocument.openPackage("tempFile.docx");//包含所有POI OOXML文档类的通用功能，打开一个文件包。
                XWPFDocument document = new XWPFDocument(opcPackage);//使用XWPF组件XWPFDocument类获取文档内容
                List<XWPFParagraph> paras = document.getParagraphs();
                for(XWPFParagraph paragraph : paras){
                    String words = paragraph.getText();
                    if(Pattern.matches(".*\\[题目\\].*", words)){
                        String[] wordArray = words.split("\\[题目]");
                        if(wordArray.length == 1){
                            words = wordArray[0];
                        }
                        else{
                            words = wordArray[1];
                        }
                        if(!wordMap.isEmpty()){
                            Exercise exercise = new Exercise();
                            exercise.setUpdated(false);
                            exercise.setContent(wordMap.get("content"));
                            exercise.setAnswer(wordMap.get("answer"));
                            exerciseService.saveExercise(exercise);
                            keyWord.getExercises().add(exercise);
                            keyWordService.saveKeyWord(keyWord);
                        }
                        wordMap = new LinkedHashMap<>();
                        now = "content";
                        wordMap.put("content", words);
                    }
                    else if(Pattern.matches(".*[答案].*", words)){
                        now = "answer";
                        wordMap.put("answer", words);
                    }
                    else{
                        if(now.equals("content")){
                            String content = wordMap.get("content");
                            wordMap.put("content", content + "\n" + words);
                        }
                        else if(now.equals("answer")){
                            String answer = wordMap.get("answer");
                            wordMap.put("answer", answer + "\n" + words);
                        }
                    }
                }
                Exercise exercise = new Exercise();
                exercise.setUpdated(false);
                exercise.setContent(wordMap.get("content"));
                exercise.setAnswer(wordMap.get("answer"));
                exerciseService.saveExercise(exercise);
                keyWord.getExercises().add(exercise);
                keyWordService.saveKeyWord(keyWord);
                uFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        respBean = RespBean.ok("解析成功", wordMap);

        return respBean;
    }
}
