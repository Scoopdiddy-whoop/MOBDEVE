package com.mobdeve.s18.cuevas.alfonso.legitcheckers.game;

public final class CheckerPiece {

    private final int col;
    private final int row;
    private final String player;
    private final boolean isKing;

    public CheckerPiece(int col, int row, String player, boolean isKing) {
        super();
        this.col = col;
        this.row = row;
        this.player = player;
        this.isKing = isKing;
    }
    public final int getCol() {
        return this.col;
    }

    public final int getRow() {
        return this.row;
    }

    public final String getPlayer() {
        return this.player;
    }

    public boolean isKing() { return this.isKing; }
}
