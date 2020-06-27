package cc.catgasm.HTLWSlidingPuzzle.image;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.GridView;

import java.util.LinkedList;
import java.util.List;

//TODO Thread
public class SlicedImage {
    private Bitmap originalImage;
    private int emptyColor;

    public SlicedImage(Bitmap originalImage, int emptyColor) {
        this.originalImage = originalImage;
        this.emptyColor = emptyColor;
    }

    /*
    * w--> Seitenlänge (3 --> 3x3)
    * */
    public List<Bitmap> slice(int w) {
        List<Bitmap> slicedImages = new LinkedList<>();
        int slicedWidth = originalImage.getWidth() / w;
        int slicedHeight = originalImage.getHeight() / w;

        int[] pixels = new int[slicedHeight * slicedWidth];

        int currentSlicedWidth = 0;
        int currentSlicedHeight = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                if(i == w - 1 && j == w - 1) {
                    Bitmap tmp = Bitmap.createBitmap(slicedWidth,slicedHeight, originalImage.getConfig());
                    tmp.eraseColor(emptyColor);
                    slicedImages.add(tmp);
                    continue;
                }
                originalImage.getPixels(pixels,0,slicedWidth,currentSlicedWidth,currentSlicedHeight,slicedWidth,slicedHeight);
                Bitmap tmp = Bitmap.createBitmap(pixels,slicedWidth,slicedHeight, originalImage.getConfig());
                slicedImages.add(tmp);
                currentSlicedWidth += slicedWidth;
            }
            currentSlicedHeight += slicedHeight;
            currentSlicedWidth = 0;
        }
        return slicedImages;
    }


}
