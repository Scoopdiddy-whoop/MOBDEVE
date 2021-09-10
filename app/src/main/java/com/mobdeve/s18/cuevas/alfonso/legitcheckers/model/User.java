package com.mobdeve.s18.cuevas.alfonso.legitcheckers.model;

import java.util.HashMap;
import java.util.Map;       

public class User {
    private String id;
    private Map<String, String> friendlist;
    private Map<String, String> matches;
    private String username;
    private int wins;
    private int losses;

    public User(String id, Map friendlist,
                Map matches, String username, int wins, int losses){
        this.id = id;
        this.friendlist = friendlist;
        this.matches = matches;
        this.username = username;
        this.wins = wins;
        this.losses = losses;
    }
    public User(String id, String username) {
        this.id = id;
        this.username = username;
        this.friendlist = new HashMap<>();
        this.matches = new HashMap<>();
        this.wins = 0;
        this.losses = 0;
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

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLoss(int loss) {
        this.losses = loss;
    }


}
