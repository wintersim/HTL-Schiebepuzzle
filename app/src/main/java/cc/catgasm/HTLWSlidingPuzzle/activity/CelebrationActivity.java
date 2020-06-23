package cc.catgasm.HTLWSlidingPuzzle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cc.catgasm.HTLWSlidingPuzzle.R;

public class CelebrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebration);

        Intent intent = getIntent();
        final int gridSize = intent.getIntExtra(MainActivity.GAME_SIZE_MESSAGE, 3);
        final long timeStamp = intent.getLongExtra(GameActivity.TIMESTAMP, 0);
        final int taps = intent.getIntExtra(GameActivity.TAPS, 0);

        final TextView sizeText = findViewById(R.id.sizeText);
        final TextView timeText = findViewById(R.id.timeText);
        final TextView tapsText = findViewById(R.id.tapsText);

        String sizeDisplay = formatSize(gridSize);
        sizeText.setText(sizeDisplay);

        String timeDisplay = formatTime(timeStamp);
        timeText.setText(timeDisplay);

        String tapsDisplay = formatTaps(taps);
        tapsText.setText(tapsDisplay);
    }

    public void returnToMenu(View view) {
        finish();
    }

    private String formatSize(int gridSize) {
        String tmp = "";

        tmp = "[" + gridSize + "x" + gridSize + "]";

        return tmp;
    }

    private String formatTime(long time) {
        String tmp = "";

        long minutes = (time / 1000) / 60;
        long seconds = (time / 1000) % 60;
        tmp = "[" + minutes + ":" + seconds + "]";

        return tmp;
    }

    private String formatTaps(int taps) {
        String tmp = "";

        tmp = "[" + taps + "]";

        return tmp;
    }
}
