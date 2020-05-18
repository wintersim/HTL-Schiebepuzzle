package cc.catgasm.HTLWSlidingPuzzle.image;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

//TODO Thread
public class SlicedImage {
    private Bitmap originalImage;

    public SlicedImage(Bitmap originalImage) {
        this.originalImage = originalImage;
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
                originalImage.getPixels(pixels,0,slicedWidth,currentSlicedWidth,currentSlicedHeight,slicedWidth,slicedHeight);
                Bitmap tmp = Bitmap.createBitmap(pixels,slicedWidth,slicedHeight, originalImage.getConfig());
                Log.e("bruh", tmp.toString());
                slicedImages.add(tmp);
                currentSlicedWidth += slicedWidth;
            }
            currentSlicedHeight += slicedHeight;
            currentSlicedWidth = 0;
        }
        return slicedImages;
    }


}
