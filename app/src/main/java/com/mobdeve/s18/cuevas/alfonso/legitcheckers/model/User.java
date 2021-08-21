package com.mobdeve.s18.cuevas.alfonso.legitcheckers.model;

import java.util.Map;

public class User {
    private String id;
    private Map friendlist;
    private String[] matches;
    private String username;

    public User(String id, Map friendlist,
                String[] matches, String username){
        this.id = id;
        this.friendlist = friendlist;
        this.matches = matches;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map getFriendlist() {
        return friendlist;
    }

    public void setFriendlist(Map friendlist) {
        this.friendlist = friendlist;
    }

    public String[] getMatches() {
        return matches;
    }

    public void setMatches(String[] matches) {
        this.matches = matches;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
