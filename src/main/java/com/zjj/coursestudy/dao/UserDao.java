package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String> {

    User findByName(String userName);

    boolean existsByName(String userName);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);
}
