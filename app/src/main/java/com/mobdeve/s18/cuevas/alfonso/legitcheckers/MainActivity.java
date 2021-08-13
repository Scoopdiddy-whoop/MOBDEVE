package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnFriend.setOnClickListener(v -> {
            openDialog();
        });
    }

    public void openDialog() {
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(getSupportFragmentManager(), "example dialog");
    }
}