package cc.catgasm.HTLWSlidingPuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.GridView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.catgasm.HTLWSlidingPuzzle.image.SlicedImage;
import cc.catgasm.HTLWSlidingPuzzle.parcelable.ImageParcelable;

public class GameActivity extends AppCompatActivity {

    private ArrayList<ImageCell> cells = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Intent intent = getIntent();
        int gridSize = intent.getIntExtra(MainActivity.GAME_SIZE_MESSAGE,3);
        ImageParcelable parcelable = intent.getParcelableExtra(MainActivity.IMAGE_MESSAGE);

        createCells(gridSize, parcelable);

        GridView gridView = findViewById(R.id.gridview);
        AdapterSub adapterSub = new AdapterSub(this, cells);
        gridView.setAdapter(adapterSub);
        gridView.setNumColumns(gridSize);
    }

    private Bitmap getBitmapFromResources(int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(getResources(), resId,options);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void createCells(int sz, ImageParcelable parcelable) {
        SlicedImage si;
        if(parcelable.getImageType() == ImageParcelable.OFFICIAL_IMAGE) {
            si = new SlicedImage(getBitmapFromResources(R.drawable.wholepicture));
        } else {
            try {
                si = new SlicedImage(getBitmapFromUri(parcelable.getCustomImage()));
            } catch (IOException e) {
                Toast t = Toast.makeText(this, R.string.cutom_image_error_toast, Toast.LENGTH_SHORT);
                t.show();
                finish();
                return;
            }
        }

        List<Bitmap> bitmaps = si.slice(sz);

        for (int i = 0; i < (sz * sz) - 1; i++) {
            cells.add(new ImageCell(i, bitmaps.get(i)));
        }
        cells.add(new ImageCell((sz * sz) - 1, getBitmapFromResources(R.drawable.placeholder)));
        System.out.println("Cells added.");
    }
}
