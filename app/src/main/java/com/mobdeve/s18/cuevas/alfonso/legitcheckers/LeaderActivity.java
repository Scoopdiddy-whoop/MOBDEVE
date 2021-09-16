package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityLeaderBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.util.StoragePreferences;

public class LeaderActivity extends AppCompatActivity {

    private ActivityLeaderBinding binding;
    private StoragePreferences storagePreferences;
    private boolean playBGmusic = true;
    private boolean nightMode = false;
    private ImageView background;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderBinding.inflate(getLayoutInflater());
        storagePreferences = new StoragePreferences(getApplicationContext());
        background = binding.leaderbg;
        Button btn = binding.returnHome;
        btn.setOnClickListener(v -> startActivity(new Intent(LeaderActivity.this, MainActivity.class)));
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();

        playBGmusic = storagePreferences.getMusicPreferences("Play");
        nightMode = storagePreferences.getThemePreferences("Theme");
        Log.i("TAG", "HistoryActivity:" + playBGmusic);
        if(nightMode){
            background.setImageResource(R.drawable.nightbg);
        }

    }
}
