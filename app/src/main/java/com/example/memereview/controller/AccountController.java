package com.example.memereview.controller;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.memereview.CreateAccountMenu;
import com.example.memereview.firebaseService.FirebaseService;
import com.example.memereview.model.User;
import com.example.memereview.observer.UserObserver;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AccountController {

    private User user;
    private FirebaseService firebaseService;
    private CreateAccountMenu accountMenu;

    public AccountController(){
        firebaseService = new FirebaseService();
        user = new User();
    }

    public void registerObserver(UserObserver userObserver){
        user.register(userObserver);
    }

    public void setAccountMenu(CreateAccountMenu accountMenu){ this.accountMenu = accountMenu;}

    public void logInUser(String username){
        firebaseService.getUser(new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                User temp = (User) returnedThing;
                if (temp != null){
                    user.setAll(temp.userName, temp.nickName, temp.password, temp.profilePicture, temp.ownedMemes, temp.salt);
                    user.setPreferences(temp.gender, temp.age, temp.audioVolume, temp.theme);
                }
                user.notifyAllObservers();
            }
            @Override
            public void DataLoadFailed() {
                Log.d("great", "failure");
            }
        }, username);
    }

    public synchronized void createAccount(String userName, String nickName, String password, Bitmap icon) throws NoSuchAlgorithmException {
        byte[] salt = generateSalt();
        String hashedPassword = generateSecurePassword(password,salt);
        Log.d("wachtwoord", salt + "");

        firebaseService.addUser(userName, nickName, hashedPassword, salt.toString(), new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                Boolean result = (boolean) returnedThing;
                showResult(result);
            }
            @Override
            public void DataLoadFailed() {
            }
        }, icon);
    }

    private synchronized void showResult(boolean succes){
        if (succes){
            Toast.makeText(accountMenu, "Account created!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(accountMenu, "Account already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private String generateSecurePassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            //md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public boolean validatePassword(User user, String password){
        String hashedPassword = null;
        //byte[] salt = user.salt.getBytes();
        //Log.d("wachtwoord", salt + "");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            //md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        Log.d("wachtwoord", user.password + " " + hashedPassword);
        if (hashedPassword.equals(user.password)){
            return true;
        }
        else{
            return false;
        }
    }

    public User getUser(){
        return this.user;
    }

    public void removeUser(){this.user = null;}

}
