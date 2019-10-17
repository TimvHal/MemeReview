package com.example.memereview.model;

import android.graphics.Bitmap;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String userName;
    public String nickName;
    public String password;
    public Bitmap profilePicture;
    public String[] ownedMemes;

    private static User user;

    public static synchronized User getMainUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    //lege constuctor nodig om firebase object te maken
    public User() {
    }

    //wordt alleen gebruikt om een tijdelijke user aan te maken wanneer er een nieuw account gemaakt wordt.
    public User(String userName, String nickName, String password) {
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
    }

    public void setAll(String userName, String nickName, String password, Bitmap profilePicture, String[] ownedMemes) {
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.profilePicture = profilePicture;
        this.ownedMemes = ownedMemes;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setOwnedMemes(String[] ownedMemes) {
        this.ownedMemes = ownedMemes;
    }

}
