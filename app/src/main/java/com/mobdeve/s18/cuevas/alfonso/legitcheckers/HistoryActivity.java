package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityHistoryBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.util.StoragePreferences;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;

    private StoragePreferences storagePreferences;
    private boolean playBGmusic = true;
    private boolean nightMode = false;
    private Intent musicIntent;
    private ImageView background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_history);

        storagePreferences = new StoragePreferences(getApplicationContext());

        Button btn = (Button)findViewById(R.id.return_home);
        background = findViewById(R.id.matchbg);

        btn.setOnClickListener(v -> startActivity(new Intent(HistoryActivity.this, MainActivity.class)));

        musicIntent = new Intent(HistoryActivity.this, BackgroundSoundService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        playBGmusic = storagePreferences.getMusicPreferences("Play");
        nightMode = storagePreferences.getThemePreferences("Theme");
        Log.i("TAG", "HistoryActivity:" + playBGmusic);
        if(playBGmusic){
            //startService(musicIntent);
        }
        if(nightMode){
        background.setImageResource(R.drawable.nightbg);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //stopService(musicIntent);
    }
}
