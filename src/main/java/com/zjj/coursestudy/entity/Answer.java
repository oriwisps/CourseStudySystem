package com.zjj.coursestudy.entity;

import javax.persistence.*;

@Entity
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @Column(name = "content")
    private String content;

    /**
     * 是否已提交  false表示答案仅保存 true表示答案已提交
     */
    @Column(name = "submit")
    private boolean submit;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @OneToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @OneToOne
    @JoinColumn(name = "key_word_id", nullable = false)
    private KeyWord keyWord;

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

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }

    public int getExerciseID(){
        return this.exercise.getID();
    }

    public KeyWord getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(KeyWord keyWord) {
        this.keyWord = keyWord;
    }
}
