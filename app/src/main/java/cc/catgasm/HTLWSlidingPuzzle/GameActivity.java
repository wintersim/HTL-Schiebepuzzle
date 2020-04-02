package cc.catgasm.HTLWSlidingPuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView tv = findViewById(R.id.textView2);
        Intent intent = getIntent();
        tv.setText(String.format("%s%s", tv.getText(), intent.getStringExtra(MainActivity.GAME_SIZE_MESSAGE)));
    }
}
