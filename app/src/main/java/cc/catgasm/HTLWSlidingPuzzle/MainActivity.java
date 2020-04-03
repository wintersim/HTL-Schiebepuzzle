package cc.catgasm.HTLWSlidingPuzzle;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    //TODO: Anfangen lol

    public static final String GAME_SIZE_MESSAGE = "cc.catgasm.HTLWSlidingPuzzle.GAME_SIZE_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


createCells();
        GridView gridView = (GridView) findViewById(R.id.gridview);
        AdapterSub adapterSub = new AdapterSub(this, cells);
        gridView.setAdapter(adapterSub);
        gridView.setNumColumns(3);
    }


private void createCells(){
        String[] numbers = {"one", "two", "three", "four", "five", "six", "seven", "eight"};
        for (int i = 1; i < 9; i++) {
            cells.add(new ImageCell(("./drawable/" + numbers[i-1] + ".png"), numbers[i-1], i));
        }
        cells.add(new ImageCell("./drawable/placeholder.png", "placeholder", 8));
        System.out.println("Cells added.");
    }


    public void startGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        String sz = "3x3"; //TODO Button oder so in der View
        intent.putExtra(GAME_SIZE_MESSAGE, sz);
        startActivity(intent);
    }
}
