package com.zjj.coursestudy.vo;

import com.zjj.coursestudy.entity.Answer;
import com.zjj.coursestudy.entity.Exercise;
import com.zjj.coursestudy.entity.KeyWord;

public class StuExerciseVo {

    private Integer answerID;
    private Integer exerciseID;
    private String answerContent;
    private String exerciseContent;
    private String answer;
    private String analysis;
    private Integer keywordID;
    private boolean submit;

    public StuExerciseVo(Answer answer){
        this.answerID = answer.getID();
        this.exerciseID = answer.getExercise().getID();
        this.answerContent = answer.getContent();
        this.exerciseContent = answer.getExercise().getContent();
        this.answer = answer.getExercise().getAnswer();
        this.analysis = answer.getExercise().getAnalysis();
        this.submit = answer.isSubmit();
        this.keywordID = answer.getKeyWord().getID();
    }

    public StuExerciseVo(Exercise exercise, KeyWord keyWord){
        this.exerciseID = exercise.getID();
        this.exerciseContent = exercise.getContent();
        this.answer = exercise.getAnswer();
        this.analysis = exercise.getAnalysis();
        this.submit = false;
        this.keywordID = keyWord.getID();
    }

    public Integer getAnswerID() {
        return answerID;
    }

    public void setAnswerID(Integer answerID) {
        this.answerID = answerID;
    }

    public Integer getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(Integer exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getExerciseContent() {
        return exerciseContent;
    }

    public void setExerciseContent(String exerciseContent) {
        this.exerciseContent = exerciseContent;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }

    public Integer getKeywordID() {
        return keywordID;
    }

    public void setKeywordID(Integer keywordID) {
        this.keywordID = keywordID;
    }
}
