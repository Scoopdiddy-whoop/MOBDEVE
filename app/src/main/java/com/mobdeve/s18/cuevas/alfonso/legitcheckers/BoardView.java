package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {

    private Paint paint;
    private Rect rect;
    private Bitmap rPiece;
    private Bitmap cPiece;
    private final float scaleFactor = 1.0F;
    private float originX = 20.0F;
    private float originY = 200.0F;
    private float cellSide = 130.0F;
    private final int lightColor = Color.parseColor("#EEEEEE");
    private final int darkColor = Color.parseColor("#000000");
    float x = 200;
    float y = 200;

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
                x = event.getX();
                y = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                invalidate();
                break;
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

    private final void drawPieces(Canvas canvas){

        int row = 0;
        for(int i = 8; row < i; ++row) {
            int col = 0;
            for(int j = 8; col < j; ++col) {
                boolean positions = (col % 2 == 1 && row % 2 != 1) || (col % 2 == 0 && row % 2 == 1);
                if(row < 3) {
                    if (positions)
                        canvas.drawBitmap(rPiece, (Rect) null, new RectF(this.originX + (float) col * this.cellSide, this.originY + (float) row * this.cellSide, this.originX +
                                (float) (col + 1) * this.cellSide, this.originY + (float) (row + 1) * this.cellSide), null);
                }

                if(row > 4) {
                    if (positions)
                        canvas.drawBitmap(cPiece, (Rect) null, new RectF(this.originX + (float) col * this.cellSide, this.originY + (float) row * this.cellSide, this.originX +
                                (float) (col + 1) * this.cellSide, this.originY + (float) (row + 1) * this.cellSide), null);
                }
            }
        }

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
        rPiece = BitmapFactory.decodeResource(getResources(), R.drawable.red_piece);
        cPiece = BitmapFactory.decodeResource(getResources(), R.drawable.cream_piece);
    }

}
