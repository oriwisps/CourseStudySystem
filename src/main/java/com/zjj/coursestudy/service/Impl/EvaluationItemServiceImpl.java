package com.zjj.coursestudy.service.Impl;

import com.zjj.coursestudy.dao.EvaluationItemDao;
import com.zjj.coursestudy.dao.ScoreDao;
import com.zjj.coursestudy.entity.EvaluationItem;
import com.zjj.coursestudy.service.EvaluationItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvaluationItemServiceImpl implements EvaluationItemService {

    @Autowired
    private EvaluationItemDao evaluationItemDao;

    @Autowired
    private ScoreDao scoreDao;

    public EvaluationItem getItemByID(int ID){
        return evaluationItemDao.getOne(ID);
    }

    public boolean deleteItem(int ID){
        evaluationItemDao.deleteById(ID);
        if(!evaluationItemDao.existsById(ID)){
            return true;
        }
        return false;
    }
}
