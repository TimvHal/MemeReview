package com.example.memereview.ui.owned;

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
import com.example.memereview.OwnedImageAdapter;
import com.example.memereview.R;
import com.example.memereview.controller.AccountController;
import com.example.memereview.controller.SuperController;
import com.example.memereview.firebaseService.FirebaseService;
import com.example.memereview.model.Meme;
import com.example.memereview.ui.hot.HotViewModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class OwnedFragment extends Fragment {

    private HotViewModel hotViewModel;
    private RecyclerView recyclerView;
    private OwnedImageAdapter adapter;
    private DatabaseReference databaseReference;
    private FirebaseService firebaseService;
    private List<Meme> memes;
    private Bitmap currentMeme;
    private AccountController controller;
    private ArrayList<String> memeReferences;
    private int amountToAdd;
    private int amountAdded = 0;

    private OwnedViewModel ownedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controller = SuperController.getInstance().accountController;
        ownedViewModel =
                ViewModelProviders.of(this).get(OwnedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_owned, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        memes = new ArrayList<>();
        memeReferences = new ArrayList<>();
        firebaseService = new FirebaseService();
        getMemeReferences();
        return root;
    }

    private void getMemeReferences(){
        firebaseService.getMemeReferences(controller.getUser().userName, new FirebaseService.DataStatus() {
            @Override
            public void DataIsLoaded(Object returnedThing) {
                memeReferences = (ArrayList<String>) returnedThing;
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
            adapter = new OwnedImageAdapter(getActivity().getApplicationContext(), memes);
            recyclerView.setAdapter(adapter);
        }
    }

}