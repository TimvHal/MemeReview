package com.example.memereview;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class NavActivity extends AppCompatActivity {

    private static double audioVolume;
    private static String currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_fresh, R.id.navigation_hot, R.id.navigation_owned)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if(new File(getApplicationContext().getFilesDir(), "personal_settings").exists()) {
            try {
                FileInputStream fileInputStream = getApplicationContext().openFileInput("personal_settings");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                audioVolume = Double.parseDouble(bufferedReader.readLine());
                currentTheme = bufferedReader.readLine().toLowerCase();

                applyPersonalChanges();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            audioVolume = 1.0;
            currentTheme = "red";
        }
    }

    public static double getAudioVolume() {
        return audioVolume;
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }

    public void applyPersonalChanges() {
        switch(currentTheme) {
            case "red":
                findViewById(R.id.container).setBackgroundResource(R.drawable.app_background_red);
                break;
            case "blue":
                findViewById(R.id.container).setBackgroundResource(R.drawable.app_background_blue);
                break;
            case "black":
                findViewById(R.id.container).setBackgroundResource(R.drawable.app_background_black);
                break;
        }
    }
}
