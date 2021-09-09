package com.mobdeve.s18.cuevas.alfonso.legitcheckers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.R;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.FriendModel;

import java.util.ArrayList;


public class FriendAdapter extends RecyclerView.Adapter{

    private ArrayList<FriendModel> friends;
    private Context context;

    public FriendAdapter(ArrayList<FriendModel> friends, Context context){
        this.friends = friends;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item,
                parent, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return friends.size();
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
