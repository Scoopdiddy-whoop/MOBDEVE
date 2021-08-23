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
        final boolean added[] = new boolean[1];
        db.collection("users").document(user.getId()).set(new HashMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("DATABASE", "Account added");
                updateFriendList(user, user.getFriendlist());
                updateMatches(user, user.getMatches());
                updateUsername(user, user.getUsername());

                added[0]=true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("DATABASE", "Account not added");
                added[0]=false;
            }
        });

        return added[0];
    }

    public void addFriend(User user, String friendID){
        getFriendList(user, new FirebaseMapCallback() {
            @Override
            public void onCallBack(Map<String, Object> map) {
                map.put("friendID", friendID);
                db.collection("users").document(user.getId()).update("friendlist", map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("DATABASE", "Friend added to friend list");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DATABASE", "Error adding friend", e);
                    }
                });
            }
        });
    }
    public boolean updateFriendList(User user, Map friendList) {
        final boolean[] valid = new boolean[1];

        db.collection("users").document(user.getId()).update("friendlist", friendList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                valid[0] = true;
                Log.i("DATABASE", "Friend list has been updated ");a
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
    public Map getFriendList(User user, FirebaseMapCallback firebaseMapCallback){
        DocumentReference docRef = db.collection("users").document(user.getId());
        final Map<String, Object>[] friendlist = new Map[1];

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    friendlist[0] = (Map) document.getData().get("friendlist");
                    Log.i("DATABASE", "Friendlist data: " + friendlist[0]);
                    firebaseMapCallback.onCallBack(friendlist[0]);

                } else {
                    Log.i("DATABASE", "No friendlist data");
                }
            }
            else{
                Log.i("DATABASE", "it gone done did it");
            }
        });
        return friendlist[0];
    }
    public boolean addMatch(User user, String matchID){
        Map<String, Object> matches = getMatches(user);
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
        Map<String, Object> updatedList = matches;
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
        final Map<String, Object>[] matches = new Map[1];

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    matches[0] = (Map) document.getData().get("matches");
                    Log.d("DATABASE", "Matches data: " + matches[0]);
                } else {
                    Log.d("DATABASE", "No Matches data");
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
                    Log.d("DATABASE", "Username data: " + username[0]);
                } else {
                    Log.d("DATABASE", "No username data");
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
    public interface FirebaseMapCallback{
        void onCallBack(Map<String, Object> map);
    }
}

