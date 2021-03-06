package com.zjj.coursestudy.utils;

import java.util.Random;

public class CourseCodeUtil {

    public static String createCode(){
        CourseCodeUtil courseCodeUtil = new CourseCodeUtil();
        return courseCodeUtil.getRandomString(8);
    }

    private String getRandomString(int length){
        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer code = new StringBuffer();
        for(int i = 0; i < length; i++){
            int number = random.nextInt(36);
            code.append(str.charAt(number));
        }
        return code.toString();
    }
}
