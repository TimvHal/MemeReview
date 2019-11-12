package com.example.memereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

/*        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Oh nee! Beschuldig me daar nooit van!");*/
        startActivity(new Intent(this, LoginMenu.class));
    }
}
