package com.mobdeve.s18.cuevas.alfonso.legitcheckers.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private FirebaseFirestore db;

    public Database() {
        db = FirebaseFirestore.getInstance();
    }
    public void addUser(User user){
        db.collection("users").document(user.getId()).set(new HashMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("DATABASE", "Account added");
                updateFriendList(user.getId(), user.getFriendlist());
                updateMatches(user.getId(), user.getMatches());
                updateUsername(user.getId(), user.getUsername());
                updateWins(user.getId(), user.getWins());
                updateLosses(user.getId(), user.getLosses());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("DATABASE", "Account not added");
            }
        });
    }

    public void addFriend(String user, String friendID, FirebaseBooleanCallback firebaseBooleanCallback){
        if(user == friendID){
            return;
        }
        getFriendList(user, new FirebaseMapCallback() {
            @Override
            public void onCallBack(Map<String, String> map) {
                map.put("friendID" + map.size() + 1, friendID);
                db.collection("users").document(user).update("friendlist", map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getUsername(friendID, new FirebaseStringCallback() {
                            @Override
                            public void onCallBack(String string) {
                                firebaseBooleanCallback.onCallBack(true);
                                Log.i("DATABASE", "Friend added to friend list");
                                FirebaseDatabase fdb = FirebaseDatabase.getInstance("https://legitcheckers-default-rtdb.asia-southeast1.firebasedatabase.app");
                                fdb.getReference("users/"+user+"/friends/"+friendID+"/username").setValue(string);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DATABASE", "Error adding friend", e);
                        firebaseBooleanCallback.onCallBack(false);
                    }
                });
            }
        });
    }
    public boolean updateFriendList(String user, Map friendList) {
        final boolean[] valid = new boolean[1];

        db.collection("users").document(user).update("friendlist", friendList).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void getFriendList(String user, FirebaseMapCallback firebaseMapCallback){
        DocumentReference docRef = db.collection("users").document(user);
        final Map<String, String>[] friendlist = new Map[1];

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    friendlist[0] = (Map)document.getData().get("friendlist");
                    Log.i("DATABASE", "Friendlist data: " + friendlist[0]);
                    firebaseMapCallback.onCallBack(friendlist[0]);

                } else {
                    Log.i("DATABASE", "No friendlist data");
                }
            }
        });
    }
    public void findFriend(String user, String friend, FirebaseBooleanCallback firebaseBooleanCallback){
        getFriendList(user, new FirebaseMapCallback() {
            @Override
            public void onCallBack(Map<String, String> map) {
               firebaseBooleanCallback.onCallBack(map.containsValue(user));
            }
        });
    }
    public void addMatchToUser(String user, String matchID, FirebaseBooleanCallback firebaseBooleanCallback){
        getMatches(user, new FirebaseMapCallback() {
            @Override
            public void onCallBack(Map<String, String> matches) {
                matches.put("matchID" + matches.size() + 1,  matchID);
                final boolean[] valid = new boolean[1];
                db.collection("users").document(user).update("matches", matches).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        valid[0] = true;
                        Log.i("DATABASE", "Match added to match list");
                        firebaseBooleanCallback.onCallBack(true);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        valid[0] = false;
                        Log.i("DATABASE", "Error adding match", e);
                        firebaseBooleanCallback.onCallBack(false);
                    }
                });
            }
        });
    }
    public boolean updateMatches(String user, Map matches) {
        Map<String, String> updatedList = matches;
        final boolean[] valid = new boolean[1];

        db.collection("users").document(user).update("matches", updatedList).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public Map getMatches(String user, FirebaseMapCallback firebaseMapCallback){
        DocumentReference docRef = db.collection("users").document(user);
        final Map<String, String>[] matches = new Map[1];

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    matches[0] = (Map) document.getData().get("matches");
                    Log.i("DATABASE", "Matches data: " + matches[0]);
                    firebaseMapCallback.onCallBack(matches[0]);
                } else {
                    Log.i("DATABASE", "No Matches data");
                }
            }
        });
        return matches[0];
    }
    public String getUsername(String user, FirebaseStringCallback firebaseStringCallback) {
        DocumentReference docRef = db.collection("users").document(user);
        final String[] username = new String[1];

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    username[0] = (String) document.getData().get("username");
                    Log.d("DATABASE", "Username data: " + username[0]);
                    firebaseStringCallback.onCallBack(username[0]);
                } else {
                    Log.d("DATABASE", "No username data");
                }
            }
        });
        return username[0];
    }
    public boolean updateUsername(String user, String username) {
        String updatedUsername = username;
        final boolean[] valid = new boolean[1];

        db.collection("users").document(user).update("username", updatedUsername).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void addWin(String user, FirebaseBooleanCallback firebaseBooleanCallback) {
        getWins(user, new FirebaseIntCallback() {
            @Override
            public void onCallBack(int wins) {
                wins+=1;
                db.collection("users").document(user).update("wins", wins).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("DATABASE", "Wins has been increased by 1");
                        firebaseBooleanCallback.onCallBack(true);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DATABASE", "Error adding to wins", e);
                        firebaseBooleanCallback.onCallBack(false);
                    }
                });
            }
        });
    }
    public void getWins(String user, FirebaseIntCallback firebaseIntCallback) {
        DocumentReference docRef = db.collection("users").document(user);

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Long wins;
                    wins = (Long)document.getData().get("wins");
                    Log.i("DATABASE", "Wins data: " + wins.intValue());
                    firebaseIntCallback.onCallBack(wins.intValue());
                } else {
                    Log.i("DATABASE", "No wins data");
                }
            }
        });
    }
    public void updateWins(String user, int wins) {
        int updatedWins = wins;

        db.collection("users").document(user).update("wins", updatedWins).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("DATABASE", "Wins has been updated ");
            }

        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("DATABASE", "Error in updating wins", e);
            }
        });
    }
    public void addLoss(String user, FirebaseBooleanCallback firebaseBooleanCallback){
        getLosses(user, new FirebaseIntCallback() {
            @Override
            public void onCallBack(int losses) {
                losses+=1;
                db.collection("users").document(user).update("losses", losses).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("DATABASE", "Losses has been increased by 1");
                        firebaseBooleanCallback.onCallBack(true);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DATABASE", "Error adding to losses", e);
                        firebaseBooleanCallback.onCallBack(false);
                    }
                });
            }
        });
    }
    public void getLosses(String user, FirebaseIntCallback firebaseIntCallback) {
        DocumentReference docRef = db.collection("users").document(user);
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Long losses;
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    losses = (Long) document.getData().get("losses");
                    Log.i("DATABASE", "Losses data: " + losses.intValue());
                    firebaseIntCallback.onCallBack(losses.intValue());
                } else {
                    Log.i("DATABASE", "No Losses data");
                }
            }
        });
    }
    public void updateLosses(String user, int losses) {
        int updatedLosses = losses;

        db.collection("users").document(user).update("losses", updatedLosses).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("DATABASE", "Losses has been updated ");
            }

        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("DATABASE", "Error in updating Losses", e);
            }
        });
    }
    public void getMatchWinner(String matchID, FirebaseStringCallback firebaseStringCallback){
        DocumentReference docRef = db.collection("match").document(matchID);
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("DATABASE", "Match winner data: " + (String) document.getData().get("winner"));
                    firebaseStringCallback.onCallBack((String) document.getData().get("winner"));
                } else {
                    Log.d("DATABASE", "No Match winner data");
                }
            }
        });
    }
    public void getMatchPlayer1(String matchID, FirebaseStringCallback firebaseStringCallback){
        DocumentReference docRef = db.collection("match").document(matchID);
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("DATABASE", "Match player1 data: " + (String) document.getData().get("player1"));
                    firebaseStringCallback.onCallBack((String) document.getData().get("player1"));
                } else {
                    Log.d("DATABASE", "No Match player1 data");
                }
            }
        });
    }
    public void getMatchPlayer2(String matchID, FirebaseStringCallback firebaseStringCallback){
        DocumentReference docRef = db.collection("match").document(matchID);
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("DATABASE", "Match player2 data: " + (String) document.getData().get("player2"));
                    firebaseStringCallback.onCallBack((String) document.getData().get("player2"));
                } else {
                    Log.d("DATABASE", "No Match player2 data");
                }
            }
        });
    }
    public void addMatchToDatabase(String player1, String player2, String winner, FirebaseBooleanCallback firebaseBooleanCallback) {
        Map<String, String> data = new HashMap<>();
        data.put("player1", player1);
        data.put("player2", player2);
        data.put("winner", winner);
        db.collection("match").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.i("DATABASE", "MATCH ADDED TO DATABASE");
                addMatchToUser(player1, documentReference.getId(), new FirebaseBooleanCallback() {
                    @Override
                    public void onCallBack(boolean bool) {
                        addMatchToUser(player2, documentReference.getId(), new FirebaseBooleanCallback() {
                            @Override
                            public void onCallBack(boolean bool) {
                                if(winner.equals(player1)){
                                    addWin(player1, new FirebaseBooleanCallback() {
                                        @Override
                                        public void onCallBack(boolean bool) {
                                            addLoss(player2, new FirebaseBooleanCallback() {
                                                @Override
                                                public void onCallBack(boolean bool) {
                                                    if(bool)
                                                        firebaseBooleanCallback.onCallBack(true);
                                                    else
                                                        firebaseBooleanCallback.onCallBack(false);
                                                }
                                            });
                                        }
                                    });
                                }
                                else{
                                    addWin(player2, new FirebaseBooleanCallback() {
                                        @Override
                                        public void onCallBack(boolean bool) {
                                            addLoss(player1, new FirebaseBooleanCallback() {
                                                @Override
                                                public void onCallBack(boolean bool) {
                                                    if(bool)
                                                        firebaseBooleanCallback.onCallBack(true);
                                                    else
                                                        firebaseBooleanCallback.onCallBack(false);
                                                }
                                            });
                                        }
                                    });

                                }

                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("DATABASE", "FAILED TO ADD MATCH");
                firebaseBooleanCallback.onCallBack(false);
            }
        });
    }
    public interface FirebaseMapCallback{
        void onCallBack(Map<String, String> map);
    }
    public interface FirebaseStringCallback{
        void onCallBack(String string);
    }
    public interface FirebaseIntCallback{
        void onCallBack(int number);
    }
    public interface FirebaseBooleanCallback{
        void onCallBack(boolean bool);
    }
}

