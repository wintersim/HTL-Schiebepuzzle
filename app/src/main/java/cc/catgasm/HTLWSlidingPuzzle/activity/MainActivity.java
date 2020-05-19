package cc.catgasm.HTLWSlidingPuzzle.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import cc.catgasm.HTLWSlidingPuzzle.R;
import cc.catgasm.HTLWSlidingPuzzle.parcelable.ImageParcelable;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String GAME_SIZE_MESSAGE = "cc.catgasm.HTLWSlidingPuzzle.GAME_SIZE_MESSAGE";
    public static final String IMAGE_MESSAGE = "cc.catgasm.HTLWSlidingPuzzle.IMAGE_MESSAGE";
    private static final int PICK_IMAGE_FILE = 9873;
    private int gameSize;
    private Uri customImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = findViewById(R.id.size_spinner);
        spinner.setOnItemSelectedListener(this);
        gameSize = 3;
    }

    public void startGame(View view) {
        //Gewünschte Größe und Bild an GameActivity schicken
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GAME_SIZE_MESSAGE, gameSize);
        if(customImageUri != null) {
            intent.putExtra(IMAGE_MESSAGE, new ImageParcelable(customImageUri));
        } else {
            intent.putExtra(IMAGE_MESSAGE, new ImageParcelable(R.drawable.wholepicture));
        }
        startActivity(intent);
    }

    public void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");


        startActivityForResult(intent, PICK_IMAGE_FILE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_FILE
                && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (data != null) {
                uri = data.getData();
                if(uri != null) {
                    customImageUri = uri;
                    //Berechtigung speichern
                    final int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(uri, takeFlags);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gameSize = i + 3;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
