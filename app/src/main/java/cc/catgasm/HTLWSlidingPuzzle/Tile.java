package cc.catgasm.HTLWSlidingPuzzle;

import android.graphics.Rect;

public class Tile {
    private int x;
    private int y;
    private int number;
    private Rect rect;

    public static final int MAX_SIZE = 150;
    public static final float GAP_MULTIPLIER = 1.3f;
    public static final int OFFSET_X = 300; //TODO Automatisch centern
    public static final int OFFSET_Y = 200;

    public Tile(int x, int y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
        rect = new Rect();

        calcRect();
    }

    private void calcRect(){
        int rx = (int) (x * MAX_SIZE * GAP_MULTIPLIER) + OFFSET_X;
        int ry = (int) (y * MAX_SIZE * GAP_MULTIPLIER) + OFFSET_Y;

        rect.set(rx,ry,rx + MAX_SIZE,ry + MAX_SIZE);
    }

    /*Gibt das zu zeichnenden Rechteck zurück*/
    public Rect getRect() {
        return rect;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        calcRect();
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        calcRect();
        this.y = y;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                ", number=" + number +
                '}';
    }
}
