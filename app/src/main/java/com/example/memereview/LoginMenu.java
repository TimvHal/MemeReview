package com.example.memereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.memereview.controller.AccountController;
import com.example.memereview.controller.SuperController;
import com.example.memereview.model.User;
import com.example.memereview.observer.UserObservable;
import com.example.memereview.observer.UserObserver;

public class LoginMenu extends AppCompatActivity implements UserObserver {

    EditText username;
    EditText password;
    AccountController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
        username = findViewById(R.id.caUserName);
        password = findViewById(R.id.caPassword);
        controller = SuperController.getInstance().accountController;
        controller.registerObserver(this);
    }

    public void login(View v){
        controller.logInUser(username.getText().toString());
    }

    @Override
    public void update(UserObservable uo) {
        User user = (User) uo;
        if (user == null){
            Toast.makeText(LoginMenu.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
        }
        else{
            boolean succes = controller.logUserIn(user, password.getText().toString());
            if (succes){
                goToMainMenu();
            }else{
                Toast.makeText(LoginMenu.this, "Wrong password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goToMainMenu(){
        Intent intent = new Intent(this, NavActivity.class);
        startActivity(intent);
    }

    public void goToCreateAccount(View v){
        Intent intent = new Intent(this, CreateAccountMenu.class);
        startActivity(intent);
    }
}
