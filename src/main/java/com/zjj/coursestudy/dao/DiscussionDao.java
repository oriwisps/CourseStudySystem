package com.zjj.coursestudy.dao;

import com.zjj.coursestudy.entity.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussionDao extends JpaRepository<Discussion, Integer> {
}
