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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityGameBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.BoardView;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.CheckerGame;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.CheckerPiece;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.Square;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;
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
    private TextView gameHeader;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference roomRef;
    ArrayList<CheckerPiece> piecesLoad;
    private String currentPlayer;
    private String status;
    private String roomName;
    ValueEventListener valueEventListener;
    FirebaseAuth mAuth;
    private boolean removeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);

        currentPlayer = "White";
        storagePreferences = new StoragePreferences(getApplicationContext());
        piecesLoad = new ArrayList<>();
        background = findViewById(R.id.gamebg);
        piecesLoad.add(new CheckerPiece(0, 1, "Black", false));
        checkerGame = new CheckerGame(piecesLoad);
        boardView = findViewById(R.id.board);
        gameHeader = findViewById(R.id.gameHeader);
        boardView.setPiecePosition((PiecePosition)this);
        status = getIntent().getStringExtra("status");
        roomName = getIntent().getStringExtra("roomName");
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        firebaseDatabase = FirebaseDatabase.getInstance("https://legitcheckers-default-rtdb.asia-southeast1.firebasedatabase.app");
        roomRef = firebaseDatabase.getReference("rooms/"+roomName);
        Log.i("GAMEACTIVITY", "ON CREATE");

        if(status.equals("host")){
            piecesLoad.clear();
            Log.i("GAMEACTIVITY", "HAHATDOG");
            int row = 0;
            /*for (int i = 8; row < i; ++row) {
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
            }*/
            piecesLoad.add(new CheckerPiece(4, 5, "White", true));
            piecesLoad.add(new CheckerPiece(5, 4, "Black", true));
            checkerGame = new CheckerGame(piecesLoad);
            Log.i("TEST", checkerGame.getWinningPlayer());
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

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean won = false;
//                currentPlayer = roomRef.child("Turn").get().toString();
//                checkerGame.setCurrentPlayer(currentPlayer);
                roomRef.child("turn").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().getValue()!=null) {
                                currentPlayer = task.getResult().getValue().toString();
                                checkerGame.setCurrentPlayer(currentPlayer);
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
                                Log.i("PLAY", "moved ON DATA: " + currentPlayer);
                                Log.i("GAME", "NUM: "+ bd.size());
                                checkerGame.setPiecesBox(bd);
                                boardView.invalidate();
                                enemyScore.setText("Enemy: " + (12 - checkerGame.getNumPieces("White")));
                                playerScore.setText("Player: " + (12 - checkerGame.getNumPieces("Black")));
                                checkerGame.checkWinner();
                                removeListener = true;
                                win();
                            }
                        }
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        };
        roomRef.child("boxes").addValueEventListener(valueEventListener);
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
    protected void onDestroy() {
        super.onDestroy();
//        roomRef.removeValue();
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
        bundle.putString("room",roomName);
        WinnerDialog winnerDialog = new WinnerDialog();
        winnerDialog.setArguments(bundle);
        winnerDialog.show(getSupportFragmentManager(), "example dialog");
//        roomRef.child("boxes").removeEventListener(valueEventListener);
    }

    @Override
    public CheckerPiece pieceAt(Square square) {
        return checkerGame.pieceAt(square);
    }

    @Override
    public void movePiece(Square from, Square to) {
        if((status.equals("host") && pieceAt(from).getPlayer().equals("White"))
                || status.equals("guest") && pieceAt(from).getPlayer().equals("Black")){
            boolean antiStun;
            checkerGame.movePiece(from, to);
            boardView.invalidate();
            roomRef.child("boxes").setValue(checkerGame.getPiecesBox());
            antiStun = currentPlayer != checkerGame.getCurrentPlayer();
            currentPlayer = checkerGame.getCurrentPlayer();
            Log.i("PLAY", "moved MOVE PIECE: " + currentPlayer);
            Log.i("PLAY", "CURR: " + currentPlayer);
            if(currentPlayer.equals("White")){
                currentPlayer = "White";
            }else{
                currentPlayer = "Black";
            }
            if(antiStun)
                roomRef.child("turn").setValue(currentPlayer);

        }
    }
    public void win(){
        if(valueEventListener == null){
            return;
        }
        if(!checkerGame.getWinningPlayer().equals("None")) {

            Log.i("TAG", checkerGame.getWinningPlayer() + " WON THE GAME!!!");
            Database db = new Database();
            roomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    String player1 =  task.getResult().child("p1ID").getValue().toString();
                    String player2 = task.getResult().child("p2ID").getValue().toString();
                    String winner;

                    if(checkerGame.getWinningPlayer().equals("White")){
                        winner = player1;
                    }
                    else{
                        winner = player2;
                    }
                    winfunc(player1, player2, winner);
                    Log.i("DEBUG", "opening");
//                    openWinnerDialog();
                    gameHeader.setText(checkerGame.getWinningPlayer() + " Won!");
                    checkerGame.setWinningPlayerColor("None");
//                    startActivity(new Intent(getApplicationContext() , MainActivity.class));
                    roomRef.removeValue();
                }
            });

        }
    }
    public void winfunc(String player1, String player2, String winner){
        Database db = new Database();
        db.addMatchToDatabase(player1, player2, winner, new Database.FirebaseDecRefCallback() {
            @Override
            public void onCallBack(DocumentReference docRef) {
                if(mAuth.getCurrentUser().getUid().equals(player1)) {
                    db.addMatchToUser(player1, docRef.getPath(), new Database.FirebaseBooleanCallback() {
                        @Override
                        public void onCallBack(boolean bool) {
                            if(winner.equals(player1)) {
                                Log.i("DB", "ADDING WIN");
                                db.addWin(player1, new Database.FirebaseBooleanCallback() {
                                    @Override
                                    public void onCallBack(boolean bool) {}
                                });
                            }
                            else {
                                Log.i("DB", "ADDING LOSS");
                                db.addLoss(player1, new Database.FirebaseBooleanCallback() {
                                    @Override
                                    public void onCallBack(boolean bool) {}
                                });
                            }
                        }
                    });
                }
                else{
                    db.addMatchToUser(player2, docRef.getPath(), new Database.FirebaseBooleanCallback() {
                        @Override
                        public void onCallBack(boolean bool) {
                            if(winner.equals(player2)) {
                                db.addWin(player2, new Database.FirebaseBooleanCallback() {
                                    @Override
                                    public void onCallBack(boolean bool) {}
                                });
                            }
                            else {
                                db.addLoss(player2, new Database.FirebaseBooleanCallback() {
                                    @Override
                                    public void onCallBack(boolean bool) {}
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}