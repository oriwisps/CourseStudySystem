package com.zjj.coursestudy.vo;

import com.zjj.coursestudy.entity.Score;

public class ScoreVo {

    private int scoreID;
    private int itemID;
    private String itemName;
    private String courseName;
    private double proportion;
    private String description;
    private double score;

    public ScoreVo(Score score){
        this.itemID = score.getEvaluationItem().getID();
        this.scoreID = score.getID();
        this.itemName = score.getEvaluationItem().getName();
        this.courseName = score.getEvaluationItem().getCourse().getName();
        this.proportion = score.getEvaluationItem().getProportion();
        this.score = score.getScore();
        this.description = score.getEvaluationItem().getDescription();
    }

    public int getScoreID() {
        return scoreID;
    }

    public void setScoreID(int scoreID) {
        this.scoreID = scoreID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}
