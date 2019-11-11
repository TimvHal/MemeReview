package com.example.memereview.controller;

import android.util.Log;
import android.widget.Toast;

import com.example.memereview.CreateAccountMenu;
import com.example.memereview.firebaseService.FirebaseService;
import com.example.memereview.model.User;
import com.example.memereview.observer.UserObserver;

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
                    user.setAll(temp.userName, temp.nickName, temp.password, temp.profilePicture, temp.ownedMemes);
                }
                user.notifyAllObservers();
            }
            @Override
            public void DataLoadFailed() {
                Log.d("great", "failure");
            }
        }, username);
    }

    public synchronized void createAccount(String userName, String nickName, String password){
        firebaseService.addUser(userName, nickName, password, new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                Boolean result = (boolean) returnedThing;
                showResult(result);
            }
            @Override
            public void DataLoadFailed() {
            }
        });
    }

    private synchronized void showResult(boolean succes){
        if (succes){
            Toast.makeText(accountMenu, "Account created!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(accountMenu, "Account already exists", Toast.LENGTH_SHORT).show();
        }
    }

}
