package com.example.memereview.model;

import android.graphics.Bitmap;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Account {

    public String userName;
    public String nickName;
    public String password;
    public Bitmap profilePicture;
    public String[] ownedMemes;

    public Account(String userName, String nickName, String password) {
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
    }

    public void setProfilePicture(Bitmap profilePicture){
        this.profilePicture = profilePicture;
    }

    public void setOwnedMemes(String[] ownedMemes){
        this.ownedMemes = ownedMemes;
    }
}
