package com.example.memereview.model;

import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class Meme {
    private Bitmap userAvatar;
    private String creator;
    private Bitmap memeImage;
    private String averageRating;

    public Meme() {
        //Empty constructor is used.
    }

    public Meme(Bitmap userAvatar, String creator, Bitmap memeImage,
                String averageRating) {
        this.userAvatar = userAvatar;
        this.creator = creator;
        this.memeImage = memeImage;
        this.averageRating = averageRating;
    }

    public Bitmap getUserAvatar() {
        return this.userAvatar;
    }

    public void setUserAvatar(Bitmap map) {
        this.userAvatar = map;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Bitmap getMemeImage() {
        return this.memeImage;
    }

    public void setMemeImage(Bitmap map) {
        this.memeImage = map;
    }

    public String getAverageRating() {
        return this.averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

}
