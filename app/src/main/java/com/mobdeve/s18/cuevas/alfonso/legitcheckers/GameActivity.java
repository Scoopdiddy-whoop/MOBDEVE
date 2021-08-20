package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);

        ImageButton btn = findViewById(R.id.btn_menu);

        btn.setOnClickListener(v -> {
            openDialog();
        });

    }

    public void openDialog() {
        MenuDialog menuDialog = new MenuDialog();
        menuDialog.show(getSupportFragmentManager(), "example dialog");
    }
}