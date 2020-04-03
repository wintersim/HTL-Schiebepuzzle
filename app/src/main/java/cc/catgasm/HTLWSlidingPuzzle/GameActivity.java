package cc.catgasm.HTLWSlidingPuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private ArrayList<ImageCell> cells = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Intent intent = getIntent();
        int gridSize = intent.getIntExtra(MainActivity.GAME_SIZE_MESSAGE,3);

        createCells();
        GridView gridView = findViewById(R.id.gridview);
        AdapterSub adapterSub = new AdapterSub(this, cells);
        gridView.setAdapter(adapterSub);
        gridView.setNumColumns(gridSize);
    }

    private void createCells() {
        String[] numbers = {"one", "two", "three", "four", "five", "six", "seven", "eight"};
        for (int i = 1; i < 9; i++) {
            cells.add(new ImageCell(("./drawable/" + numbers[i - 1] + ".png"), numbers[i - 1], i));
        }
        cells.add(new ImageCell("./drawable/placeholder.png", "placeholder", 8));
        System.out.println("Cells added.");
    }
}
