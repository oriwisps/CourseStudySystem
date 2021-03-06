package com.zjj.coursestudy.service;

import com.zjj.coursestudy.entity.Exercise;
import com.zjj.coursestudy.entity.KeyWord;

import java.util.List;

public interface KeyWordService {

    KeyWord getKeyWordByID(int ID);

    KeyWord saveKeyWord(KeyWord keyWord);

    boolean deleteKeyWord(int keyWordID);

    List<Exercise> autoGetExercises(String keyWord, Integer number);
}
