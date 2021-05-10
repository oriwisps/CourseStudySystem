package com.zjj.coursestudy.vo;

import java.util.LinkedList;
import java.util.List;

public class AveMedVo {

    private String label;
    private List<Integer> data;


    public AveMedVo(String label){
        this.label = label;
        data = new LinkedList<>();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
