package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityHistoryBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Log.i("HISTORY", "USER: "+ mAuth.getCurrentUser());
        Intent intent = getIntent();
        String friendID = intent.getStringExtra("friendID");

        if(friendID==null){
            loadProfile(user.getUid());
            Button btn = (Button)findViewById(R.id.return_home);
            btn.setOnClickListener(v -> startActivity(new Intent(HistoryActivity.this, MainActivity.class)));
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

                                binding.textView2.setText(new StringBuilder().append("W").append(wins[0]).append("/L").append(losses[0]).toString());
                                binding.textView3.setText(username[0]);
                            }
                        });
                    }
                });
            }
        });



    }
}
