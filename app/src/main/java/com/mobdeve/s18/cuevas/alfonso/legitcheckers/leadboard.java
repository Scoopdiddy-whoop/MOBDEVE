package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityLeaderBinding;

public class leadboard extends AppCompatActivity {
    private ActivityLeaderBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase fdb;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
