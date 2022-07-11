package com.example.myapplication;

import java.util.List;

public class MatchingData {
    private String waiterid;
    private String userid;
    private String roomnumber;

    public MatchingData(String waiterid, String userid, String roomnumber) {
        this.waiterid = waiterid;
        this.userid = userid;
        this.roomnumber = roomnumber;
    }
    public String getWaiterid() {
        return waiterid;
    }
    public String getRoomnumber() {
        return roomnumber;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public void setWaiterid(String waiterid) {
        this.waiterid = waiterid;
    }
    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }
}
