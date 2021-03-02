package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.model.JwtToken;
import com.zjj.coursestudy.utils.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/jwt")
public class JwtController {

    /**
     * 登录并获取token
     * @param userName
     * @param passWord
     * @return
     */
    @JwtToken
    @PostMapping("/login")
    public Object login( String userName, String passWord){

        // 生成签名
        String token= JwtUtil.sign(userName);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userName", userName);
        userInfo.put("passWord", passWord);
        userInfo.put("token", token);
        return userInfo;
    }

    /**
     * @return
     */
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }
}
