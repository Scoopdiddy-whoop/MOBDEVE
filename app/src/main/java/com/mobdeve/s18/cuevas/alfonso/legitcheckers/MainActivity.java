package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

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