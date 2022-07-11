package com.example.myapplication;

import android.app.Application;

public class GlobalId extends Application {
    private String id;

    public String getGlobalId() {
        return id;
    }
    public void setGlobalId(String id) {
        this.id = id;
    }
}
