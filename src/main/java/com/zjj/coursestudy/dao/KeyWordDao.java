package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.KeyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyWordDao extends JpaRepository<KeyWord, Integer> {
}
