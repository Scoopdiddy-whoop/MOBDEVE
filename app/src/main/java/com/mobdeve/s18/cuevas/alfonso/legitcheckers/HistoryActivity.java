package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_history);

        Button btn = (Button)findViewById(R.id.return_home);

        btn.setOnClickListener(v -> startActivity(new Intent(HistoryActivity.this, MainActivity.class)));
    }
}
