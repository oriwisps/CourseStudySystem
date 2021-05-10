package com.zjj.coursestudy.service;

import com.zjj.coursestudy.entity.Discussion;
import com.zjj.coursestudy.entity.User;

import java.util.List;

public interface DiscussionService {

    List<Discussion> findBySender(User sender);

    List<Discussion> findByReceiver(User receiver);

    Discussion saveDiscussion(Discussion discussion);

    Discussion getDiscussionByID(int id);
}
