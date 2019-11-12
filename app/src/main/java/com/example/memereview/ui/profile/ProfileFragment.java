package com.example.memereview.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.memereview.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private int gallery;
    private ImageButton avatar;
    private Button saveChanges;
    private StorageReference mStorageRef;
    StorageReference avatarRef;
    private Uri contentURI;
    private Bitmap newAvatar;
    private Bitmap oldAvatar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gallery = 1;
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        avatar = root.findViewById(R.id.avatar);
        saveChanges = root.findViewById(R.id.savechanges);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        avatarRef = mStorageRef.child("/avatar/Michiel");
        applyPersonalChanges();

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
    }

    public void applyPersonalChanges() {
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
    }


}