package com.zjj.coursestudy.vo;

import com.zjj.coursestudy.entity.EvaluationItem;

public class EvaluationItemVo {

    private String itemid;
    private String itemname;
    private String description;
    private double proportion;

    public EvaluationItemVo(EvaluationItem evaluationItem){
        itemid = String.valueOf(evaluationItem.getID());
        itemname = evaluationItem.getName();
        description = evaluationItem.getDescription();
        proportion = evaluationItem.getProportion();
    }

    public EvaluationItem createEvaluationItem(){
        EvaluationItem evaluationItem = new EvaluationItem();
        if(itemid != null){
            evaluationItem.setID(Integer.valueOf(itemid));
        }
        evaluationItem.setName(itemname);
        evaluationItem.setDescription(description);
        evaluationItem.setProportion(proportion);
        return evaluationItem;
    }

    public String getItemID() {
        return itemid;
    }

    public void setItemID(String itemID) {
        this.itemid = itemID;
    }

    public String getItemName() {
        return itemname;
    }

    public void setItemName(String itemName) {
        this.itemname = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

}
