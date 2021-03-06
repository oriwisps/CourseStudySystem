package com.zjj.coursestudy.service.Impl;

import com.zjj.coursestudy.dao.UserDao;

import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public boolean login(String userName, String password){
        if(userDao.findByName(userName).getPassword().equals(password)){
            return true;
        }
        else {
            return false;
        }
    }

    public User getUserByName(String userName){
        return userDao.findByName(userName);
    }

    public boolean existByUserName(String userName){
        return userDao.existsByName(userName);
    }

    public boolean existByPhone(String phone){
        return userDao.existsByPhone(phone);
    }

    public boolean existByEmail(String email){
        return userDao.existsByEmail(email);
    }

    public User saveUser(User user){
        return userDao.save(user);
    }

    public User getUserByID(String ID){
        Optional<User> user = userDao.findById(ID);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }
}
