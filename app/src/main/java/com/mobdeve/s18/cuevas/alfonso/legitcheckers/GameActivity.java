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
    ArrayList<CheckerPiece> piecesLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);

        storagePreferences = new StoragePreferences(getApplicationContext());
        piecesLoad = new ArrayList<>();
        background = findViewById(R.id.gamebg);
        piecesLoad.add(new CheckerPiece(0, 1, "Black", false));
        checkerGame = new CheckerGame(piecesLoad);
        boardView = findViewById(R.id.board);
        boardView.setPiecePosition((PiecePosition)this);
        String status = getIntent().getStringExtra("status");
        String roomName = getIntent().getStringExtra("roomName");


        firebaseDatabase = FirebaseDatabase.getInstance("https://legitcheckers-default-rtdb.asia-southeast1.firebasedatabase.app");
        roomRef = firebaseDatabase.getReference("rooms/"+roomName);
        Log.i("GAMEACTIVITY", "ON CREATE");

        if(status.equals("host")){
            piecesLoad.clear();
            Log.i("GAMEACTIVITY", "HAHATDOG");
            int row = 0;
            for (int i = 8; row < i; ++row) {
                int col = 0;
                for (int j = 8; col < j; ++col) {
                    boolean positions = (col % 2 == 1 && row % 2 != 1) || (col % 2 == 0 && row % 2 == 1);
                    if (row < 3) {
                        if (positions) {
                            piecesLoad.add(new CheckerPiece(col, row, "Black", false));
                        }
                    }

                    if (row > 4) {
                        if (positions)
                            piecesLoad.add(new CheckerPiece(col, row, "White", false));
                    }
                }
            }
            checkerGame = new CheckerGame(piecesLoad);
            roomRef.child("boxes").setValue(checkerGame.getPiecesBox());
            Log.i("GAMEACTIVITY", "HOST");
            setup();
        }
        else{
            Log.i("GAMEACTIVITY", "GUEST");
            roomRef.child("boxes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    piecesLoad.clear();
                    Log.i("GAMEACTIVITY", "ONCOMP");
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        Iterable<DataSnapshot> pieces = dataSnapshot.getChildren();

                        for(DataSnapshot snapshot : pieces) {
                            String player = Objects.requireNonNull(((HashMap) snapshot.getValue()).get("player").toString());
                            int row = Integer.parseInt(Objects.requireNonNull(((HashMap) snapshot.getValue()).get("row").toString()));
                            int col = Integer.parseInt(Objects.requireNonNull(((HashMap) snapshot.getValue()).get("col").toString()));
                            boolean king = ((boolean)((HashMap) Objects.requireNonNull(snapshot.getValue())).get("king"));

                            CheckerPiece cp = new CheckerPiece(col,row,player,king);
                            piecesLoad.add(cp);
                        }
                        checkerGame = new CheckerGame(piecesLoad);
                        boardView.invalidate();
                        Log.i("onComplete", "pieces"+ checkerGame.getPiecesBox().size());
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
                checkerGame.setPiecesBox(bd);
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

        roomRef.child("boxes").setValue(checkerGame.getPiecesBox());

        enemyScore.setText("Enemy: " + (12 - checkerGame.getNumPieces("White")));
        playerScore.setText("Player: " + (12 - checkerGame.getNumPieces("Black")));
        if(checkerGame.getWinningPlayer()!="None") {
            Log.i("TAG", checkerGame.getWinningPlayer() + " WON THE GAME!!!");
            openWinnerDialog();
        }

        boardView.invalidate();
    }
    
}