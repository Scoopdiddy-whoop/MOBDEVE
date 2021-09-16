package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityGameBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.BoardView;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.CheckerGame;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.CheckerPiece;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.Square;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.util.StoragePreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GameActivity extends AppCompatActivity implements PiecePosition {

    private ActivityGameBinding binding;
    private StoragePreferences storagePreferences;
    private boolean playBGmusic = true;
    private boolean nightMode = false;
    private Intent musicIntent;
    private ImageView background;
    private CheckerGame checkerGame;
    private BoardView boardView;
    private TextView playerScore;
    private TextView enemyScore;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);

        storagePreferences = new StoragePreferences(getApplicationContext());
        background = findViewById(R.id.gamebg);

        checkerGame = new CheckerGame();
        String status = getIntent().getStringExtra("status");
        String roomName = getIntent().getStringExtra("roomName");

        firebaseDatabase = FirebaseDatabase.getInstance("https://legitcheckers-default-rtdb.asia-southeast1.firebasedatabase.app");
        roomRef = firebaseDatabase.getReference("rooms/"+roomName);
        Log.i("GAMEACTIVITY", "ON CREATE");

        if(status.equals("host")){
            Log.i("GAMEACTIVITY", "HAHATDOG");
            roomRef.child("boxes").setValue(CheckerGame.getPiecesBox());
            Log.i("GAMEACTIVITY", "HOST");
            setup();
        }
        else{
            Log.i("GAMEACTIVITY", "GUEST");
            roomRef.child("boxes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        ArrayList<CheckerPiece> bd = new ArrayList<>();
                        Iterable<DataSnapshot> pieces = dataSnapshot.getChildren();

                        for(DataSnapshot snapshot : pieces) {
                            String player = Objects.requireNonNull(((HashMap) snapshot.getValue()).get("player").toString());
                            int row = Integer.parseInt(Objects.requireNonNull(((HashMap) snapshot.getValue()).get("row").toString()));
                            int col = Integer.parseInt(Objects.requireNonNull(((HashMap) snapshot.getValue()).get("col").toString()));
                            boolean king = ((boolean)((HashMap) Objects.requireNonNull(snapshot.getValue())).get("king"));

                            CheckerPiece cp = new CheckerPiece(col,row,player,king);
                            bd.add(cp);
                        }
                        CheckerGame.setPiecesBox(bd);
                        setup();
                    }
                    else {
                        Log.i("GAMEACTIVITY", String.valueOf(task.getResult().getValue())+"working not complete");
                    }
                }
            });
        }

    }
    public void setup(){
        boardView = findViewById(R.id.board);
        boardView.setPiecePosition((PiecePosition)this);
        playerScore = findViewById(R.id.player);
        enemyScore = findViewById(R.id.enemy);
        ImageButton btn = findViewById(R.id.btn_menu);
        musicIntent = new Intent(GameActivity.this, BackgroundSoundService.class);
        btn.setOnClickListener(v -> {
            openMenuDialog();
        });
        roomRef.child("boxes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<CheckerPiece> bd = new ArrayList<>();
                Iterable<DataSnapshot> pieces = dataSnapshot.getChildren();
                for(DataSnapshot snapshot : pieces) {
                    String player = Objects.requireNonNull(((HashMap) snapshot.getValue()).get("player").toString());
                    int row = Integer.parseInt(Objects.requireNonNull(((HashMap) snapshot.getValue()).get("row").toString()));
                    int col = Integer.parseInt(Objects.requireNonNull(((HashMap) snapshot.getValue()).get("col").toString()));
                    boolean king = ((boolean)((HashMap) Objects.requireNonNull(snapshot.getValue())).get("king"));

                    CheckerPiece cp = new CheckerPiece(col,row,player,king);
                    bd.add(cp);
                }
                Log.i("GAME", "NUM: "+ bd.size());
                CheckerGame.setPiecesBox(bd);
                Log.i("GAME ACTIVITY", CheckerGame.getPiecesBox().toString());
                boardView.invalidate();
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        playBGmusic = storagePreferences.getMusicPreferences("Play");
        nightMode = storagePreferences.getThemePreferences("Theme");
        Log.i("TAG", "GameActivity:" + playBGmusic);
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
    public void openMenuDialog() {
        MenuDialog menuDialog = new MenuDialog();
        menuDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void openWinnerDialog() {
        Bundle bundle = new Bundle();
        bundle.putString("winner", checkerGame.getWinningPlayer().toString());
        WinnerDialog winnerDialog = new WinnerDialog();
        winnerDialog.setArguments(bundle);
        winnerDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public CheckerPiece pieceAt(Square square) {
        return checkerGame.pieceAt(square);
    }

    @Override
    public void movePiece(Square from, Square to) {
        checkerGame.movePiece(from, to);

        roomRef.child("boxes").setValue(CheckerGame.getPiecesBox());

        enemyScore.setText("Enemy: " + (12 - checkerGame.getNumPieces("White")));
        playerScore.setText("Player: " + (12 - checkerGame.getNumPieces("Black")));
        if(checkerGame.getWinningPlayer()!="None") {
            Log.i("TAG", checkerGame.getWinningPlayer() + " WON THE GAME!!!");
            openWinnerDialog();
        }

        boardView.invalidate();
    }
    
}