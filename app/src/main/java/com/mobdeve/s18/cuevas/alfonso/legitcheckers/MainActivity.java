package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityMainBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.util.StoragePreferences;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private StoragePreferences storagePreferences;
    private boolean playBGmusic = true;
    private boolean nightMode = false;
    private Intent musicIntent;
    private ImageView background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storagePreferences = new StoragePreferences(getApplicationContext());

        FirebaseApp.initializeApp(this);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> {
            openLoginDialog();
        });

        binding.btnHistory.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

        binding.btnSettings.setOnClickListener(v->{
            openSettingsDialog();
        });

        binding.btnFriend.setOnClickListener(v->{
            openFriendsDialaog();
        });

        background = findViewById(R.id.homebg);

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        musicIntent = new Intent(MainActivity.this, BackgroundSoundService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        playBGmusic = storagePreferences.getMusicPreferences("Play");
        nightMode = storagePreferences.getThemePreferences("Theme");
        Log.i("TAG", "MainActivity:" + playBGmusic);
        if(playBGmusic)
            startService(musicIntent);
        if(nightMode)
            background.setImageResource(R.drawable.nightbg);

    }


    @Override
    protected void onResume() {
        super.onResume();
        playBGmusic = storagePreferences.getMusicPreferences("Play");
        Log.i("TAG", "MainActivityOnResume:" + playBGmusic);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storagePreferences.saveMusicPreferences("Play", playBGmusic);
        storagePreferences.saveThemePreferences("Theme", nightMode);
        Log.i("TAG", "MainActivityOnDestroy:" + playBGmusic);
        stopService(musicIntent);
    }

    public void openLoginDialog() {
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(getSupportFragmentManager(), "login dialog");
    }
    public void openSettingsDialog() {
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.show(getSupportFragmentManager(), "settings dialog");
    }
    public void openFriendsDialaog() {
        FriendsDialog friendsDialog = new FriendsDialog();
        friendsDialog.show(getSupportFragmentManager(), "friends dialog");
    }

}