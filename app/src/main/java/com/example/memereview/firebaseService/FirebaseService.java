package com.example.memereview.firebaseService;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.memereview.R;
import com.example.memereview.model.Meme;
import com.example.memereview.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.FormatFlagsConversionMismatchException;

public class FirebaseService {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    public FirebaseService() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public void addUser(final String userName, String nickName, String password, String salt, final DataStatus dataStatus, final Bitmap icon){
        //The value '2131230877' is the id for the default radiobutton.
        final User user = new User(userName, nickName, password, salt, "2131230877", "0", "1.0", "RED");
        DatabaseReference reference = firebaseDatabase.getReference();
        final DatabaseReference userReference = reference.child("users").child(userName);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    dataStatus.DataIsLoaded(false);
                }
                else{
                    userReference.setValue(user);
                    addProfilePicture(userName, icon);
                    dataStatus.DataIsLoaded(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    dataStatus.DataLoadFailed();
            }
        });
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

    public void uploadMeme(final String userName , final Bitmap meme, final DataStatus dataStatus){
        DatabaseReference amountOfMemesRef = firebaseDatabase.getReference().child("memeCounter");
        amountOfMemesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("antwoord", "'"+ dataSnapshot.getValue());
                Long amountOfMemes = (Long) dataSnapshot.getValue();
                uploadMemeToStorage(amountOfMemes, meme, userName);
                dataStatus.DataIsLoaded(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dataStatus.DataLoadFailed();
            }
        });
    }

    private void uploadMemeToStorage(final long name, Bitmap meme, final String userName){
        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("amountRated", "" + 0).setCustomMetadata("rating", ""+0).setCustomMetadata("creator", userName).build();

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

    public void getMemeReferences(final String location, final DataStatus dataStatus) {

        DatabaseReference reference;
        if (location == "fresh" || location == "hot") {
            reference = firebaseDatabase.getReference().child(location);
        } else {
            reference = firebaseDatabase.getReference().child("users").child(location).child("memes");
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> memeReferences = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    memeReferences.add(ds.getValue(String.class));
                }
                while (memeReferences.size() > 15){
                    memeReferences.remove(0);
                }
                dataStatus.DataIsLoaded(memeReferences);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dataStatus.DataLoadFailed();
            }
        });

    }

    public void getMeme(final DataStatus dataStatus, final String name){
        final StorageReference memeReference = firebaseStorage.getReference().child("memes").child(name);

        final long ONE_MEGABYTE = 1024 * 1024;
        memeReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap=  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Meme meme = new Meme();
                meme.setMemeImage(bitmap);
                meme.setName(name);
                getMemeMetadata(dataStatus, memeReference, meme);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                dataStatus.DataLoadFailed();
            }
        });
    }

    public void getMemeMetadata(final DataStatus dataStatus, StorageReference reference, final Meme meme){
        reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                meme.setAmountRated(storageMetadata.getCustomMetadata("amountRated"));
                meme.setAverageRating(storageMetadata.getCustomMetadata("rating"));
                String creatorName = storageMetadata.getCustomMetadata("creator");
                getCreatorNickname(dataStatus, meme, creatorName);
            }
        });
    }

    private void getCreatorNickname(final DataStatus dataStatus, final Meme meme, final String creatorName){
        DatabaseReference userNicknameReference = firebaseDatabase.getReference().child("users").child(creatorName).child("nickName");
        userNicknameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meme.setCreator(dataSnapshot.getValue(String.class));
                getCreatorProfilePicture(dataStatus, meme, creatorName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getCreatorProfilePicture(final DataStatus dataStatus, final Meme meme, String creatorName){
        StorageReference profilePictureReference = firebaseStorage.getReference().child("profilePicture").child(creatorName + ".jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        profilePictureReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap=  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                meme.setUserAvatar(bitmap);
                dataStatus.DataIsLoaded(meme);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
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

    public void addMemeToHot(String location){
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

    public void enableBottomBar(BottomNavigationView mBottomMenu, boolean enable){
        for (int i = 0; i < mBottomMenu.getMenu().size(); i++) {
            mBottomMenu.getMenu().getItem(i).setEnabled(enable);
        }
    }

}
