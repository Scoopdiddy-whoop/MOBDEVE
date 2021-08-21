package com.mobdeve.s18.cuevas.alfonso.legitcheckers.model;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;


    public Database() {
        db = FirebaseFirestore.getInstance();
    }
    public boolean addUser(User user){
        db.collection("users").document(user.getId());

        return false;
    }
    public boolean addFriend(User user, String friendID){
        Map friendlist = getFriendList(user);
        friendlist.put("friendID", friendID);
        final boolean[] valid = new boolean[1];

        db.collection("users").document(user.getId()).update("friendlist", friendlist).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                valid[0] = true;
                Log.i("DATABASE", "Friend added to friend list");
            }

        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                valid[0] = false;
                Log.i("DATABASE", "Error adding friend", e);
            }
        });

        return valid[0];
    }
    public boolean updateFriendList(User user, Map friendList) {
        Map friendlist = user.getFriendlist();
        final boolean[] valid = new boolean[1];

        db.collection("users").document(user.getId()).update("friendlist", friendlist).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                valid[0] = true;
                Log.i("DATABASE", "Friend list has been updated ");
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        valid[0] = false;
                        Log.i("DATABASE", "Error in updated friend list", e);
                    }
                });

        return valid[0];

    }
    public Map getFriendList(User user){
        DocumentReference docRef = db.collection("users").document(user.getId());
        final Map[] friendlist = new Map[1];

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    friendlist[0] = (Map) document.getData().get("friendlist");
                    Log.d("DATABASE", "DocumentSnapshot data: " + document.getData());

                } else {
                    Log.d("DATABASE", "No such document");
                }
            }
        });
        return friendlist[0];
    }
}

