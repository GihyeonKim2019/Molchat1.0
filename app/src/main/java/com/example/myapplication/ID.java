package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class ID {
    @SerializedName("ID")
    private Integer ID;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}