package com.example.memereview.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.example.memereview.firebaseService.FirebaseService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private View root;
    private int gallery;
    private ImageButton avatar;
    private Button saveChanges;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;
    private StorageReference mStorageRef;
    private StorageReference avatarRef;
    private AccountController controller;
    private Uri contentURI;
    private Bitmap newAvatar;
    private SeekBar ageSeekBar;
    private TextView ageCounter;
    private EditText usernameField;
    private RadioGroup genderBoxes;
    private RadioButton maleButton;
    private RadioButton femaleButton;
    private RadioButton otherButton;
    private String userData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controller = SuperController.getInstance().accountController;
        gallery = 1;
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        avatar = root.findViewById(R.id.avatar);
        saveChanges = root.findViewById(R.id.savechanges);
        usernameField = root.findViewById(R.id.usernameField);
        genderBoxes = root.findViewById(R.id.genderBoxes);
        maleButton = root.findViewById(R.id.maleCheckBox);
        femaleButton = root.findViewById(R.id.femaleCheckBox);
        otherButton = root.findViewById((R.id.otherCheckBox));
        userData = "";

        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("users/" + controller.getUser().userName);


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

        getProfilePicture();
        applyPersonalChanges(root);
        return root;
    }

    private void getProfilePicture(){
        Log.d(" zooi", controller.getUser().userName + ".jpg");
        FirebaseService firebaseService = new FirebaseService();
        firebaseService.getProfilePicture(new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                Bitmap picture = (Bitmap) returnedThing;
                avatar.setImageBitmap(picture);
            }

            @Override
            public void DataLoadFailed() {

            }
        }, controller.getUser().userName);
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
        FirebaseService firebaseService = new FirebaseService();
        BitmapDrawable drawable = (BitmapDrawable) avatar.getDrawable();
        Bitmap picture = drawable.getBitmap();
        firebaseService.addProfilePicture(controller.getUser().userName, picture);
        if(contentURI != null) {
            avatarRef.putFile(contentURI);
        }
        userRef.child("nickName").setValue(usernameField.getText().toString());
        userRef.child("gender").setValue(getGender());
        userRef.child("age").setValue(ageCounter.getText().toString());
        controller.getUser().nickName = usernameField.getText().toString();
        controller.getUser().gender = getGender();
        controller.getUser().age = ageCounter.getText().toString();
        applyPersonalChanges(root);
    }

    public String getGender(){
        int id = genderBoxes.getCheckedRadioButtonId();
        if (id == maleButton.getId()){
            return "male";
        }
        else if (id == femaleButton.getId()){
            return "female";
        }
        else{
            return "other";
        }
    }


    public void applyPersonalChanges(View root) {
        avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                contentURI = uri;
            }
        });
        usernameField.setText(controller.getUser().nickName);
        setGenderBox(controller.getUser().gender);
        String currentAge = controller.getUser().age;
        ageSeekBar.setProgress(Integer.parseInt(currentAge));
        ageCounter.setText(currentAge);
    }

    private void setGenderBox(String gender){
        if (gender.equals("male")){
            maleButton.setChecked(true);
        }
        else if (gender.equals("female")){
            femaleButton.setChecked(true);
        }
        else{
            otherButton.setChecked(true);
        }
    }
}