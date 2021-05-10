package com.zjj.coursestudy.vo;
import com.zjj.coursestudy.entity.Score;
import com.zjj.coursestudy.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CourseScoreVo {

    private String studentID;
    private String studentName;
    private int totalScore;
    private int rank;
    private List<ScoreVo> scores;

    public CourseScoreVo(User student, List<Score> scores){
        totalScore = 0;
        this.scores = new ArrayList<>();
        studentName = student.getName();
        studentID = student.getID();
        for (Score score: scores) {
            this.scores.add(new ScoreVo(score));
            totalScore += score.getScore() * score.getEvaluationItem().getProportion();
        }
    }


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<ScoreVo> getScores() {
        return scores;
    }

    public void setScores(List<ScoreVo> scores) {
        this.scores = scores;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
}
