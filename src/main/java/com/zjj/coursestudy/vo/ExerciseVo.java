package com.zjj.coursestudy.vo;

import com.zjj.coursestudy.entity.Exercise;

public class ExerciseVo {

    private Integer exerciseID;
    private String content;
    private String answer;
    private String analysis;

    public ExerciseVo(Exercise exercise){
        exerciseID = exercise.getID();
        content = exercise.getContent();
        analysis = exercise.getAnalysis();
        answer = exercise.getAnswer();
    }

    public Integer getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(Integer exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
