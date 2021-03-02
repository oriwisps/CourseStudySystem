package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreDao extends JpaRepository<Score, Integer> {
}
