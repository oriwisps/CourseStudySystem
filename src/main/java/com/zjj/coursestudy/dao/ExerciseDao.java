package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseDao extends JpaRepository<Exercise, Integer> {

    @Query(value = "select * from exercise e where e.content like %?1% limit ?2", nativeQuery = true)
    List<Exercise> autoGetExercises(String keyWord, int number);
}
