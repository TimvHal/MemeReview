package com.example.memereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.memereview.firebaseService.FirebaseService;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UploadMenu extends AppCompatActivity {

    private Bitmap bitmap;
    private ImageView imageView;
    private Button galleryButton;
    private Button uploadButton;
    private int gallery = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_menu);
        imageView = findViewById(R.id.memeView);
        galleryButton  = findViewById(R.id.chooseMeme);
        uploadButton = findViewById(R.id.uploadMeme);
    }

    public void openGallery(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, gallery);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == gallery){
            if (data != null){
                Uri contentURI = data.getData();
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    Toast.makeText(UploadMenu.this, "Now look at that epic meme!", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);

                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(UploadMenu.this, "OOF! Something went wrong!", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

    public void uploadMeme(View v){
        if (bitmap == null){
            Toast.makeText(UploadMenu.this, "Where is your meme?", Toast.LENGTH_SHORT).show();
            return;
        }
        galleryButton.setEnabled(false);
        uploadButton.setEnabled(false);
        FirebaseService firebaseService = new FirebaseService();
        firebaseService.uploadMeme("john", bitmap, new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                Toast.makeText(UploadMenu.this, "YAY, your meme has been uploaded!", Toast.LENGTH_SHORT).show();
                bitmap = null;
                imageView.setImageResource(0);
                galleryButton.setEnabled(true);
                uploadButton.setEnabled(true);

            }

            @Override
            public void DataLoadFailed() {
                Toast.makeText(UploadMenu.this, "OOF! Something went wrong!", Toast.LENGTH_SHORT).show();
                galleryButton.setEnabled(true);
                uploadButton.setEnabled(true);
            }
        });
    }


}
