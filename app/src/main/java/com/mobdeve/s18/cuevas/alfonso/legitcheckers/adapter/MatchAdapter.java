package com.mobdeve.s18.cuevas.alfonso.legitcheckers.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.R;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Match;

import java.util.ArrayList;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder>{

    private ArrayList<String> matches;
    private Context context;

    public MatchAdapter(ArrayList<String> matches, Context context) {
        this.matches = matches;
        this.context = context;
        Log.i("MATCHDADAPTER", "WORKING");
    }

    @Override
    public void onBindViewHolder(MatchAdapter.MatchViewHolder holder, int position) {
        Log.i("MATCHADAPTER", "WORKING ONBIND");

        Database db = new Database();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        db.getMatchWinner(matches.get(position), new Database.FirebaseStringCallback() {
            @Override
            public void onCallBack(String winner) {
                db.getMatchPlayer1(matches.get(position), new Database.FirebaseStringCallback() {
                    @Override
                    public void onCallBack(String player1) {
                        db.getMatchPlayer2(matches.get(position), new Database.FirebaseStringCallback() {
                            @Override
                            public void onCallBack(String player2) {
                                Match match = new Match(player1, player2, matches.get(position), winner);

                                db.getUsername(player1, new Database.FirebaseStringCallback() {
                                    @Override
                                    public void onCallBack(String usernameP1) {
                                        db.getUsername(player2, new Database.FirebaseStringCallback() {
                                            @Override
                                            public void onCallBack(String usernameP2) {
                                                Log.i("MATCH", "USERID: " + user.getUid());
                                                Log.i("MATCH", "WINNER: " + winner);
                                                Log.i("MATCH", "Player1: " + usernameP1);
                                                Log.i("MATCH", "Player2: " + usernameP2);
                                                if(user.getUid().equals(winner)) {
                                                    holder.tv_winner.setText("VICTORY");
                                                }
                                                else {
                                                    holder.tv_winner.setText("DEFEAT");
                                                }
                                                if(player1.equals(user.getUid())){
                                                    holder.tv_opponent.setText(new StringBuilder().append("vs ").append(usernameP2).toString());
                                                }
                                                else {
                                                    holder.tv_opponent.setText(new StringBuilder().append("vs").append(usernameP1).toString());
                                                }
                                            }
                                        });
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
    public int getItemCount() {
        return matches.size();
    }
    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_item,
                parent, false);
        MatchViewHolder matchViewHolder = new MatchViewHolder(view);
        return matchViewHolder;
    }

    protected class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView tv_winner;
        TextView tv_opponent;

        public MatchViewHolder(View view){
            super(view);
            tv_winner = view.findViewById(R.id.tv_winner);
            tv_opponent = view.findViewById(R.id.tv_opponent);
        }
    }
}
