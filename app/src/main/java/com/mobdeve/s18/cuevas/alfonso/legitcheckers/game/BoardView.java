package com.mobdeve.s18.cuevas.alfonso.legitcheckers.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mobdeve.s18.cuevas.alfonso.legitcheckers.PiecePosition;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.R;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.game.CheckerPiece;

import java.util.LinkedHashMap;
import java.util.Map;

public class BoardView extends View {

    private Paint paint;
    private Rect rect;
    private final float scaleFactor = 1.0F;
    private float originX = 20.0F;
    private float originY = 200.0F;
    private float cellSide = 130.0F;
    private Bitmap movingPieceBitmap;
    private CheckerPiece movingPiece;
    private int fromCol;
    private int fromRow;
    private final Map bitmaps;
    private float movingPieceX;
    private float movingPieceY;
    private final int lightColor = Color.parseColor("#EEEEEE");
    private final int darkColor = Color.parseColor("#000000");
    float x = 200;
    float y = 200;
    private PiecePosition piecePosition;

    public void setPiecePosition(PiecePosition x) {
        this.piecePosition = x;
    }

    public PiecePosition getPiecePosition(){return this.piecePosition;}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int smaller = Math.min(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(smaller, smaller);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*super.onDraw(canvas);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight()/2, paint);*/
        float chessBoardSide = (float)Math.min(this.getWidth(), this.getHeight()) * this.scaleFactor;
        this.cellSide = chessBoardSide / 8.0F;
        this.originX = ((float)this.getWidth() - chessBoardSide) / 2.0F;
        this.originY = ((float)this.getHeight() - chessBoardSide) / 2.0F;
        drawChessboard(canvas);
        drawPieces(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                this.fromCol = (int)((event.getX() - this.originX) / this.cellSide);
                this.fromRow = (int)((event.getY() - this.originY) / this.cellSide);
                Log.i("TAG", "fromCol:" + this.fromCol + "fromRow" + this.fromRow);
                    CheckerPiece temp = this.piecePosition.pieceAt(new Square(this.fromCol, this.fromRow));
                    this.movingPiece = temp;
                    if(temp==null) {
                        Log.i("TAG", "piecePosition NULL");
                    }
                    else{
                        if (temp.getPlayer() == "Black")
                            this.movingPieceBitmap = (Bitmap) this.bitmaps.get(R.drawable.red_piece);
                        else
                            this.movingPieceBitmap = (Bitmap) this.bitmaps.get(R.drawable.cream_piece);
                    }
                    Log.i("TAG", "onTouchEvent: YOU MADE IT DOWN!");
                break;
            case MotionEvent.ACTION_UP:
                int col = (int)((event.getX() - this.originX) / this.cellSide);
                int row = (int)((event.getY() - this.originY) / this.cellSide);
                if(this.piecePosition.pieceAt(new Square(this.fromCol, this.fromRow))!=null)
                    this.piecePosition.movePiece(new Square(this.fromCol, this.fromRow), new Square(col, row));
                Log.i("TAG", "onTouchEvent: YOU MADE IT UP!");
                this.movingPiece = (CheckerPiece)null;
                this.movingPieceBitmap = (Bitmap)null;
                this.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                this.movingPieceX = event.getX();
                this.movingPieceY = event.getY();
                Log.i("TAG", "onTouchEvent: YOU MADE IT MOVE!");
                this.invalidate();
        }
        return true;
    }

    private final void drawChessboard(Canvas canvas) {
        int row = 0;

        for(int i = 8; row < i; ++row) {
            int col = 0;
            for(int j = 8; col < j; ++col) {
                this.drawSquareAt(canvas, col, row, (col + row) % 2 == 1);
            }
        }

    }

    private final void loadBitmaps() {
            Bitmap rPiece = BitmapFactory.decodeResource(getResources(), R.drawable.red_piece);
            Bitmap cPiece = BitmapFactory.decodeResource(getResources(), R.drawable.cream_piece);
            this.bitmaps.put(R.drawable.red_piece, rPiece);
            this.bitmaps.put(R.drawable.cream_piece, cPiece);
    }

    private final void drawPieces(Canvas canvas){

        int row = 0;
        for(int i = 8; row < i; ++row) {
            int col = 0;
            for(int j = 8; col < j; ++col) {
                CheckerPiece piece = this.piecePosition.pieceAt(new Square(col, row));
                if(piece!=null){
                    if(piece.getPlayer() == "Black")
                        drawPieceAt(canvas,col,row,(Bitmap)this.bitmaps.get(R.drawable.red_piece));
                    if(piece.getPlayer() == "White")
                        drawPieceAt(canvas,col,row,(Bitmap)this.bitmaps.get(R.drawable.cream_piece));
                }
            }
        }

        if (this.movingPieceBitmap != null) {
            canvas.drawBitmap(this.movingPieceBitmap, (Rect)null, new RectF(this.movingPieceX - this.cellSide / (float)2, this.movingPieceY - this.cellSide / (float)2, this.movingPieceX + this.cellSide / (float)2, this.movingPieceY + this.cellSide / (float)2), this.paint);
        }

    }

    private final void drawPieceAt(Canvas canvas, int col, int row, Bitmap pieceColor){
        canvas.drawBitmap(pieceColor, (Rect) null, new RectF(this.originX + (float) col * this.cellSide, this.originY + (float) row * this.cellSide, this.originX +
                (float) (col + 1) * this.cellSide, this.originY + (float) (row + 1) * this.cellSide), null);
    }


    private final void drawSquareAt(Canvas canvas, int col, int row, boolean isDark) {
        this.paint.setColor(isDark ? this.darkColor : this.lightColor);
        canvas.drawRect(this.originX + (float)col * this.cellSide, this.originY + (float)row * this.cellSide, this.originX +
                                    (float)(col + 1) * this.cellSide, this.originY + (float)(row + 1) * this.cellSide, this.paint);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context,  attrs);
        paint = new Paint();
        rect = new Rect();
        this.bitmaps = (Map)(new LinkedHashMap());
        boolean var3 = false;
        this.paint = new Paint();
        this.fromCol = -1;
        this.fromRow = -1;
        this.movingPieceX = -1.0F;
        this.movingPieceY = -1.0F;
        this.loadBitmaps();
    }

}
