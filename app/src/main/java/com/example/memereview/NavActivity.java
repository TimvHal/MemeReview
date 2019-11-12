package com.example.memereview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.memereview.controller.AccountController;
import com.example.memereview.controller.SuperController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class NavActivity extends AppCompatActivity {

    private static double audioVolume;
    private static String currentTheme;
    private AccountController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = SuperController.getInstance().accountController;
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

       audioVolume = Double.parseDouble(controller.getUser().audioVolume);
       currentTheme = controller.getUser().theme.toLowerCase();
       applyPersonalChanges();
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

    public void goToUploadView(View v){
        Intent intent = new Intent(this, UploadMenu.class);
        startActivity(intent);
    }
}
