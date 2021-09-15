package com.mobdeve.s18.cuevas.alfonso.legitcheckers.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class CheckerGame {
    private static ArrayList piecesBox;
    private static String currentPlayer;
    private static String winningPlayer;

    public final void clear() {
        piecesBox.clear();
    }

    public static ArrayList getPiecesBox() {
        return piecesBox;
    }
    public static void setPiecesBox(ArrayList piecesBox) {
        CheckerGame.piecesBox = piecesBox;
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
            if (pieceAt(from.getCol(), from.getRow()).getPlayer() == "Black") {
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
        if(currentPlayer == "White" && to.getRow() == 0)
            return true;
        if(currentPlayer == "Black" && to.getRow() == 7)
            return true;
        return false;
    }

    public int canEatRight(Square from){

        int val;
        CheckerPiece pieceAtRight;
        if(currentPlayer == "Black")
            val = 1;
        else
            val = -1;

        pieceAtRight = pieceAt(from.getCol() + 1, from.getRow() + val);
        if(pieceAtRight!=null){
            if(pieceAtRight.getPlayer()!=currentPlayer)
                Log.i("TAG", "Enemy spotted: "+ pieceAtRight.getPlayer() + pieceAtRight.getCol() + pieceAtRight.getRow() + "\nBy: "+ currentPlayer + from.getCol() + from.getRow());
        }


        if(pieceAtRight!=null && !isOnEdge(pieceAtRight)){
            if(pieceAtRight.getPlayer()!=currentPlayer){
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
                    if (pieceAtRight.getPlayer() != currentPlayer) {
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

        int val;
        CheckerPiece pieceAtLeft;
        if(currentPlayer == "Black")
            val = 1;
        else
            val = -1;

        pieceAtLeft = pieceAt(from.getCol() - 1, from.getRow() + val);

        if(pieceAtLeft!=null && !isOnEdge(pieceAtLeft)){
            if(pieceAtLeft.getPlayer() != currentPlayer){
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
                    if (pieceAtLeft.getPlayer() != currentPlayer) {
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
            if(currentPlayer== piece.getPlayer()) {
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
        int val;
        boolean isCurrKing =  pieceAt(from.getCol(), from.getRow()).isKing();
        if(currentPlayer == "Black")
            val = 1;
        else
            val = -1;
        Log.i("TAG", "movePiece:" + from.getCol() + from.getRow());
        Log.i("TAG", "Before Move: " + currentPlayer);
        if(currentPlayer == pieceAt(from.getCol(), from.getRow()).getPlayer()) {
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
                    if (currentPlayer == "Black")
                        currentPlayer = "White";
                    else
                        currentPlayer = "Black";
                }
            }
            else if (pieceEater != null && pieceEater == pieceAt(from.getCol(), from.getRow())){
                Log.i("TAG", "movePiece: CAN EAT SUCCESS");
                if(canEatLeft(from)!=99){
                    if(canEatLeft(from)==2)
                        val *= -1;
                    if((from.getCol() - 2 == to.getCol()) && (from.getRow() + val + val) == to.getRow()) {
                        piecesBox.remove(pieceAt(from.getCol(), from.getRow()));
                        piecesBox.remove(pieceAt(from.getCol() - 1, from.getRow() + val));
                        if(isCurrKing)
                            addPiece(new CheckerPiece(to.getCol(), to.getRow(), currentPlayer, true));
                        else
                            addPiece(new CheckerPiece(to.getCol(), to.getRow(), currentPlayer, turnKing(to)));
                    }
                }
                if(canEatRight(from)!=99){
                    if(canEatRight(from)==2) {
                        Log.i("TAG", "movePiece: KINGEATS");
                        val *= -1;
                    }
                    if((from.getCol() + 2 == to.getCol()) && (from.getRow() + val + val) == to.getRow()) {
                        Log.i("TAG", "movePiece: KINGEATS SUCCESSFULLY");
                        piecesBox.remove(pieceAt(from.getCol(), from.getRow()));
                        piecesBox.remove(pieceAt(from.getCol() + 1, from.getRow() + val));
                        if(isCurrKing)
                            addPiece(new CheckerPiece(to.getCol(), to.getRow(), currentPlayer, true));
                        else
                            addPiece(new CheckerPiece(to.getCol(), to.getRow(), currentPlayer, turnKing(to)));
                    }
                }

                if (availEat() == null){
                    if (currentPlayer == "Black")
                        currentPlayer = "White";
                    else
                        currentPlayer = "Black";
                }
                else{
                    Boolean samePieceCanEat = (availEat().getCol() == to.getCol()) && (availEat().getRow() == to.getRow());
                    if(!samePieceCanEat){
                        if (currentPlayer == "Black")
                            currentPlayer = "White";
                        else
                            currentPlayer = "Black";
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
            if(piece.getPlayer() == player)
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

    public CheckerGame(){
        piecesBox = new ArrayList<CheckerPiece>();
        currentPlayer = "White";
        winningPlayer = "None";
        int row = 0;
        for (int i = 8; row < i; ++row) {
            int col = 0;
            for (int j = 8; col < j; ++col) {
                boolean positions = (col % 2 == 1 && row % 2 != 1) || (col % 2 == 0 && row % 2 == 1);
                if (row < 3) {
                    if (positions) {
                        this.addPiece(new CheckerPiece(col, row, "Black", false));
                    }
                }

                if (row > 4) {
                    if (positions)
                        this.addPiece(new CheckerPiece(col, row, "White", false));
                }
            }
        };
    }
}



