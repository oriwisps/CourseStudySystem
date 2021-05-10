package com.zjj.coursestudy.service.Impl;

import com.zjj.coursestudy.dao.ExerciseDao;
import com.zjj.coursestudy.dao.KeyWordDao;
import com.zjj.coursestudy.entity.Exercise;
import com.zjj.coursestudy.entity.KeyWord;
import com.zjj.coursestudy.service.KeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyWordServiceImpl implements KeyWordService {

    @Autowired
    private KeyWordDao keyWordDao;

    @Autowired
    private ExerciseDao exerciseDao;

    public KeyWord getKeyWordByID(int ID){
        return keyWordDao.findKeyWordByID(ID);
    }

    public KeyWord saveKeyWord(KeyWord keyWord){
        return keyWordDao.save(keyWord);
    }

    public boolean deleteKeyWord(int keyWordID){
        keyWordDao.deleteById(keyWordID);
        if(keyWordDao.existsById(keyWordID)){
            return false;
        }
        return true;
    }

    public List<Exercise> autoGetExercises(String keyWord, Integer number){
        return exerciseDao.autoGetExercises(keyWord, number);
    }
}
