package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.EvaluationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationItemDao extends JpaRepository<EvaluationItem, Integer> {
}
