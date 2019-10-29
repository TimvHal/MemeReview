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
import com.example.memereview.enums.Theme;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    //private SettingsViewModel settingsViewModel;
    private TextView audioPercentage;
    private ProgressBar audioProgressBar;
    private SeekBar audioSeekBar;
    private ImageButton redTheme;
    private ImageButton blueTheme;
    private ImageButton blackTheme;
    private ArrayList<ImageButton> imageButtonList;
    private Button saveChanges;
    private static double audioVolume;
    private Theme currentTheme;
    private Drawable currentBackground;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
/*        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);*/
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        audioPercentage = root.findViewById(R.id.sound_percentage);
        audioProgressBar = root.findViewById(R.id.sound_progressBar);
        audioSeekBar = root.findViewById(R.id.sound_seekBar);
        redTheme = root.findViewById(R.id.theme_red);
        blueTheme = root.findViewById(R.id.theme_blue);
        blackTheme = root.findViewById(R.id.theme_black);
        currentTheme = Theme.determineTheme(NavActivity.getCurrentTheme());
        currentBackground = getActivity().findViewById(R.id.container).getBackground();
        imageButtonList = new ArrayList<>();
        imageButtonList.add(redTheme);
        imageButtonList.add(blueTheme);
        imageButtonList.add(blackTheme);
        saveChanges = root.findViewById(R.id.settings_save_changes);

        audioSeekBar.setMax(100);
        audioSeekBar.setProgress(100);
        audioSeekBar.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
        audioSeekBar.setThumbTintList(ColorStateList.valueOf(Color.parseColor("#660000")));
        audioProgressBar.setMax(100);
        audioProgressBar.setProgress(100);
        audioProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#660000")));
        audioPercentage.setText(100 + "%");
        audioVolume = 1.0;

        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioProgressBar.setProgress(i);
                audioPercentage.setText(i + "%");
                audioVolume = Double.parseDouble("0." + i);
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
                }
                break;

            case R.id.theme_blue:
                if(currentTheme != Theme.BLUE) {
                    currentTheme = Theme.BLUE;
                    currentBackground = ContextCompat.getDrawable(getContext(), R.drawable.app_background_blue);
                }
                break;

            case R.id.theme_black:
                if(currentTheme != Theme.BLACK) {
                    currentTheme = Theme.BLACK;
                    currentBackground = ContextCompat.getDrawable(getContext(), R.drawable.app_background_black);
                }
                break;

        }
    }

    public void saveChanges() {
        String filename = "personal_settings";
        String fileContents = audioVolume + "\n" + currentTheme.toString();
        FileOutputStream outputStream;

        try {
            outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getActivity().findViewById(R.id.container).setBackground(currentBackground);
    }
}