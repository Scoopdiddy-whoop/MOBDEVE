package com.mobdeve.s18.cuevas.alfonso.legitcheckers.model;

public class Leader {
    private String username;
    private int wins;

    public Leader(String username, int wins) {
        this.username = username;
        this.wins = wins;
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
}
