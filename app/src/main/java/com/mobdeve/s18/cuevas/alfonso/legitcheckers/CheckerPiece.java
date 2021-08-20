package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

public final class CheckerPiece {

    private final int col;
    private final int row;
    private final Player player;

    public CheckerPiece(int col, int row, Player player) {
        super();
        this.col = col;
        this.row = row;
        this.player = player;
    }
    public final int getCol() {
        return this.col;
    }

    public final int getRow() {
        return this.row;
    }

    public final Player getPlayer() {
        return this.player;
    }
}
