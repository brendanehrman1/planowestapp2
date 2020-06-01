package com.example.planowestapp2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private static final int DPAD_SIZE = 200;

    private Player player;
    private GameThread thread;
    private Rect upDisplay;
    private Rect downDisplay;
    private Rect leftDisplay;
    private Rect rightDisplay;
    private Rect upBtn;
    private Rect downBtn;
    private Rect leftBtn;
    private Rect rightBtn;
    private boolean actionDownFlag;
    private MotionEvent event;

    public Game(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new GameThread(this, getHolder());
        
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            this.event = event;
            this.actionDownFlag = true;
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            this.event = event;
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            this.event = null;
            this.actionDownFlag = false;
            return false;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void update() {
        player.update();
        if (actionDownFlag) {
            if (upBtn.contains((int) event.getX(), (int) event.getY()))
                player.moveUp();
            if (downBtn.contains((int) event.getX(), (int) event.getY()))
                player.moveDown();
            if (leftBtn.contains((int) event.getX(), (int) event.getY()))
                player.moveLeft();
            if (rightBtn.contains((int) event.getX(), (int) event.getY()))
                player.moveRight();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);
        if (upBtn == null) {
            instantiateValues(canvas);
        }
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        canvas.drawRect(upDisplay, paint);
        canvas.drawRect(downDisplay, paint);
        canvas.drawRect(leftDisplay, paint);
        canvas.drawRect(rightDisplay, paint);

        player.draw(canvas);
    }

    private void instantiateValues(Canvas c) {
        int leftSide = 25;
        int rightSide = leftSide + DPAD_SIZE;
        int downSide = c.getHeight() - 25;
        int upSide = downSide - DPAD_SIZE;
        upDisplay = new Rect(leftSide + (DPAD_SIZE / 3), upSide, rightSide - (DPAD_SIZE / 3), upSide + (DPAD_SIZE / 3));
        downDisplay = new Rect(leftSide + (DPAD_SIZE / 3), downSide - (DPAD_SIZE / 3), 150, 700);
        leftDisplay = new Rect(leftSide, 575, 100, downSide - (DPAD_SIZE / 3));
        rightDisplay = new Rect(rightSide - (DPAD_SIZE / 3), upSide + (DPAD_SIZE / 3), rightSide, 625);
        upBtn = new Rect(leftSide, 500, rightSide, upSide + (DPAD_SIZE / 3));
        downBtn = new Rect(leftSide, downSide - (DPAD_SIZE / 3), rightSide, 700);
        leftBtn = new Rect(leftSide, 500, 100, 700);
        rightBtn = new Rect(rightSide - (DPAD_SIZE / 3), 500, rightSide, 700);
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(thread.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(35);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(thread.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(35);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }
}
