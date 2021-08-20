package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    BoardView boardView;

    private ActivityGameBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);
    }
}