package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.CheckerPiece;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.Square;

public interface PiecePosition {

    CheckerPiece pieceAt(Square var1);
    void movePiece(Square var1,Square var2);

}

