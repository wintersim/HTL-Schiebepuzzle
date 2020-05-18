package cc.catgasm.HTLWSlidingPuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cc.catgasm.HTLWSlidingPuzzle.image.SlicedImage;

public class GameActivity extends AppCompatActivity {

    private ArrayList<ImageCell> cells = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Intent intent = getIntent();
        int gridSize = intent.getIntExtra(MainActivity.GAME_SIZE_MESSAGE,3);

        createCells(gridSize);
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

    private void createCells(int sz) {
        SlicedImage si = new SlicedImage(getBitmapFromResources(R.drawable.wholepicture));

        List<Bitmap> bitmaps = si.slice(sz);

        for (int i = 0; i < (sz * sz) - 1; i++) {
            cells.add(new ImageCell(i, bitmaps.get(i)));
        }
        cells.add(new ImageCell((sz * sz) - 1, getBitmapFromResources(R.drawable.placeholder)));
        System.out.println("Cells added.");
    }
}
