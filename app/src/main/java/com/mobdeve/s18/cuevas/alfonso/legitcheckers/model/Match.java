package com.mobdeve.s18.cuevas.alfonso.legitcheckers.model;

public class Match {
    private String idPlayer1;
    private String idPlayer2;
    private String matchID;
    private String winner;

    public Match(String idPlayer1, String idPlayer2, String matchID, String winner) {
        this.idPlayer1 = idPlayer1;
        this.idPlayer2 = idPlayer2;
        this.matchID = matchID;
        this.winner = winner;
    }

    public String getIdPlayer1() {
        return idPlayer1;
    }

    public void setIdPlayer1(String idPlayer1) {
        this.idPlayer1 = idPlayer1;
    }

    public String getIdPlayer2() {
        return idPlayer2;
    }

    public void setIdPlayer2(String idPlayer2) {
        this.idPlayer2 = idPlayer2;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
