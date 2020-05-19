package cc.catgasm.HTLWSlidingPuzzle.grid;

import android.graphics.Bitmap;

public class ImageCell {
    private Bitmap bitmap;
    private int id;

    public ImageCell(int id, Bitmap bitmap) {
        this.id = id;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
