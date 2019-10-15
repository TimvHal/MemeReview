package com.example.memereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.memereview.firebaseService.FirebaseService;
import com.example.memereview.model.User;

import java.io.File;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random r = new Random();
        int randInt = r.nextInt(2) + 1;
        ImageView i = findViewById(R.id.start_emote);

        switch(randInt) {
            case 1:
                i.setImageResource(R.drawable.ok_hand);
                break;

            case 2:
                i.setImageResource(R.drawable.clapping_hands);
        }
    }

    public void startReviewing(View v) {
        new FirebaseService().getUser(new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(User tempUser) {
                User.getInstance().setAll(tempUser.userName, tempUser.nickName, tempUser.password, tempUser.profilePicture, tempUser.ownedMemes);
                Log.d("starters", "nibba" + User.getInstance().userName);
                checkLogin();
            }
        }, "john");

    }

    private void checkLogin(){
        Log.d("starters", "nibba" + User.getInstance().userName);
        Intent intent = new Intent(this, MemePage.class);
        startActivity(intent);
    }
}
