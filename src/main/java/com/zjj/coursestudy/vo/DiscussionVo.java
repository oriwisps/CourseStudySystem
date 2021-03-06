package com.zjj.coursestudy.vo;

import com.zjj.coursestudy.entity.Discussion;

import java.util.Date;

public class DiscussionVo {

    private int discussionID;
    private String content;
    private Date time;
    private String senderName;
    private String receiverName;

    public DiscussionVo(Discussion discussion){
        discussionID = discussion.getID();
        content = discussion.getContent();
        time = discussion.getTime();
        senderName = discussion.getSender().getName();
        receiverName = discussion.getReceiver().getName();
    }

    public int getDiscussionID() {
        return discussionID;
    }

    public void setDiscussionID(int discussionID) {
        this.discussionID = discussionID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
