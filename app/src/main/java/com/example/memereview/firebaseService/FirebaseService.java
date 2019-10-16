package com.example.memereview.firebaseService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.memereview.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

public class FirebaseService {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    public  FirebaseService firebaseService;

    private User user;

    public FirebaseService() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        user = User.getInstance();
    }

    /*public static FirebaseService getInstance(){
        return firebaseService;
    }*/

    public void addUser(String userName, String nickName, String password){
        User user = new User(userName, nickName, password);

        DatabaseReference reference = firebaseDatabase.getReference();

        reference.child("users").child(userName).setValue(user);
    }

    public void getUser(final DataStatus dataStatus, String userName) {
        final String username = userName;
        final DatabaseReference reference = firebaseDatabase.getReference();

        reference.child("users").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public synchronized void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User tempUser = dataSnapshot.getValue(User.class);
                dataStatus.DataIsLoaded(tempUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dataStatus.DataLoadFailed();
            }
        });
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
        User.getInstance().profilePicture = profilePicture;
    }

    public void getProfilePicture(final DataStatus dataStatus, String userName){
        StorageReference profilePictureReference = firebaseStorage.getReference().child("profilePicture").child(userName + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        profilePictureReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap=  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                dataStatus.DataIsLoaded(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                dataStatus.DataLoadFailed();
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

    public interface DataStatus{
        void DataIsLoaded(Object returnedThing);
        void DataLoadFailed();
    }

}
