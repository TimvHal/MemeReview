package com.example.memereview.model;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class Meme {
    private String userAvatarUrl;
    private String creator;
    private String memeImageUrl;
    private String averageRating;

    public Meme() {
        //Empty constructor is used.
    }

    public Meme(String userAvatarUrl, String creator, String memeImageUrl,
                String averageRating) {
        this.userAvatarUrl = userAvatarUrl;
        this.creator = creator;
        this.memeImageUrl = memeImageUrl;
        this.averageRating = averageRating;
    }

    public String getUserAvatarUrl() {
        return this.userAvatarUrl;
    }

    public void setUserAvatarUrl(String url) {
        this.userAvatarUrl = url;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getMemeImageUrl() {
        return this.memeImageUrl;
    }

    public void setMemeImageUrl(String url) {
        this.memeImageUrl = url;
    }

    public String getAverageRating() {
        return this.averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

}
