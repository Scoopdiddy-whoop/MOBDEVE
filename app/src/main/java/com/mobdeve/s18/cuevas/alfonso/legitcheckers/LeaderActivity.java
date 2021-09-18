package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.adapter.LeaderAdapter;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityLeaderBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Leader;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.util.StoragePreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderActivity extends AppCompatActivity {

    private ActivityLeaderBinding binding;
    private StoragePreferences storagePreferences;
    private boolean playBGmusic = true;
    private boolean nightMode = false;
    private ImageView background;
    FirebaseAuth mAuth;
    FirebaseDatabase fdb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderBinding.inflate(getLayoutInflater());
        storagePreferences = new StoragePreferences(getApplicationContext());
        background = binding.leaderbg;

        Button btn = binding.returnHome;
        btn.setOnClickListener(v -> startActivity(new Intent(LeaderActivity.this, MainActivity.class)));
        setContentView(binding.getRoot());

        Log.i("LEAD", "ON CREATE OF LEAD");

        fdb = FirebaseDatabase.getInstance("https://legitcheckers-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference usersRef = fdb.getReference("leaderboards");

        Log.i("LEAD", "ASDFDF");
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Leader> leaders = new ArrayList<Leader>();
                    DataSnapshot dataSnapshot = task.getResult();
                    Iterable<DataSnapshot> users = dataSnapshot.getChildren();
                    for(DataSnapshot snapshot : users) {
                        String username = ((HashMap)snapshot.getValue()).get("username").toString();
                        Log.i("LEAD",username);
                        int wins =  Integer.parseInt(((HashMap)snapshot.getValue()).get("wins").toString());
                        Log.i("LEAD", wins+"");

                        leaders.add(new Leader(username, wins));
                    }
                    int i, key, j;
                    for (i = 1; i < leaders.size(); i++)
                    {
                        key = leaders.get(i).getWins();
                        j = i - 1;
                        /* Move elements of arr[0..i-1], that are
                        greater than key, to one position ahead
                        of their current position */
                        while (j >= 0 && leaders.get(j).getWins() < key)
                        {
                            leaders.set(j+1, leaders.get(j));
                            j = j - 1;
                        }
                        leaders.set(j+1, leaders.get(i));
                    }
//                    Collections.reverse(leaders);
                    RecyclerView rv_players = binding.rvPlayers;
                    LeaderAdapter leaderAdapter = new LeaderAdapter(leaders, getApplicationContext());
                    rv_players.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv_players.setAdapter(leaderAdapter);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        playBGmusic = storagePreferences.getMusicPreferences("Play");
        nightMode = storagePreferences.getThemePreferences("Theme");
        Log.i("TAG", "LEADACTIVITY:" + playBGmusic);
        if(nightMode){
            background.setImageResource(R.drawable.nightbg);
        }

    }
}
