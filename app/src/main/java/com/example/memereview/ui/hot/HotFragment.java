package com.example.memereview.ui.hot;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memereview.ImageAdapter;
import com.example.memereview.R;
import com.example.memereview.controller.AccountController;
import com.example.memereview.controller.SuperController;
import com.example.memereview.firebaseService.FirebaseService;
import com.example.memereview.model.Meme;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class HotFragment extends Fragment {

    private HotViewModel hotViewModel;
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
        hotViewModel =
                ViewModelProviders.of(this).get(HotViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hot, container, false);
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
        Log.d("zooi", memeReferences.size() + "");
        firebaseService.getMemeReferences("hot", new FirebaseService.DataStatus() {
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
}