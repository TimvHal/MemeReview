package com.example.memereview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends AppCompatActivity {
    private int gallery;
    private ImageButton avatar;
    private StorageReference mStorageRef;
    StorageReference avatarRef;
    private Uri contentURI;
    private Bitmap newAvatar;
    private Bitmap oldAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        gallery = 1;
        avatar = findViewById(R.id.avatar);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        avatarRef = mStorageRef.child("/avatar/Michiel");
        applyPersonalChanges();
    }

    public void uploadNewAvatar(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, gallery);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == this.RESULT_CANCELED) {
            return;
        }

        if(requestCode == gallery) {
            if(data != null) {
                contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    newAvatar = Bitmap.createScaledBitmap(bitmap, avatar.getWidth(), avatar.getHeight(), true);
                    avatar.setImageBitmap(newAvatar);
                }
                catch(Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public void saveChanges(View v) {
        if(contentURI != null) {
            avatarRef.putFile(contentURI);
        }
    }

    public void applyPersonalChanges() {
        avatarRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(Uri uri) {

                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(uri)
                                .listener(new RequestListener<Bitmap>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                        Bitmap resized = Bitmap.createScaledBitmap(resource, avatar.getWidth(), avatar.getHeight(), true);
                                        avatar.setImageBitmap(resized);
                                        return false;
                                    }
                                }).submit();
                    }
                });
    }
}
