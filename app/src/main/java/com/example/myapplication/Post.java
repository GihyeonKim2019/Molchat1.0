package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("ID")
    private Integer ID;
    @SerializedName("Name")
    private String Name;
    @SerializedName("Userid")
    private String Userid;
    @SerializedName("Password")
    private String Password;

    /* 프로필에서 사용
    @SerializedName("Image")
    private String Image;
    @SerializedName("Time")
    private String password;
    */

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getUserid() {
        return Userid;
    }

    public String getPassword() {
        return Password;
    }

    /* 프로필에서 사용
    public String getImage() {
        return Image;
    }

    public String getTime() {
        return Time;
    }
     */

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setUserid(String Userid) {
        this.Userid = Userid;
    }

    public void setPassword(String Passowrd) {
        this.Password = Password;
    }

    /* 프로필에서 사용
    public void setImage(String Image) {
        this.Image = Image;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }
     */

}