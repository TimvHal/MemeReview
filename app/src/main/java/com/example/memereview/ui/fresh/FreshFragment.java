package com.example.memereview.ui.fresh;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
import com.example.memereview.controller.AccountController;
import com.example.memereview.controller.SuperController;
import com.example.memereview.firebaseService.FirebaseService;
import com.example.memereview.model.Meme;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FreshFragment extends Fragment {

    private FreshViewModel freshViewModel;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private DatabaseReference databaseReference;
    private FirebaseService firebaseService;
    private List<Meme> memes;
    private Bitmap currentMeme;
    private AccountController controller;
    private ArrayList<String> memeReferences;
    private int amountToAdd;
    private int amountAdded = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controller = SuperController.getInstance().accountController;
        freshViewModel =
                ViewModelProviders.of(this).get(FreshViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fresh, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        memes = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference freshReference = databaseReference.child("fresh");
        memeReferences = new ArrayList<>();
        firebaseService = new FirebaseService();
        getMemeReferences();
        /*freshReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot imageInfo : dataSnapshot.getChildren()) {
                    String imageName = (String) imageInfo.getValue();
                    Meme m = new Meme(controller.getUser().profilePicture, "Placeholder", currentMeme, "Placeholder");
                    //memes.add(m);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        return root;
    }

    private void getMemeReferences(){
        Log.d("zooi", memeReferences.size() + "");
        firebaseService.getMemeReferences("fresh", new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                memeReferences = (ArrayList<String>) returnedThing;
                Log.d("zooi", memeReferences.size() + "");
                amountToAdd = memeReferences.size();
                addMemes();
            }

            @Override
            public void DataLoadFailed() {

            }
        });
    }

    public void getMeme(String name){
        firebaseService.getMeme(new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                Meme meme = (Meme) returnedThing;
                addToMemes(meme);
            }

            @Override
            public void DataLoadFailed() {

            }
        }, name);
    }

    private void addMemes(){
        for (String memeLocation:memeReferences){
            getMeme(memeLocation);
        }
    }

    private void addToMemes(Meme meme){
        amountAdded ++;
        memes.add(meme);
        startAdapter();
    }

    private void startAdapter(){
        if (amountAdded == amountToAdd){
            adapter = new ImageAdapter(getActivity().getApplicationContext(), memes);
            recyclerView.setAdapter(adapter);
        }
    }

/*    public Meme createMeme() {

    }*/
}