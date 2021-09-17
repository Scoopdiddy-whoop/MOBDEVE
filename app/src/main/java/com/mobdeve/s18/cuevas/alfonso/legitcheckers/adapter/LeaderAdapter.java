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
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Leader;

import java.util.ArrayList;


public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.LeaderViewHolder>{

    private ArrayList<Leader> leaders;
    private Context context;

    public LeaderAdapter(ArrayList<Leader> leaders, Context context) {
        this.leaders = leaders;
        this.context = context;
        Log.i("MATCHDADAPTER", "WORKING");
    }

    @Override
    public void onBindViewHolder(LeaderAdapter.LeaderViewHolder holder, int position) {
        Log.i("MATCHADAPTER", "WORKING ONBIND");

        Database db = new Database();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        Log.i("LEAD", "ADAPT: " + leaders.get(position).getUsername());
        Log.i("LEAD", "ADAPT: " + leaders.get(position).getWins());
        holder.tv_user.setText(leaders.get(position).getUsername());
        holder.tv_wins.setText(leaders.get(position).getWins()+"");
    }
    @Override
    public int getItemCount() {
        return leaders.size();
    }
    @Override
    public LeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_item,
                parent, false);
        LeaderViewHolder leaderViewHolder = new LeaderViewHolder(view);
        return leaderViewHolder;
    }

    protected class LeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user;
        TextView tv_wins;

        public LeaderViewHolder(View view){
            super(view);
            tv_user = view.findViewById(R.id.tv_user);
            tv_wins = view.findViewById(R.id.tv_wins);
        }
    }
}
