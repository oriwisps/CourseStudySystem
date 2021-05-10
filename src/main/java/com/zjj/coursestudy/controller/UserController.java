package com.zjj.coursestudy.controller;

import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.model.JwtToken;
import com.zjj.coursestudy.model.RespBean;
import com.zjj.coursestudy.service.UserService;
import com.zjj.coursestudy.utils.JwtUtil;
import com.zjj.coursestudy.vo.UserInfoVo;
import com.zjj.coursestudy.vo.UserSignInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @JwtToken
    @PostMapping("/login")
    public RespBean login(@RequestParam(value = "loginName") String userName,
                          @RequestParam(value = "password") String password){

        RespBean respBean;
        Map<String, String> token = new HashMap<>();
        if(userService.login(userName, password)){
            token.put("token",JwtUtil.sign(userName));
            respBean = RespBean.ok("登录成功", token);
        }
        else {
            respBean = RespBean.requestError("用户名或密码错误");
        }
        return respBean;
    }

    /**
     * 身份验证
     * @param password 密码
     * @param token token
     * @param request
     * @return
     */
    @PostMapping("/checkout")
    public RespBean checkOut(@RequestParam(value = "password") String password,
                             @RequestHeader(value = "token") String token,
                             HttpServletRequest request){

        RespBean respBean;

        User user = userService.getUserByName(JwtUtil.getUserName(token));
        if(user != null){
            if(user.getPassword().equals(password)){
                respBean = RespBean.ok("校验成功");
            }
            else {
                respBean = RespBean.requestError("校验失败");
            }
        }
        else {
            respBean = RespBean.requestError("校验失败");
        }
        return respBean;
    }

    /**
     * 用户注册
     * @param userSignInVo 注册信息
     * @return
     */
    @JwtToken
    @PostMapping("/signin")
    public RespBean signIn(@RequestBody UserSignInVo userSignInVo){
        RespBean respBean;
        if(userService.existByUserName(userSignInVo.getName())){
            respBean = RespBean.requestError("用户名重复");
            return respBean;
        }
        if(userService.existByPhone(userSignInVo.getPhone())){
            respBean = RespBean.requestError("电话重复");
            return respBean;
        }
        if(userService.existByEmail(userSignInVo.getEmail())){
            respBean = RespBean.requestError("邮箱重复");
            return respBean;
        }

        User user = userSignInVo.createUser();
        user = userService.saveUser(user);
        if(user != null){
            respBean = RespBean.ok("注册成功");
        }
        else {
            respBean = RespBean.requestError("注册失败");
        }

        return respBean;
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @GetMapping("/info")
    public RespBean queryUserInfo(@RequestHeader(value = "token") String token){
        RespBean respBean;
        User user = userService.getUserByName(JwtUtil.getUserName(token));
        if(user != null){
            UserInfoVo userInfoVo = new UserInfoVo(user);
            respBean = RespBean.ok("查询成功", userInfoVo);
        }
        else {
            respBean = RespBean.requestError("查询失败");
        }
        return respBean;
    }

    @PostMapping("/info")
    public RespBean queryUserInfoByID(@RequestParam(value = "studentID") String studentID){
        RespBean respBean;
        User user = userService.getUserByID(studentID);
        if(user != null){
            UserInfoVo userInfoVo = new UserInfoVo(user);
            respBean = RespBean.ok("查询成功", userInfoVo);
        }
        else {
            respBean = RespBean.requestError("查询失败");
        }
        return respBean;
    }

    /**
     * 修改用户电话或邮箱
     * @param phone 电话（可为空，此时不修改）
     * @param email 邮箱（可为空，此时不修改）
     * @param token
     * @return
     */
    @PutMapping("/update")
    public RespBean updateUserInfo(@RequestParam(value = "phone", required = false, defaultValue = "") String phone,
                                   @RequestParam(value = "email", required = false, defaultValue = "") String email,
                                   @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("修改失败");
        String userName = JwtUtil.getUserName(token);
        User user = userService.getUserByName(userName);
        if(user != null){
            if(!phone.equals("")){
                user.setPhone(phone);
            }
            else if(!email.equals("")){
                user.setEmail(email);
            }
        }
        else {
            return respBean;
        }
        user = userService.saveUser(user);
        if(user != null){
            respBean = RespBean.ok("修改成功");
        }
        return respBean;
    }

    /**
     * 用户密码修改
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param token
     * @return
     */
    @PutMapping("/changepassword")
    public RespBean changePassword(@RequestParam(value = "opwd") String oldPassword,
                                   @RequestParam(value = "npwd") String newPassword,
                                   @RequestHeader(value = "token") String token){
        RespBean respBean = RespBean.requestError("修改失败");
        User user = userService.getUserByName(JwtUtil.getUserName(token));
        if(user != null){
            if(oldPassword.equals(user.getPassword())){
                user.setPassword(newPassword);
                user = userService.saveUser(user);
                if(user != null){
                    respBean = RespBean.ok("修改成功");
                }
            }
        }
        return respBean;
    }
}
