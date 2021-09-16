package com.mobdeve.s18.cuevas.alfonso.legitcheckers.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class CheckerGame {
    private ArrayList piecesBox;
    private String currentPlayer;
    private String winningPlayer;
    private int val;

    public final void clear() {
        piecesBox.clear();
    }

    public ArrayList<CheckerPiece> getPiecesBox() {
        return piecesBox;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setPiecesBox(ArrayList<CheckerPiece> piecesBox) {
        this.piecesBox = piecesBox;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
        if(this.currentPlayer.equals("Black")) {
            this.val = 1;
            Log.i("TAG", "ValBlack: " + val);
        }
        else {
            this.val = -1;
            Log.i("TAG", "ValWhite: " + val);
        }
    }


    public boolean canMove(Square from, Square to){
        int colDist, rowDist;
        boolean colValid, rowValid;
        colDist = from.getCol() - to.getCol();
        rowDist = from.getRow() - to.getRow();
        Log.i("TAG", "canMove: IN" + colDist + rowDist);

        colValid = ((colDist==1) || (colDist==-1));
        Log.i("TAG", "colValid: " + colValid);

        /*If King Piece, can move forward and back, if not only forward*/
        if(pieceAt(from.getCol(), from.getRow()).isKing()){
            Log.i("TAG", "isKing: TRUE");
            rowValid = ((rowDist==1) || (rowDist==-1));
        }
        else {
            if (pieceAt(from.getCol(), from.getRow()).getPlayer().equals("Black")) {
                rowValid = (rowDist == -1);
                Log.i("TAG", "rowValidBLACK: " + rowValid);
            }
            else{
                rowValid = (rowDist == 1);
                Log.i("TAG", "rowValidWHITE: " + rowValid);
            }
        }

        if(colValid && rowValid){
            Log.i("TAG", "isValid: TRUE");
            if(pieceAt(to.getCol(), to.getRow()) == null)
                return true;
        }

        return false;
    }

    public boolean isOnEdge(CheckerPiece piece){
        if(piece.getRow() == 0 || piece.getRow() == 7)
            return true;
        if(piece.getCol() == 0 || piece.getCol() == 7)
            return true;

        return false;
    }

    public boolean turnKing(Square to){
        if(currentPlayer.equals("White") && to.getRow() == 0)
            return true;
        if(currentPlayer.equals("Black") && to.getRow() == 7)
            return true;
        return false;
    }

    public int canEatRight(Square from){

        CheckerPiece pieceAtRight;
        pieceAtRight = pieceAt(from.getCol() + 1, from.getRow() + val);
        if(pieceAtRight!=null){
            if(!pieceAtRight.getPlayer().equals(currentPlayer))
                Log.i("TAG", "Enemy spotted: "+ pieceAtRight.getPlayer() + pieceAtRight.getCol() + pieceAtRight.getRow() + "\nBy: "+ currentPlayer + from.getCol() + from.getRow());
        }


        if(pieceAtRight!=null && !isOnEdge(pieceAtRight)){
            if(!pieceAtRight.getPlayer().equals(currentPlayer)){
                Log.i("TAG", "ENEMY PIECE AT RIGHT");
                if(pieceAt(pieceAtRight.getCol() + 1,pieceAtRight.getRow() + val)==null){
                    Log.i("TAG", "HULI KA 5");
                    return 1;
                }
            }
        }
        if(pieceAt(from.getCol(), from.getRow())==null){
            Log.i("TAG", "NULL AFTER EATING " + from.getCol() + from.getRow());
        }
        if(pieceAt(from.getCol(), from.getRow())!=null) {
            if (pieceAt(from.getCol(), from.getRow()).isKing()) {
                pieceAtRight = pieceAt(from.getCol() + 1, from.getRow() - val);

                if (pieceAtRight != null && !isOnEdge(pieceAtRight)) {
                    if (!pieceAtRight.getPlayer().equals(currentPlayer)) {
                        if (pieceAt(pieceAtRight.getCol() + 1, pieceAtRight.getRow() - val) == null) {
                            Log.i("TAG", "HULI KA 7");
                            return 2;
                        }
                    }
                }
            }
        }

        return -99;
    }

    public int canEatLeft(Square from){

        CheckerPiece pieceAtLeft;

        pieceAtLeft = pieceAt(from.getCol() - 1, from.getRow() + val);

        if(pieceAtLeft!=null && !isOnEdge(pieceAtLeft)){
            if(!pieceAtLeft.getPlayer().equals(currentPlayer)){
                if(pieceAt(pieceAtLeft.getCol() - 1,pieceAtLeft.getRow() + val)==null){
                    Log.i("TAG", "HULI KA 2");
                    return 1;
                }
            }
        }
        if(pieceAt(from.getCol(), from.getRow())!=null) {
            if (pieceAt(from.getCol(), from.getRow()).isKing()) {
                pieceAtLeft = pieceAt(from.getCol() - 1, from.getRow() - val);
                if (pieceAtLeft != null && !isOnEdge(pieceAtLeft)) {
                    if (!pieceAtLeft.getPlayer().equals(currentPlayer)) {
                        if (pieceAt(pieceAtLeft.getCol() - 1, pieceAtLeft.getRow() - val) == null) {
                            Log.i("TAG", "HULI KA 4");
                            return 2;
                        }
                    }
                }
            }
        }

        return -99;
    }

    public CheckerPiece availEat(){
        Iterator pieces= piecesBox.iterator();
        CheckerPiece piece;
        Square from;
        do{

            piece = (CheckerPiece)pieces.next();
            if(currentPlayer.equals(piece.getPlayer())) {
                from = new Square(piece.getCol(), piece.getRow());
                if(canEatRight(from)!=-99 || canEatLeft(from)!=-99) {
                    Log.i("TAG", "gottem " + piece.getCol() + piece.getRow());
                    return piece;
                }
            }

        }while(pieces.hasNext());
        return null;
    }

    public void movePiece(Square from, Square to) {
        boolean isCurrKing =  pieceAt(from.getCol(), from.getRow()).isKing();
        Log.i("TAG", "movePiece:" + from.getCol() + from.getRow());
        Log.i("TAG", "Before Move: " + currentPlayer);
        if(currentPlayer.equals(pieceAt(from.getCol(), from.getRow()).getPlayer())) {
            CheckerPiece pieceEater = availEat();
            if (pieceEater == null) {
                Log.i("TAG", "movePiece: CAN MOVE SUCCESS");
                if (canMove(from, to)) {
                    //Log.i("TAG", "movePiece: CAN MOVE SUCCESS");
                    piecesBox.remove(pieceAt(from.getCol(), from.getRow()));
                    if(isCurrKing)
                        addPiece(new CheckerPiece(to.getCol(), to.getRow(), currentPlayer, true));
                    else
                        addPiece(new CheckerPiece(to.getCol(), to.getRow(), currentPlayer, turnKing(to)));
                    if (currentPlayer.equals("Black"))
                        currentPlayer = "White";
                    else
                        currentPlayer = "Black";
                }
            }
            else if (pieceEater != null && pieceEater == pieceAt(from.getCol(), from.getRow())){
                Log.i("TAG", "movePiece: CAN EAT SUCCESS");
                if(canEatLeft(from)!=99){
                    Log.i("TAG", "movePiece: SUCCESSFULLY ATE3 VAL = " + val);

                    if(canEatLeft(from)==2)
                        val *= -1;
                    if((from.getCol() - 2 == to.getCol()) && (from.getRow() + val + val) == to.getRow()) {
                        Log.i("TAG", "movePiece: SUCCESSFULLY ATE4");
                        piecesBox.remove(pieceAt(from.getCol(), from.getRow()));
                        piecesBox.remove(pieceAt(from.getCol() - 1, from.getRow() + val));
                        if(isCurrKing)
                            addPiece(new CheckerPiece(to.getCol(), to.getRow(), currentPlayer, true));
                        else
                            addPiece(new CheckerPiece(to.getCol(), to.getRow(), currentPlayer, turnKing(to)));
                        if (availEat() == null){
                            Log.i("TAG", "movePiece: AVAIL EAT");
                            if (currentPlayer.equals("Black"))
                                currentPlayer = "White";
                            else
                                currentPlayer = "Black";
                        }
                        else{
                            Boolean samePieceCanEat = (availEat().getCol() == to.getCol()) && (availEat().getRow() == to.getRow());
                            if(!samePieceCanEat){
                                Log.i("TAG", "movePiece: SAMEPIECE CANT EAT" + to.getCol() + to.getRow());
                                if (currentPlayer.equals("Black"))
                                    currentPlayer = "White";
                                else
                                    currentPlayer = "Black";
                            }
                        }
                    }
                }
                if(canEatRight(from)!=99){
                    Log.i("TAG", "movePiece: SUCCESSFULLY ATE1 VAL = " + val);
                    if(canEatRight(from)==2) {
                        Log.i("TAG", "movePiece: KINGEATS");
                        val *= -1;
                    }
                    if((from.getCol() + 2 == to.getCol()) && (from.getRow() + val + val) == to.getRow()) {
                        Log.i("TAG", "movePiece: SUCCESSFULLY ATE2");
                        piecesBox.remove(pieceAt(from.getCol(), from.getRow()));
                        piecesBox.remove(pieceAt(from.getCol() + 1, from.getRow() + val));
                        if(isCurrKing) {
                            Log.i("TAG", "movePiece: SUCCESSFULLY MADE KING");
                            addPiece(new CheckerPiece(to.getCol(), to.getRow(), currentPlayer, true));
                        }
                        else {
                            Log.i("TAG", "movePiece: SUCCESSFULLY PLACED PIECE");
                            addPiece(new CheckerPiece(to.getCol(), to.getRow(), currentPlayer, turnKing(to)));
                        }
                        if (availEat() == null){
                            Log.i("TAG", "movePiece: AVAIL EAT");
                            if (currentPlayer.equals("Black"))
                                currentPlayer = "White";
                            else
                                currentPlayer = "Black";
                        }
                        else{
                            Boolean samePieceCanEat = (availEat().getCol() == to.getCol()) && (availEat().getRow() == to.getRow());
                            if(!samePieceCanEat){
                                Log.i("TAG", "movePiece: SAMEPIECE CANT EAT" + to.getCol() + to.getRow());
                                if (currentPlayer.equals("Black"))
                                    currentPlayer = "White";
                                else
                                    currentPlayer = "Black";
                            }
                        }
                    }
                }
            }
            else
                currentPlayer = currentPlayer;

        }
        if(getNumPieces("Black")==0)
            setWinningPlayerColor("White");
        if(getNumPieces("White")==0)
            setWinningPlayerColor("Black");
        Log.i("TAG", "After Move: " + currentPlayer);
    }

    public void addPiece(CheckerPiece piece){
        piecesBox.add(piece);
    };

    public void reset() {
        this.clear();
        int row = 0;
        for (int i = 8; row < i; ++row) {
            int col = 0;
            for (int j = 8; col < j; ++col) {
                boolean positions = (col % 2 == 1 && row % 2 != 1) || (col % 2 == 0 && row % 2 == 1);
                if (row < 3) {
                    if (positions)
                        this.addPiece(new CheckerPiece(col, row, "Black", false));
                }

                if (row > 4) {
                    if (positions)
                        this.addPiece(new CheckerPiece(col, row,"White", false));
                }
            }
        }
    }

    public CheckerPiece pieceAt(Square square){
        /*if(pieceAt(square.getCol(),square.getRow())!=null)
            Log.i("TAG", "player@: col " + (this.pieceAt(square.getCol(),square.getRow())).getCol() + "row " + (this.pieceAt(square.getCol(),square.getRow())).getRow() + "= " + (this.pieceAt(square.getCol(),square.getRow())).getPlayer());*/

        return this.pieceAt(square.getCol(), square.getRow());
    }

    private CheckerPiece pieceAt(int col, int row){
        Iterator pieces= piecesBox.iterator();
        CheckerPiece piece;

        if(piecesBox == null){
            Log.i("TAG", "pieceAt: NULL PIECES BOX");
        }
        do{

            piece = (CheckerPiece)pieces.next();
            if(col == piece.getCol() && row == piece.getRow())
                return piece;

        }while(pieces.hasNext());
        return null;
    }

    public int getNumPieces(String player){
        Iterator pieces= piecesBox.iterator();
        CheckerPiece piece;

        int nPieces=0;

        do{

            piece = (CheckerPiece)pieces.next();
            if(piece.getPlayer().equals(player))
                nPieces++;

        }while(pieces.hasNext());

        return nPieces;

    }

    public void setWinningPlayerColor(String color){
        winningPlayer = color;
    }

    public String getWinningPlayer() {
        return winningPlayer;
    }

    public CheckerGame(ArrayList<CheckerPiece> piecesLoad){
        piecesBox = piecesLoad;
        currentPlayer = "White";
        winningPlayer = "White";

    }
}



