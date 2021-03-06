package com.zjj.coursestudy.service;

import com.zjj.coursestudy.entity.User;

public interface UserService {

    /**
     * 用户登录
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    boolean login(String userName, String password);

    /**
     * 根据用户名获取用户
     * @param userName 用户名
     * @return
     */
    User getUserByName(String userName);

    /**
     * 通过用户名判断是否存在该用户
     * @param userName
     * @return
     */
    boolean existByUserName(String userName);

    /**
     * 通过电话判断是否存在该用户
     * @param phone
     * @return
     */
    boolean existByPhone(String phone);

    /**
     * 通过邮箱判断是否存在该用户
     * @param email
     * @return
     */
    boolean existByEmail(String email);

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    User saveUser(User user);

    User getUserByID(String ID);
}
