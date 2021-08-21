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
        Map updatedList = friendList;
        final boolean[] valid = new boolean[1];

        db.collection("users").document(user.getId()).update("friendlist", updatedList).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public boolean addMatch(User user, String matchID){
        Map matches = getMatches(user);
        matches.put("matchID",  matchID);
        final boolean[] valid = new boolean[1];

        db.collection("users").document(user.getId()).update("matches", matches).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                valid[0] = true;
                Log.i("DATABASE", "Match added to match list");
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                valid[0] = false;
                Log.i("DATABASE", "Error adding match", e);
            }
        });

        return valid[0];
    }
    public boolean updateMatches(User user, Map matches) {
        Map updatedList = matches;
        final boolean[] valid = new boolean[1];

        db.collection("users").document(user.getId()).update("matches", updatedList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                valid[0] = true;
                Log.i("DATABASE", "match list has been updated ");
            }

        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                valid[0] = false;
                Log.i("DATABASE", "Error in updating match list", e);
            }
        });

        return valid[0];

    }
    public Map getMatches(User user){
        DocumentReference docRef = db.collection("users").document(user.getId());
        final Map[] matches = new Map[1];

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    matches[0] = (Map) document.getData().get("matches");
                    Log.d("DATABASE", "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d("DATABASE", "No such document");
                }
            }
        });
        return matches[0];
    }
    public String getUsername(User user) {
        DocumentReference docRef = db.collection("users").document(user.getId());
        final String[] username = new String[1];

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    username[0] = (String) document.getData().get("username");
                    Log.d("DATABASE", "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d("DATABASE", "No such document");
                }
            }
        });
        return username[0];
    }
    public boolean updateUsername(User user, String username) {
        String updatedUsername = username;
        final boolean[] valid = new boolean[1];

        db.collection("users").document(user.getId()).update("username", updatedUsername).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                valid[0] = true;
                Log.i("DATABASE", "username has been updated ");
            }

        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                valid[0] = false;
                Log.i("DATABASE", "Error in updating username", e);
            }
        });

        return valid[0];
    }

}

