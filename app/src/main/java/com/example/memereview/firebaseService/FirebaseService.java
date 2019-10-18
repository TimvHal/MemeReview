package com.example.memereview.firebaseService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.memereview.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class FirebaseService {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    public FirebaseService() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

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
        User.getMainUser().profilePicture = profilePicture;
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

    public void uploadMeme(final String userName , final Bitmap meme){
        DatabaseReference amountOfMemesRef = firebaseDatabase.getReference().child("memeCounter");
        amountOfMemesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("antwoord", "'"+ dataSnapshot.getValue());
                Long amountOfMemes = (Long) dataSnapshot.getValue();
                uploadMemeToStorage(amountOfMemes, meme, userName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadMemeToStorage(final long name, Bitmap meme, final String userName){
        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("amountRated", "" + 0).setCustomMetadata("rating", ""+0).build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        meme.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference reference = firebaseStorage.getReference();
        StorageReference memesLocation = reference.child("memes").child(name + ".jpg");

        UploadTask uploadTask = memesLocation.putBytes(data, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("meme", "failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadMemeReference(name, userName);
            }
        });
    }

    private void uploadMemeReference(long name, String userName){
        DatabaseReference reference = firebaseDatabase.getReference();

        DatabaseReference userMemeReference = reference.child("users").child(userName).child("memes");
        String key = userMemeReference.push().getKey();
        DatabaseReference keyReference = userMemeReference.child(key);
        keyReference.setValue(name +".jpg");

        DatabaseReference freshReference = reference.child("fresh");
        String freshKey = freshReference.push().getKey();
        freshReference.child(freshKey).setValue(name + ".jpg");

        DatabaseReference amountRef = reference.child("memeCounter");
        amountRef.setValue(name + 1);
    }

    public void getMemeReferences(final String location, final ArrayList<String> list){
        DatabaseReference reference;
        if (location == "fresh" || location == "hot"){
            reference = firebaseDatabase.getReference().child(location);
        }else{
            reference = firebaseDatabase.getReference().child("users").child(location).child("memes");
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                int timesLooped = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    timesLooped++;
                    if (dataSnapshot.getChildrenCount() - timesLooped > 50 ) {
                        continue;
                    }else{
                        String data = snapshot.getValue(String.class);
                        ArrayList<String> tempList = new ArrayList();
                        tempList.add(data);
                    }
                }
                Collections.reverse(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getMeme(final DataStatus dataStatus, String name){
        StorageReference memeReference = firebaseStorage.getReference().child("memes").child(name);

        final long ONE_MEGABYTE = 1024 * 1024;
        memeReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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

    public void rateMeme(final String location, final int rating){
        StorageReference storageReference = firebaseStorage.getReference();
        final StorageReference memeReference = storageReference.child("memes").child(location);
        memeReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                double amount = Double.parseDouble(storageMetadata.getCustomMetadata("amountRated"));
                double currentRating =  Double.parseDouble(storageMetadata.getCustomMetadata("rating"));
                Log.d("aids", " " + storageMetadata.getCreationTimeMillis());
                double newAmount = amount + 1;
                double newRating = (((currentRating * amount) + rating) / newAmount);
                double cleanNewRating = Math.round(newRating * 100.0) / 100.0;
                StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("amountRated", "" + newAmount).setCustomMetadata("rating", ""+cleanNewRating).build();
                memeReference.updateMetadata(metadata);
                if(cleanNewRating > 7 && newAmount > 10){
                    addMemeToHot(location);
                }
            }
        });
    }

    private void addMemeToHot(String location){
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference hotReference = databaseReference.child("hot");
        String key = hotReference.push().getKey();
        DatabaseReference keyReference = hotReference.child(key);
        keyReference.setValue(location);
    }

    public interface DataStatus{
        void DataIsLoaded(Object returnedThing);
        void DataLoadFailed();
    }

}
