package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseDao extends JpaRepository<Exercise, Integer> {
}
