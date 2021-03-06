package com.zjj.coursestudy.service.Impl;

import com.zjj.coursestudy.dao.ExerciseDao;
import com.zjj.coursestudy.entity.Exercise;
import com.zjj.coursestudy.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseDao exerciseDao;

    public Exercise saveExercise(Exercise exercise){
        return exerciseDao.save(exercise);
    }

    public Exercise getExerciseByID(int ID){
        return exerciseDao.getOne(ID);
    }

    public boolean deleteExercise(int ID){
        exerciseDao.deleteById(ID);
        return !exerciseDao.existsById(ID);
    }
}
