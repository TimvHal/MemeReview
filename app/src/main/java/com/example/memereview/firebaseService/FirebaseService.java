package com.example.memereview.firebaseService;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.memereview.model.Account;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class FirebaseService {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    public  FirebaseService firebaseService;

    public FirebaseService() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    /*public static FirebaseService getInstance(){
        return firebaseService;
    }*/

    public void addUser(String userName, String nickName, String password){
        Account account = new Account(userName, nickName, password);

        DatabaseReference reference = firebaseDatabase.getReference();

        reference.child("users").child(userName).setValue(account);
    }

    public void addProfilePicture(String userName, Bitmap profilePicture){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        profilePicture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference reference = firebaseStorage.getReference();
        StorageReference profilePicturesLocation = reference.child("profilePicture");
        StorageReference userProfilePicture = profilePicturesLocation.child(userName + ".jpg");

        UploadTask uploadTask = userProfilePicture.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("pf", "failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("pf", "succes");
            }
        });
    }

    public void uploadMeme(String userName, Bitmap meme){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        meme.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference reference = firebaseStorage.getReference();
        StorageReference memesLocation = reference.child("memes").child(".jpg");
    }


}
