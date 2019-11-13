package com.example.memereview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.memereview.controller.AccountController;
import com.example.memereview.controller.SuperController;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class CreateAccountMenu extends AppCompatActivity {

    private AccountController controller;
    private EditText username;
    private EditText nickname;
    private EditText password;
    private Button createAccountBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_menu);
        controller = SuperController.getInstance().accountController;
        controller.setAccountMenu(this);
        username = findViewById(R.id.caUserName);
        nickname = findViewById(R.id.caNickName);
        password = findViewById(R.id.caPassword);
        createAccountBtn = findViewById(R.id.createAccount);
    }

    public void createAccount(View v) throws NoSuchAlgorithmException {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.clapping_hands);
        createAccountBtn.setEnabled(false);
        controller.createAccount(username.getText().toString(), nickname.getText().toString(), password.getText().toString(), icon);
        createAccountBtn.setEnabled(true);
    }




}
