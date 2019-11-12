package com.example.memereview.model;

import java.util.ArrayList;

public class MemeReference {

    public ArrayList<String> freshList;
    public ArrayList<String> hotList;
    private static MemeReference memeReference;

    public static MemeReference getInstance(){
        if (memeReference == null){
            memeReference = new MemeReference();
        }
        return memeReference;
    }

    private MemeReference(){
        freshList = new ArrayList<>();
        hotList = new ArrayList<>();
    }
}
