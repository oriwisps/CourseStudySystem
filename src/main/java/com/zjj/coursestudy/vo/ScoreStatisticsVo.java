package com.zjj.coursestudy.vo;

import java.util.LinkedList;
import java.util.List;

public class ScoreStatisticsVo {

    private List<String> labels;
    private List<AveMedVo> datasets;

    public ScoreStatisticsVo(){
        labels = new LinkedList<>();
        datasets = new LinkedList<>();
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<AveMedVo> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<AveMedVo> datasets) {
        this.datasets = datasets;
    }
}
