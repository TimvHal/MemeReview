package com.example.memereview.ui.fresh;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memereview.ImageAdapter;
import com.example.memereview.R;
import com.example.memereview.firebaseService.FirebaseService;
import com.example.memereview.model.Meme;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FreshFragment extends Fragment {

    private FreshViewModel freshViewModel;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private DatabaseReference memeReference;
    private DatabaseReference userReference;
    private FirebaseService firebaseService;
    private List<Meme> memes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        freshViewModel =
                ViewModelProviders.of(this).get(FreshViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fresh, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        memes = new ArrayList<>();
        storageReference = FirebaseStorage.getInstance().getReference("memes");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        memeReference = FirebaseDatabase.getInstance().getReference("fresh");
        userReference = FirebaseDatabase.getInstance().getReference("users");


        firebaseService = new FirebaseService();
        getMeme();



        return root;
    }

    public void getMeme(){
        firebaseService.getMeme(new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                Bitmap meme = (Bitmap) returnedThing;
                System.out.println(meme);
            }

            @Override
            public void DataLoadFailed() {

            }
        }, "0.jpg");
    }

/*    public Meme createMeme() {

    }*/
}