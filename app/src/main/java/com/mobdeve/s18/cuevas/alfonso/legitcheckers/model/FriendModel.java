package com.mobdeve.s18.cuevas.alfonso.legitcheckers.model;

public class FriendModel {
    private String username;
    private boolean friend;

    public FriendModel(String username, boolean friend){
        this.username = username;
        this.friend = friend;
    }
    public String getUsername() {
        return username;
    }
    public boolean isFriend() {
        return friend;
    }
}
