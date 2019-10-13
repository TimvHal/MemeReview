package com.example.memereview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginMenu extends AppCompatActivity {

    private EditText mailField;
    private EditText passwordField;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
        mailField = findViewById(R.id.mailField);
        passwordField = findViewById(R.id.passwordField);
        Button loginButton = findViewById(R.id.loginButton);
    }

    public void login(View v){
        String username = mailField.getText().toString();
        String password = passwordField.getText().toString();
        boolean usernameEmpty = true;
        boolean passwordEmpty = true;
        if (!username.isEmpty()){
            usernameEmpty = false;
        }
        if (!password.isEmpty()){
            passwordEmpty = false;
        }

        if (usernameEmpty && !passwordEmpty){
            Toast.makeText(LoginMenu.this, "Please enter a username", Toast.LENGTH_SHORT).show();
        }
        else if (!usernameEmpty && passwordEmpty){
            Toast.makeText(LoginMenu.this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }
        else if (usernameEmpty && passwordEmpty){
            Toast.makeText(LoginMenu.this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(LoginMenu.this, "Logging in", Toast.LENGTH_SHORT).show();
            //Firebase dingen
        }
    }
}
