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
import com.example.memereview.model.MemeReference;
import com.example.memereview.model.User;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    FirebaseService firebaseService;

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

    public void startReviewing(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(this, LoginMenu.class);
        startActivity(intent);
    }
}
