package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.Discussion;
import com.zjj.coursestudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionDao extends JpaRepository<Discussion, Integer> {

    List<Discussion> findDiscussionsBySenderOrderByTime(User render);

    List<Discussion> findDiscussionsByReceiverOrderByTime(User receiver);
}
