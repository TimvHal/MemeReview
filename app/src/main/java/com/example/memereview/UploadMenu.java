package com.example.memereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.memereview.firebaseService.FirebaseService;

import java.io.IOException;

public class UploadMenu extends AppCompatActivity {

    ImageView imageView;
    private int gallery = 1;
    private Bitmap meme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_menu);
        imageView = findViewById(R.id.uploadMemeView);
    }

    public void chooseMemeFromGallery(View v){
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    meme = bitmap;
                    Toast.makeText(UploadMenu.this, "Now look at that epic meme!", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);

                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(UploadMenu.this, "OOF! Something went wrong!", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

    public void uploadMemeToFirebase(View v){
        FirebaseService firebaseService = new FirebaseService();
        firebaseService.sendMeme(meme);
    }


}
