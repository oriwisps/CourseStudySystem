package com.zjj.coursestudy.vo;

import com.zjj.coursestudy.entity.KeyWord;

public class KeyWordVo {

    private Integer keyWordID;
    private String keyWordName;
    private String description;

    public KeyWordVo (KeyWord keyWord){
        keyWordID = keyWord.getID();
        keyWordName = keyWord.getName();
        description = keyWord.getDescription();
    }

    public Integer getKeyWordID() {
        return keyWordID;
    }

    public void setKeyWordID(Integer keyWordID) {
        this.keyWordID = keyWordID;
    }

    public String getKeyWordName() {
        return keyWordName;
    }

    public void setKeyWordName(String keyWordName) {
        this.keyWordName = keyWordName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
