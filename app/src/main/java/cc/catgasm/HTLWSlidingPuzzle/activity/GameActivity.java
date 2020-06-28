package cc.catgasm.HTLWSlidingPuzzle.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cc.catgasm.HTLWSlidingPuzzle.R;
import cc.catgasm.HTLWSlidingPuzzle.grid.ImageCell;
import cc.catgasm.HTLWSlidingPuzzle.grid.ImageGridAdapter;
import cc.catgasm.HTLWSlidingPuzzle.image.SlicedImage;
import cc.catgasm.HTLWSlidingPuzzle.parcelable.ImageParcelable;
import cc.catgasm.HTLWSlidingPuzzle.util.Util;

public class GameActivity extends AppCompatActivity {

    public static final String TIMESTAMP = "cc.catgasm.HTLWSlidingPuzzle.TIMESTAMP";
    public static final String TAPS = "cc.catgasm.HTLWSlidingPuzzle.TAPS";

    private ArrayList<ImageCell> cells = new ArrayList<>();

    private boolean imgToggle;
    private ImageParcelable ip;
    private Bitmap helpImage;
    private int gridSize;

    private int taps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        taps = 0;

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

        int[] neighbors = new int[4]; //Array nur einmal erstellen

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageCell imgC = (ImageCell) gridView.getItemAtPosition(position);
                    taps++;

                    if (!(imgC.getId() == ((gridSize * gridSize) - 1))) {
                        getNeighbors(position, gridSize, neighbors);

                        System.out.println("position: " + position);
                        for (int i = 0; i < neighbors.length; i++) {
                            if (neighbors[i] != -1) {
                                System.out.print("pos i: " + neighbors[i] + "\t");
                                if (cells.get(neighbors[i]).getId() == ((gridSize * gridSize) - 1)) {
                                    System.out.println();
                                    System.out.println("=======================");
                                    Collections.swap(cells, neighbors[i], position);
                                    if (checkWin()) {
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

    private void createCells(int sz, ImageParcelable parcelable) {
        SlicedImage si;
        int color = ContextCompat.getColor(getApplicationContext(), R.color.sliced_image_empty_color);
        ip = parcelable;
        if (parcelable.getImageType() == ImageParcelable.OFFICIAL_IMAGE) {
            helpImage = Util.getBitmapFromResources(getResources(),parcelable.getOfficialImageId());
            si = new SlicedImage(helpImage, color);
        } else {
            try {
                helpImage = Util.getBitmapFromUri(getContentResolver(),parcelable.getCustomImage());
                si = new SlicedImage(helpImage, color);
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
        shuffle(cells);
        System.out.println("Cells added.");
    }

    private void shuffle(List<?> list) {
        Random r = new Random();
        int sz = list.size();
        int[] neighbors = new int[4];

        do {
            int emptyPos = sz - 1;

            int cnt = r.nextInt(50) + 20 * gridSize * gridSize;

            //Die 'weiße Fläche' cnt mal mit einem zufälligen nachbar tauschen
            for (int i = 0; i < cnt; i++) {
                getNeighbors(emptyPos, gridSize, neighbors);
                int tmpSwap = -1;
                do {
                    tmpSwap = neighbors[r.nextInt(4)];
                } while (tmpSwap < 0);
                Collections.swap(list, emptyPos, tmpSwap);
                emptyPos = tmpSwap;
            }
        } while (checkWin()); //Wenn bereits richtig sortiert, nochmal shuffeln
    }

    private boolean checkWin() {
        for (int i = 1; i < cells.size(); i++) {
            if (cells.get(i - 1).getId() > cells.get(i).getId()){
                return false;
            }
        }
        return true;
    }

    //neighbors soll von aussen erstellt werden
    //Methode muss dann nicht immer ein neues Array erstellen (Speicher verschwendung)
    private void getNeighbors(int position, int gridSize, int[] neighbors) {
        int tmp = position - gridSize;
        int[] tmpCoords = new int[2];
        int[] c = new int[2];
        getCoordinates(position, gridSize, c);
        if (tmp >= 0) {                              //neighbor over tappedfield
            neighbors[0] = tmp;
        } else {
            neighbors[0] = -1;
        }

        tmp = position + gridSize;
        if (tmp < (gridSize * gridSize)) {          //neighbor under tappedfield
            neighbors[1] = tmp;
        } else {
            neighbors[1] = -1;
        }

        tmp = position - 1;
        if (tmp >= 0 && !(getCoordinates(tmp, gridSize, tmpCoords)[1] < c[1])) { //neighbor left from tappedfield
            neighbors[2] = tmp;
        } else {
            neighbors[2] = -1;
        }

        tmp = position + 1;
        if (tmp < (gridSize * gridSize) && !(getCoordinates(tmp, gridSize, tmpCoords)[1] > c[1])) {//neighbor right from tappedfield
            neighbors[3] = tmp;
        } else {
            neighbors[3] = -1;
        }
    }

    //coordinates von aussen erstellen
    //gleich wie bei getneighbors
    //Gibt sich selbst zurück für chaining
    public int[] getCoordinates(int position, int gridSize, int[] coordinates) {
        coordinates[0] = position % gridSize;
        coordinates[1] = (int) position / gridSize;

        return coordinates;
    }

    private void startCelebration(long time) {
        Intent intent = new Intent(this, CelebrationActivity.class);
        intent.putExtra(TIMESTAMP, time);
        intent.putExtra(TAPS, taps);
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
            img.setImageResource(R.drawable.help);

            imgToggle = false;
        }
    }
}
