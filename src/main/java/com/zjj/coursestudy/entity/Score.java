package com.zjj.coursestudy.entity;

import javax.persistence.*;

@Entity
@Table(name = "score")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @Column(name = "score")
    private double score;

    @OneToOne
    @JoinColumn(name = "evaluation_item_id", nullable = false)
    private EvaluationItem evaluationItem;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public EvaluationItem getEvaluationItem() {
        return evaluationItem;
    }

    public void setEvaluationItem(EvaluationItem evaluationItem) {
        this.evaluationItem = evaluationItem;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
