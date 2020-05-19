package cc.catgasm.HTLWSlidingPuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cc.catgasm.HTLWSlidingPuzzle.image.SlicedImage;
import cc.catgasm.HTLWSlidingPuzzle.parcelable.ImageParcelable;

public class GameActivity extends AppCompatActivity {

    private ArrayList<ImageCell> cells = new ArrayList<>();

    private boolean imgToggle;
    private ImageParcelable ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Intent intent = getIntent();
        final int gridSize = intent.getIntExtra(MainActivity.GAME_SIZE_MESSAGE, 3);
        ImageParcelable parcelable = intent.getParcelableExtra(MainActivity.IMAGE_MESSAGE);

        createCells(gridSize, parcelable);

        imgToggle = false;

        final GridView gridView = findViewById(R.id.gridview);
        final AdapterSub adapterSub = new AdapterSub(this, cells);
        gridView.setAdapter(adapterSub);
        gridView.setNumColumns(gridSize);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageCell imgC = (ImageCell) gridView.getItemAtPosition(position);

                if (!(imgC.getId() == ((gridSize * gridSize) - 1))) {
                    int[] c = getCoordinates(position, gridSize);
                    ImageCell[][] grid = turnListIntoGrid(gridSize);
                    int[] neighbors = getNeighbors(position, c, grid, gridSize);

                    System.out.println("position: " + position);
                    for (int i = 0; i < neighbors.length; i++) {
                        if (neighbors[i] != -1) {
                            System.out.print("pos i: " + neighbors[i] + "\t");
                            if (cells.get(neighbors[i]).getId() == ((gridSize * gridSize) - 1)) {
                                System.out.println();
                                System.out.println("=======================");
                                Collections.swap(cells, neighbors[i], position);
                                if (checkWin()) {
                                    System.out.println("won");
                                }
                                break;
                            }
                        }
                    }
                    System.out.println();
                    adapterSub.setCells(cells);
                }
                gridView.invalidateViews();
            }
        });
    }

    private boolean checkWin() {
        for (int i = 1; i < cells.size(); i++) {
            if (cells.get(i - 1).getId() > cells.get(i).getId()){
                return false;
            }
        }
        return true;
    }

    private ImageCell[][] turnListIntoGrid(int gridSize) {
        ImageCell[][] grid = new ImageCell[gridSize][gridSize];
        int run = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = cells.get(run);
                System.out.print(run  + "/" + cells.get(run).getId() + "\t");
                run++;
            }
            System.out.println();
        }
        return grid;
    }

    private int[] getNeighbors(int position, int[] c, ImageCell[][] grid, int gridSize) {
        int[] neighbors = new int[4];
        int tmp = position - gridSize;
        if (tmp >= 0) {                              //neighbor over tappedfield
            neighbors[0] = tmp;
        } else {
            neighbors[0] = -1;
        }
        //System.out.print("nTop: " + neighbors[0] + "\t");

        tmp = position + gridSize;
        if (tmp < (gridSize * gridSize)) {          //neighbor under tappedfield
            neighbors[1] = tmp;
        } else {
            neighbors[1] = -1;
        }
        //System.out.print("nUnder: " + neighbors[1] + "\t");

        tmp = position - 1;
        if (tmp >= 0 && !(getCoordinates(tmp, gridSize)[1] < c[1])) { //neighbor left from tappedfield
            neighbors[2] = tmp;
        } else {
            neighbors[2] = -1;
        }
        System.out.println("position c[1]: " + c[1] + "\tposition - 1 [1]: " + getCoordinates(tmp, gridSize)[1]);
        //System.out.print("nLeft: " + neighbors[2] + "\t");

        tmp = position + 1;
        if (tmp < (gridSize * gridSize) && !(getCoordinates(tmp, gridSize)[1] > c[1])) {//neighbor right from tappedfield
            neighbors[3] = tmp;
        } else {
            neighbors[3] = -1;
        }
        System.out.println("position c[1]: " + c[1] + "\tposition + 1 [1]: " + getCoordinates(tmp, gridSize)[1]);

        //System.out.println("nRight: " + neighbors[3]);

        return neighbors;
    }

    private Bitmap getBitmapFromResources(int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(getResources(), resId, options);
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
        ip = parcelable;
        if (parcelable.getImageType() == ImageParcelable.OFFICIAL_IMAGE) {
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
        Collections.shuffle(cells);
        System.out.println("Cells added.");
    }

    public int[] getCoordinates(int position, int gridSize) {
        int[] coordinates = new int[2];
        coordinates[0] = position % gridSize;
        coordinates[1] = (int) position / gridSize;

        return coordinates;
    }

    public void togglePicture(View view) {
        ImageView img = findViewById(R.id.imageView);
        if (!imgToggle) {
            if (ip.getImageType() == ImageParcelable.OFFICIAL_IMAGE) {
                img.setImageResource(R.drawable.wholepicture);
            } else {
                try {
                    img.setImageBitmap(getBitmapFromUri(ip.getCustomImage()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imgToggle = true;
        } else {
            img.setImageResource(R.drawable.placeholder);

            imgToggle = false;
        }
    }
}
