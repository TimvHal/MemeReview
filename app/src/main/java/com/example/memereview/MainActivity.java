package com.example.memereview;

import androidx.appcompat.app.AppCompatActivity;

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
        firebaseService = new FirebaseService();
        firebaseService.getMemeReferences("fresh", MemeReference.getInstance().freshList);
        firebaseService.getMemeReferences("john", User.getMainUser().ownedMemes);
    }

    public void startReviewing(View v) throws ExecutionException, InterruptedException {
        firebaseService.rateMeme("3.jpg", 5);

    }
        /*ImageView imageView = findViewById(R.id.start_emote);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        firebaseService.uploadMeme("john", bitmap);

        firebaseService.getUser(new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                User tempuser = (User) returnedThing;
            }

            @Override
            public void DataLoadFailed() {

            }
        }, "john");
    }*/

//        final boolean userDone = false;
//        boolean imgDone = false;
//
//        new FirebaseService().getUser(new FirebaseService.DataStatus() {
//            @Override
//            public void DataIsLoaded(Object returnedThing) {
//                User tempUser = (User) returnedThing;
//                User.getInstance().setAll(tempUser.userName, tempUser.nickName, tempUser.password, tempUser.profilePicture, tempUser.ownedMemes);
//                checkLogin();
//            }
//
//            @Override
//            public void DataLoadFailed() {
//                Log.d("helaas", "het werkte niet doe nu dingen");
//            }
//        }, "john");
//
//        new FirebaseService().getProfilePicture(new FirebaseService.DataStatus() {
//            @Override
//            public void DataIsLoaded(Object returnedThing) {
//                Bitmap profilePicture = (Bitmap) returnedThing;
//                User.getInstance().setProfilePicture(profilePicture);
//                checkLogin();
//            }
//
//            @Override
//            public void DataLoadFailed() {
//                Log.d("helaas", "het werkte niet doe nu dingen");
//            }
//        }, "john");
//    }
//
//    private void checkLogin(){
//        Log.d("deze man", "ewa");
//        if (User.getInstance().userName == null || User.getInstance().profilePicture == null){
//            return;
//        }
//        Log.d("starters", "nibba" + User.getInstance().userName);
//        Intent intent = new Intent(this, MemePage.class);
//        startActivity(intent);
//    }
}
