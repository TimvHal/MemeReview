package com.example.memereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainMenu extends AppCompatActivity {
    private Boolean menuToggled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        this.menuToggled = false;
    }

    public void toStartScreen(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void toggleMenu(View v) {
        Button back = findViewById(R.id.backbutton);
        ImageView menu = findViewById(R.id.menu);
        FloatingActionButton hamburgerButton = findViewById(R.id.hamburgerbutton);
        if(!menuToggled) {
            menuToggled = true;
            back.animate().alpha((float) 0.5).setDuration(250);
            findViewById(R.id.settingsbutton).animate().alpha((float) 0.5).setDuration(500);
            findViewById(R.id.profilebutton).animate().alpha((float) 0.5).setDuration(750);
            back.setClickable(true);
            menu.animate().alpha((float) 0.75).setDuration(250);
            hamburgerButton.setImageResource(R.drawable.cross_icon);
            return;
        }
        menuToggled = false;
        back.animate().alpha(0).setDuration(250);
        findViewById(R.id.settingsbutton).animate().alpha(0).setDuration(250);
        findViewById(R.id.profilebutton).animate().alpha(0).setDuration(250);
        back.setClickable(false);
        menu.animate().alpha(0).setDuration(250);
        hamburgerButton.setImageResource(R.drawable.hamburger_icon);




    }
}
