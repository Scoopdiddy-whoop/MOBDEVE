package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityMainBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.User;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MAIN", "ONCREATE");
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Database db = new Database();

        setStanding();

        binding.btnLogin.setOnClickListener(v -> {
            if(mAuth.getCurrentUser() != null) {
                Log.i("MAIN", "User currently logged in: " + mAuth.getCurrentUser().toString());
                openSignOutDialog();
            }
            else{
                openLoginDialog();
            }
        });
        binding.btnHistory.setOnClickListener(v -> {
            if(mAuth.getCurrentUser() != null) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                finish();
            }
            else{
                openLoginDialog();
            }
        });

        binding.btnSettings.setOnClickListener(v->{
            openSettingsDialog();
        });
        binding.btnFriend.setOnClickListener(v->{
            if(mAuth.getCurrentUser() != null) {
                openFriendsDialaog();
            }
            else{
                openLoginDialog();
            }
        });
        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
    }
//    public void createUserModel(){
//        Database db = new Database();
//        final Map<String, Object>[] mapModel = new Map[1];
//        db.getFriendList(currUser.getUid(), new Database.FirebaseMapCallback() {
//            @Override
//            public void onCallBack(Map<String, Object> map) {
//                mapModel[0] = map;
//            }
//        });
//
//    }

    public void setStanding(){
        Database db = new Database();
        final int []wins = new int[1];
        final int []losses = new int[1];

        if(mAuth.getCurrentUser() != null) {
            db.getWins(mAuth.getCurrentUser().getUid(), new Database.FirebaseIntCallback() {
                @Override
                public void onCallBack(int number) {
                    wins[0] = number;

                    db.getLosses(mAuth.getCurrentUser().getUid(), new Database.FirebaseIntCallback() {
                        @Override
                        public void onCallBack(int number) {
                            losses[0] = number;
                            binding.tvStanding.setText(new StringBuilder().append("W").append(wins[0]).append("/L").append(losses[0]).toString());
                        }
                    });
                }
            });
        }
        else{
            binding.tvStanding.setText("W0/L0");
        }
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
    public void openSignOutDialog() {
        SignoutDialog signoutDialog = new SignoutDialog();
        signoutDialog.show(getSupportFragmentManager(), "signout dialog");
    }
}