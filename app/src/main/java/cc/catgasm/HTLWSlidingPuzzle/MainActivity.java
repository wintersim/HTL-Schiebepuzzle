package cc.catgasm.HTLWSlidingPuzzle;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


public class MainActivity extends AppCompatActivity {

    public static final String GAME_SIZE_MESSAGE = "cc.catgasm.HTLWSlidingPuzzle.GAME_SIZE_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startGame(View view) {
        /*Gewünschte Größe an GameActivity schicken*/

        Intent intent = new Intent(this, GameActivity.class);
        int sz = 3; //TODO Button oder so in der View
        intent.putExtra(GAME_SIZE_MESSAGE, sz);
        startActivity(intent);
    }
}
