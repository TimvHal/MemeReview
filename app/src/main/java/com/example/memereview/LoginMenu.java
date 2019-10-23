package com.example.memereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.memereview.firebaseService.FirebaseService;
import com.example.memereview.model.User;

public class LoginMenu extends AppCompatActivity {

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
    }

    public void login(View v){
        FirebaseService firebaseService = new FirebaseService();
        firebaseService.getUser(new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                User user = (User) returnedThing;
                if (user == null){
                    Toast.makeText(LoginMenu.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                }
                else if (user.password.equals(password.getText().toString())){
                    Toast.makeText(LoginMenu.this, "Welcome " + user.nickName + "!" , Toast.LENGTH_SHORT).show();
                    goToMainMenu();
                }
                else{
                    Toast.makeText(LoginMenu.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void DataLoadFailed() {
                Log.d("great", "failure");
            }
        }, username.getText().toString());
    }

    public void goToCreateAccount(View v){
        Toast.makeText(LoginMenu.this, "Work in progress", Toast.LENGTH_SHORT).show();
    }

    private void goToMainMenu(){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
