package com.example.memereview.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.memereview.R;
import com.example.memereview.controller.AccountController;
import com.example.memereview.controller.SuperController;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private int gallery;
    private ImageButton avatar;
    private Button saveChanges;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usernameRef;
    private StorageReference mStorageRef;
    private StorageReference avatarRef;
    private AccountController controller;
    private Uri contentURI;
    private Bitmap newAvatar;
    private SeekBar ageSeekBar;
    private TextView ageCounter;
    private EditText usernameField;
    private RadioGroup genderBoxes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controller = SuperController.getInstance().accountController;
        gallery = 1;
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        avatar = root.findViewById(R.id.avatar);
        saveChanges = root.findViewById(R.id.savechanges);
        usernameField = root.findViewById(R.id.usernameField);
        genderBoxes = root.findViewById(R.id.genderBoxes);

        firebaseDatabase = FirebaseDatabase.getInstance();
        usernameRef = firebaseDatabase
                .getReference("users/" + controller.getUser().userName)
                .child("userName");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        avatarRef = mStorageRef.child("/avatar/" + controller.getUser().userName);

        saveChanges.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveProfileChanges(v);
            }

        });
        avatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadNewProfilePicture(v);
            }

        });
        ageSeekBar = root.findViewById(R.id.ageSeekBar);
        ageCounter = root.findViewById(R.id.ageCounter);

        ageSeekBar.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
        ageSeekBar.setThumbTintList(ColorStateList.valueOf(Color.parseColor("#660000")));
        ageSeekBar.setMax(100);

        ageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ageCounter.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        applyPersonalChanges(root);
        return root;
    }

    public void uploadNewProfilePicture(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, gallery);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_CANCELED) {
            return;
        }

        if(requestCode == gallery) {
            if(data != null) {
                contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
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

    public void saveProfileChanges(View v) {
        if(contentURI != null) {
            avatarRef.putFile(contentURI);
        }
        usernameRef.setValue(usernameField.getText().toString());
        String filename = "user_information";
        String fileContents = genderBoxes.getCheckedRadioButtonId() + "\n" + ageCounter.getText();
        FileOutputStream outputStream;

        try {
            outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void applyPersonalChanges(View root) {
        avatarRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getActivity().getApplicationContext())
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
        if(new File(getActivity().getApplicationContext().getFilesDir(), "personal_settings").exists()) {
            try {
                FileInputStream fileInputStream = getActivity().getApplicationContext().openFileInput("user_information");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                usernameField.setText(controller.getUser().nickName);
                RadioButton r = root.findViewById(Integer.parseInt(bufferedReader.readLine()));
                r.setChecked(true);
                String currentAge = bufferedReader.readLine();
                ageSeekBar.setProgress(Integer.parseInt(currentAge));
                ageCounter.setText(currentAge);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            usernameField.setText("User");
            ageSeekBar.setProgress(0);
            ageCounter.setText(0);
        }


    }


}