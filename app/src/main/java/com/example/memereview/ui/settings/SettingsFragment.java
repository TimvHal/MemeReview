package com.example.memereview.ui.settings;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.memereview.NavActivity;
import com.example.memereview.R;
import com.example.memereview.controller.AccountController;
import com.example.memereview.controller.SuperController;
import com.example.memereview.enums.Theme;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    //private SettingsViewModel settingsViewModel;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;
    private AccountController controller;
    private TextView audioPercentage;
    private ProgressBar audioProgressBar;
    private SeekBar audioSeekBar;
    private ImageButton redTheme;
    private ImageButton blueTheme;
    private ImageButton blackTheme;
    private ArrayList<ImageButton> imageButtonList;
    private Button saveChanges;
    private static double audioVolume;
    private static Theme currentTheme;
    private Drawable currentBackground;
    private TextView redThemeText;
    private TextView blueThemeText;
    private TextView blackThemeText;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controller = SuperController.getInstance().accountController;
/*        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);*/
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("users/" + controller.getUser().userName);

        audioPercentage = root.findViewById(R.id.sound_percentage);
        audioProgressBar = root.findViewById(R.id.sound_progressBar);
        audioSeekBar = root.findViewById(R.id.sound_seekBar);
        redTheme = root.findViewById(R.id.theme_red);
        blueTheme = root.findViewById(R.id.theme_blue);
        blackTheme = root.findViewById(R.id.theme_black);
        if(currentTheme == null) {
            currentTheme = Theme.determineTheme(NavActivity.getCurrentTheme());
        }
        currentBackground = getActivity().findViewById(R.id.container).getBackground();
        imageButtonList = new ArrayList<>();
        imageButtonList.add(redTheme);
        imageButtonList.add(blueTheme);
        imageButtonList.add(blackTheme);
        saveChanges = root.findViewById(R.id.settings_save_changes);
        redThemeText = root.findViewById(R.id.theme_text_red);
        blueThemeText = root.findViewById(R.id.theme_text_blue);
        blackThemeText = root.findViewById(R.id.theme_text_black);

        audioSeekBar.setMax(100);
        audioSeekBar.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
        audioSeekBar.setThumbTintList(ColorStateList.valueOf(Color.parseColor("#660000")));
        audioProgressBar.setMax(100);
        audioProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#660000")));
        applyPersonalChanges();

        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioProgressBar.setProgress(i);
                audioPercentage.setText(i + "%");
                if(i != 100) {
                    audioVolume = Double.parseDouble("0." + i);
                }
                else { audioVolume = 1.0; }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        for(ImageButton i : imageButtonList) {
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTheme(v);
                }
            });
        }

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
        showThemeSelectionAtStart();
        return root;
    }

    public static double getAudioVolume() {
        return audioVolume;
    }

    public void changeTheme(View v) {
        switch(v.getId()) {
            case R.id.theme_red:
                if(currentTheme != Theme.RED) {
                    currentTheme = Theme.RED;
                    currentBackground = ContextCompat.getDrawable(getContext(), R.drawable.app_background_red);
                    redThemeText.setBackgroundColor(Color.parseColor("#32FFFFFF"));
                    blueThemeText.setBackgroundResource(0);
                    blackThemeText.setBackgroundResource(0);

                }
                break;

            case R.id.theme_blue:
                if(currentTheme != Theme.BLUE) {
                    currentTheme = Theme.BLUE;
                    currentBackground = ContextCompat.getDrawable(getContext(), R.drawable.app_background_blue);
                    blueThemeText.setBackgroundColor(Color.parseColor("#32FFFFFF"));
                    redThemeText.setBackgroundResource(0);
                    blackThemeText.setBackgroundResource(0);
                }
                break;

            case R.id.theme_black:
                if(currentTheme != Theme.BLACK) {
                    currentTheme = Theme.BLACK;
                    currentBackground = ContextCompat.getDrawable(getContext(), R.drawable.app_background_black);
                    blackThemeText.setBackgroundColor(Color.parseColor("#32FFFFFF"));
                    redThemeText.setBackgroundResource(0);
                    blueThemeText.setBackgroundResource(0);
                }
                break;
        }
    }

    public void showThemeSelectionAtStart() {
        switch(controller.getUser().theme.toLowerCase()) {
            case "red":
                redThemeText.setBackgroundColor(Color.parseColor("#32FFFFFF"));
                break;
            case "blue":
                blueThemeText.setBackgroundColor(Color.parseColor("#32FFFFFF"));
                break;
            case "black":
                blackThemeText.setBackgroundColor(Color.parseColor("#32FFFFFF"));
                break;
        }
    }

    public void applyPersonalChanges() {
        audioVolume = Double.parseDouble(controller.getUser().audioVolume);
        audioSeekBar.setProgress((int) (Double.parseDouble(controller.getUser().audioVolume) * 100));
        audioProgressBar.setProgress((int) (Double.parseDouble(controller.getUser().audioVolume) * 100));
        audioPercentage.setText((int) (Double.parseDouble(controller.getUser().audioVolume) * 100) + "%");
        switch(controller.getUser().theme) {
            case "RED":
                redThemeText.setBackgroundColor(Color.parseColor("#32FFFFFF"));
                blueThemeText.setBackgroundResource(0);
                blackThemeText.setBackgroundResource(0);
                currentBackground = ContextCompat.getDrawable(getContext(), R.drawable.app_background_red);
                break;
            case "BLUE":
                blueThemeText.setBackgroundColor(Color.parseColor("#32FFFFFF"));
                redThemeText.setBackgroundResource(0);
                blackThemeText.setBackgroundResource(0);
                currentBackground = ContextCompat.getDrawable(getContext(), R.drawable.app_background_blue);
                break;
            case "BLACK":
                blackThemeText.setBackgroundColor(Color.parseColor("#32FFFFFF"));
                redThemeText.setBackgroundResource(0);
                blueThemeText.setBackgroundResource(0);
                currentBackground = ContextCompat.getDrawable(getContext(), R.drawable.app_background_black);
                break;
        }
        getActivity().findViewById(R.id.container).setBackground(currentBackground);
    }

    public void saveChanges() {
        userRef.child("audioVolume").setValue(String.valueOf(audioVolume));
        userRef.child("theme").setValue(currentTheme.toString());
        controller.getUser().audioVolume = String.valueOf(audioVolume);
        controller.getUser().theme = currentTheme.toString();
        applyPersonalChanges();
    }
}