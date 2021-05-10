package com.zjj.coursestudy.service.Impl;

import com.zjj.coursestudy.dao.DiscussionDao;
import com.zjj.coursestudy.entity.Discussion;
import com.zjj.coursestudy.entity.User;
import com.zjj.coursestudy.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussionServiceImpl implements DiscussionService {

    @Autowired
    private DiscussionDao discussionDao;

    public List<Discussion> findBySender(User sender){
        return discussionDao.findDiscussionsBySenderOrderByTime(sender);
    }

    public List<Discussion> findByReceiver(User receiver){
        return discussionDao.findDiscussionsByReceiverOrderByTime(receiver);
    }

    public Discussion saveDiscussion(Discussion discussion){
        return discussionDao.save(discussion);
    }

    public  Discussion getDiscussionByID(int id){
        return discussionDao.getOne(id);
    }
}
