package com.mobdeve.s18.cuevas.alfonso.legitcheckers.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private Map<String, Object> friendlist;
    private Map<String, Object> matches;
    private String username;

    public User(String id, Map friendlist,
                Map matches, String username){
        this.id = id;
        this.friendlist = friendlist;
        this.matches = matches;
        this.username = username;
    }
    public User(String id, String username) {
        this.id = id;
        this.username = username;
        this.friendlist = new HashMap<>();
        this.matches = new HashMap<>();
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

    public Map getMatches() {
        return matches;
    }

    public void setMatches(Map matches) {
        this.matches = matches;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
