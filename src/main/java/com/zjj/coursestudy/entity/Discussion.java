package com.zjj.coursestudy.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "discussion")
public class Discussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @Column(name = "content")
    private String content;

    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @OneToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @OneToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @OneToOne
    @JoinColumn(name = "reply_to")
    private Discussion replyTo;

    @Column(name = "sender_visible")
    private boolean senderVisible;

    @Column(name = "receiver_visible")
    private boolean receiverVisible;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Discussion getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Discussion replyTo) {
        this.replyTo = replyTo;
    }

    public boolean isSenderVisible() {
        return senderVisible;
    }

    public void setSenderVisible(boolean senderVisible) {
        this.senderVisible = senderVisible;
    }

    public boolean isReceiverVisible() {
        return receiverVisible;
    }

    public void setReceiverVisible(boolean receiverVisible) {
        this.receiverVisible = receiverVisible;
    }
}
