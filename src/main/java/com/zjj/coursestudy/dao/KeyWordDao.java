package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.Exercise;
import com.zjj.coursestudy.entity.KeyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface KeyWordDao extends JpaRepository<KeyWord, Integer> {

}
