package com.example.memereview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memereview.firebaseService.FirebaseService;
import com.example.memereview.model.Meme;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Meme> memes;

    public ImageAdapter(Context context, List<Meme> memes) {
        this.context = context;
        this.memes = memes;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.meme_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Meme currentMeme = memes.get(position);
        holder.userAvatar.setImageBitmap(currentMeme.getUserAvatar());
        Glide.with(context)
                .load(currentMeme.getUserAvatar())
                .circleCrop()
                .into(holder.userAvatar);
        holder.userNameField.setText(currentMeme.getCreator());
        holder.meme.setImageBitmap(currentMeme.getMemeImage());
        Glide.with(context)
                .load(currentMeme.getMemeImage())
                .fitCenter()
                .into(holder.meme);
        holder.averageRating.setText(currentMeme.getAverageRating());
        holder.memeLocation = currentMeme.getName();
    }

    @Override
    public int getItemCount() {
        return this.memes.size();
    }

    public void addToMemes(Meme meme){
        memes.add(meme);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView userAvatar;
        public TextView userNameField;
        public ImageView meme;
        public TextView averageRating;
        public SeekBar ratingSeekBar;
        public TextView currentRating;
        public Button rateButton;
        public String memeLocation;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.memeUserAvatar);
            userNameField = itemView.findViewById(R.id.memeUserNameField);
            meme = itemView.findViewById(R.id.memeImage);
            averageRating = itemView.findViewById(R.id.averageRating);
            ratingSeekBar = itemView.findViewById(R.id.ratingSeekBar);
            currentRating = itemView.findViewById(R.id.ratingCurrent);
            rateButton = itemView.findViewById(R.id.rateButton);
            rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rateMeme();
                }
            });
        }

        private void rateMeme(){
            FirebaseService firebaseService = new FirebaseService();
            firebaseService.rateMeme(memeLocation, ratingSeekBar.getProgress());
            rateButton.setEnabled(false);
        }
    }
}
