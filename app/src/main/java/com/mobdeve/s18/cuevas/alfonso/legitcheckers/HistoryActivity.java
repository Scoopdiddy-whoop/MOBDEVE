package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.adapter.MatchAdapter;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityHistoryBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.util.StoragePreferences;

import java.util.ArrayList;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;


    private StoragePreferences storagePreferences;
    private boolean playBGmusic = true;
    private boolean nightMode = false;
    private Intent musicIntent;
    private ImageView background;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        storagePreferences = new StoragePreferences(getApplicationContext());
        Button btn = (Button)findViewById(R.id.return_home);
        background = findViewById(R.id.matchbg);
        btn.setOnClickListener(v -> startActivity(new Intent(HistoryActivity.this, MainActivity.class)));
        musicIntent = new Intent(HistoryActivity.this, BackgroundSoundService.class);

        Log.i("HISTORY", "USER: "+ mAuth.getCurrentUser());
        Intent intent = getIntent();
        String friendID = intent.getStringExtra("friendID");
        if(friendID==null){
            loadProfile(user.getUid());
        }
        else{
            loadProfile(friendID);
        }
    }


    private void loadProfile(String userID) {
        Database db = new Database();
        final int []wins = new int[1];
        final int []losses = new int[1];
        final String[] username = new String[1];

        db.getWins(userID, new Database.FirebaseIntCallback() {
            @Override
            public void onCallBack(int number) {
                wins[0] = number;

                db.getLosses(userID, new Database.FirebaseIntCallback() {
                    @Override
                    public void onCallBack(int number) {
                        losses[0] = number;

                        db.getUsername(userID, new Database.FirebaseStringCallback() {
                            @Override
                            public void onCallBack(String string) {
                                username[0] = string;
                                db.getMatches(userID, new Database.FirebaseMapCallback() {
                                    @Override
                                    public void onCallBack(Map<String, String> map) {
                                        binding.textView2.setText(new StringBuilder().append("W").append(wins[0]).append("/L").append(losses[0]).toString());
                                        binding.textView3.setText(username[0]);
                                        ArrayList<String> ids = new ArrayList<>(map.values());
                                        MatchAdapter matchAdapter = new MatchAdapter(ids, getApplicationContext());
                                        binding.rvMatches.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        binding.rvMatches.setAdapter(matchAdapter);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });


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

    @Override
    protected void onStop() {
        super.onStop();
        //stopService(musicIntent);

    }
}
