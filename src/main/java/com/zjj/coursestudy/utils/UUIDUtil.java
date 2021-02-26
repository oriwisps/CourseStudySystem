package com.zjj.coursestudy.utils;

import java.util.UUID;

public class UUIDUtil {

    //创建32位UUID
    public static String createUUID(){
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
