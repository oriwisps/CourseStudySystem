package com.zjj.coursestudy.service;

import com.zjj.coursestudy.entity.EvaluationItem;

public interface EvaluationItemService {

    EvaluationItem getItemByID(int ID);

    boolean deleteItem(int ID);
}
