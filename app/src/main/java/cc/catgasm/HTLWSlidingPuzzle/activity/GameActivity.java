package cc.catgasm.HTLWSlidingPuzzle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cc.catgasm.HTLWSlidingPuzzle.R;
import cc.catgasm.HTLWSlidingPuzzle.grid.ImageGridAdapter;
import cc.catgasm.HTLWSlidingPuzzle.grid.ImageCell;
import cc.catgasm.HTLWSlidingPuzzle.image.SlicedImage;
import cc.catgasm.HTLWSlidingPuzzle.parcelable.ImageParcelable;
import cc.catgasm.HTLWSlidingPuzzle.util.Util;

public class GameActivity extends AppCompatActivity {

    public static final String TIMESTAMP = "cc.catgasm.HTLWSlidingPuzzle.TIMESTAMP";

    private ArrayList<ImageCell> cells = new ArrayList<>();

    private boolean imgToggle;
    private ImageParcelable ip;
    private Bitmap helpImage;
    private int gridSize;

    private boolean bruh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Intent intent = getIntent();
        gridSize = intent.getIntExtra(MainActivity.GAME_SIZE_MESSAGE, 3);
        ImageParcelable parcelable = intent.getParcelableExtra(MainActivity.IMAGE_MESSAGE);


        imgToggle = false;

        final GridView gridView = findViewById(R.id.gridview);
        final ImageGridAdapter imageGridAdapter = new ImageGridAdapter(this, cells);
        gridView.setAdapter(imageGridAdapter);
        gridView.setNumColumns(gridSize);
        gridView.setVerticalSpacing(0);
        createCells(gridSize, parcelable);

        final long timeStamp = System.currentTimeMillis();

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
                                        bruh = false;
                                        startCelebration(System.currentTimeMillis() - timeStamp);
                                        System.out.println("won");
                                    }
                                    break;
                                }
                            }
                        }
                        System.out.println();
                        imageGridAdapter.setCells(cells);
                    }
                    gridView.invalidateViews();
                }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bruh) {
            finish();
        } else {
            System.out.println(bruh);
            bruh = true;
        }
    }

    private void createCells(int sz, ImageParcelable parcelable) {
        SlicedImage si;
        ip = parcelable;
        if (parcelable.getImageType() == ImageParcelable.OFFICIAL_IMAGE) {
            helpImage = Util.getBitmapFromResources(getResources(),R.drawable.htl_wels);
            si = new SlicedImage(helpImage);
        } else {
            try {
                helpImage = Util.getBitmapFromUri(getContentResolver(),parcelable.getCustomImage());
                si = new SlicedImage(helpImage);
            } catch (IOException e) {
                Toast t = Toast.makeText(this, R.string.custom_image_error_toast, Toast.LENGTH_SHORT);
                t.show();
                finish();
                return;
            }
        }

        List<Bitmap> bitmaps = si.slice(sz);

        for (int i = 0; i < bitmaps.size(); i++) {
            cells.add(new ImageCell(i, bitmaps.get(i)));
        }
        Collections.shuffle(cells);
        System.out.println("Cells added.");
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

    public int[] getCoordinates(int position, int gridSize) {
        int[] coordinates = new int[2];
        coordinates[0] = position % gridSize;
        coordinates[1] = (int) position / gridSize;

        return coordinates;
    }

    private void startCelebration(long time) {
        Intent intent = new Intent(this, CelebrationActivity.class);
        intent.putExtra(TIMESTAMP, time);
        intent.putExtra(MainActivity.GAME_SIZE_MESSAGE, gridSize);
        startActivity(intent);
        finish();
    }

    public void togglePicture(View view) {
        ImageView img = findViewById(R.id.imageView);
        if (!imgToggle) {
            img.setImageBitmap(helpImage);
            imgToggle = true;
        } else {
            img.setImageResource(R.drawable.placeholder);

            imgToggle = false;
        }
    }
}
