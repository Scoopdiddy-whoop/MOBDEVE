package com.mobdeve.s18.cuevas.alfonso.legitcheckers.model;

public class Friend {
    public Friend(String username, boolean friends, String id) {
        this.username = username;
        this.friends = friends;
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFriends() {
        return friends;
    }

    public void setFriends(boolean friends) {
        this.friends = friends;
    }

    private String username;
    private boolean friends;
    private String id;

}
