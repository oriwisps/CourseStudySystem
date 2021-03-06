package com.zjj.coursestudy.service;

import com.zjj.coursestudy.entity.Exercise;

public interface ExerciseService {

    Exercise saveExercise(Exercise exercise);

    Exercise getExerciseByID(int ID);

    boolean deleteExercise(int ID);
}
