package com.mobdeve.s18.cuevas.alfonso.legitcheckers.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.HistoryActivity;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.R;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Friend;

import java.util.ArrayList;


public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>{

    private ArrayList<Friend> friends;
    private Context context;

    public FriendAdapter(ArrayList<Friend> friends, Context context){
        this.friends = friends;
        this.context = context;
        Log.i("FRIENDADAPTER", "WORKING");

    }

    @Override
    public void onBindViewHolder(FriendAdapter.FriendViewHolder holder, int position) {
        Log.i("FRIENDADAPTER", "WORKING ONBIND");

        Database db = new Database();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        Friend friend = friends.get(position);
        if(friend.isFriends()){
            holder.tv_username.setText(friend.getUsername());
            holder.btn_action.setText("VIEW");
            holder.btn_action.setOnClickListener(v ->{
                String friendID = friends.get(position).getId();
                Intent intent = new Intent(context, HistoryActivity.class);
                intent.putExtra("friendID", friendID);
                context.startActivity(intent);
                ((Activity)context).finish();
            });
        }
        else{
            holder.tv_username.setText(friend.getUsername());
            holder.btn_action.setText("ADD");
            holder.btn_action.setOnClickListener(v->{
                db.addFriend(user.getUid(), friend.getId(), new Database.FirebaseBooleanCallback() {
                    @Override
                    public void onCallBack(boolean bool) {
                        if(bool){
                            holder.tv_username.setText(friend.getUsername());
                            holder.btn_action.setText("VIEW");
                            holder.btn_action.setOnClickListener(v ->{
                                String friendID = friends.get(position).getId();
                                Intent intent = new Intent(context, HistoryActivity.class);
                                intent.putExtra("friendID", friendID);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            });
                        }
                    }
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item,
                parent, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);
        return friendViewHolder;
    }

//    @Override
//    public void onBindViewHolder(com.mobdeve.s18.cuevas.alfonso.exercise2.adapter.PostAdapter.PostViewHolder holder, int position) {
//        holder.tv_username_head.setText(arr_post.get(position).getUsername());
//        holder.tv_username_bot.setText(arr_post.get(position).getUsername());
//        holder.tv_caption.setText(arr_post.get(position).getCaption());
//        holder.tv_location.setText(arr_post.get(position).getLocation());
//        holder.tv_date.setText(arr_post.get(position).getDatePosted());
//
//        holder.iv_user.setImageResource(arr_post.get(position).getUserImageId());
//        holder.iv_mainpic.setImageResource(arr_post.get(position).getImageId());
//
//        if(arr_post.get(position).getLiked())
//            holder.ib_heart.setImageResource(R.drawable.heart_red);
//        else
//            holder.ib_heart.setImageResource(R.drawable.heart);
//
//        if(arr_post.get(position).getLocation() == null ||
//                arr_post.get(position).getLocation().isEmpty()) {
//            holder.tv_location.setVisibility(View.GONE);
//            holder.tv_username_head.setGravity(Gravity.CENTER_VERTICAL);
//            holder.ll_head.setWeightSum(0);
//        }
//
//        if(arr_post.get(position).getCaption() == null ||
//                arr_post.get(position).getCaption().isEmpty())
//            holder.ll_botcon.removeAllViews();
//
//        holder.ib_heart.setOnClickListener( v -> {
//            liked = !arr_post.get(position).getLiked();
//            arr_post.get(position).setLiked(liked);
//
//            if(liked)
//                holder.ib_heart.setImageResource(R.drawable.heart_red);
//            else
//                holder.ib_heart.setImageResource(R.drawable.heart);
//        });
//    }

    protected class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView tv_username;
        Button btn_action;

        public FriendViewHolder(View view){
            super(view);
            tv_username = view.findViewById(R.id.tv_username);
            btn_action = view.findViewById(R.id.btn_action);
        }
    }
}
