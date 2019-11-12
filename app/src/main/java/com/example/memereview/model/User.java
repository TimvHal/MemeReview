package com.example.memereview.model;

import android.graphics.Bitmap;

import com.example.memereview.observer.UserObservable;
import com.example.memereview.observer.UserObserver;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User implements UserObservable{

    public String userName;
    public String nickName;
    public String password;
    public Bitmap profilePicture;
    public ArrayList<String> ownedMemes;
    public String salt;

    private static User user;

    List<UserObserver> observers = new ArrayList<>();

    public static synchronized User getMainUser() {
        if (user == null) {
            user = new User("niks");
        }
        return user;
    }

    //lege constuctor nodig om firebase object te maken
    public User() {
    }

    public User(String niks){
        ownedMemes = new ArrayList<>();
    }

    //wordt alleen gebruikt om een tijdelijke user aan te maken wanneer er een nieuw account gemaakt wordt.
    public User(String userName, String nickName, String password, String salt) {
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.salt = salt;
    }

    public void setAll(String userName, String nickName, String password, Bitmap profilePicture, ArrayList<String> ownedMemes, String salt) {
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.profilePicture = profilePicture;
        this.ownedMemes = ownedMemes;
        this.salt = salt;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setOwnedMemes(ArrayList<String> ownedMemes) {
        this.ownedMemes = ownedMemes;
    }

    @Override
    public void register(UserObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyAllObservers() {
        for (UserObserver observer: observers){
            observer.update(this);
        }
    }
}
