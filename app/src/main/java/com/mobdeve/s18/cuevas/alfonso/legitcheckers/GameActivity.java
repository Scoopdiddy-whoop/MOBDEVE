package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityGameBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.BoardView;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.CheckerGame;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.CheckerPiece;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.Player;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.Square;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.util.StoragePreferences;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);

        storagePreferences = new StoragePreferences(getApplicationContext());
        background = findViewById(R.id.gamebg);
        checkerGame = new CheckerGame();
        boardView = findViewById(R.id.board);
        boardView.setPiecePosition((PiecePosition)this);
        playerScore = findViewById(R.id.player);
        enemyScore = findViewById(R.id.enemy);
        ImageButton btn = findViewById(R.id.btn_menu);
        musicIntent = new Intent(GameActivity.this, BackgroundSoundService.class);
        btn.setOnClickListener(v -> {
            openMenuDialog();
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

        enemyScore.setText("Enemy: " + (12 - checkerGame.getNumPieces(Player.WHITE)));
        playerScore.setText("Player: " + (12 - checkerGame.getNumPieces(Player.BLACK)));
        if(checkerGame.getWinningPlayer()!=null) {
            Log.i("TAG", checkerGame.getWinningPlayer() + " WON THE GAME!!!");
            openWinnerDialog();
        }
        boardView.invalidate();
    }
    
}