package cc.catgasm.HTLWSlidingPuzzle.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import cc.catgasm.HTLWSlidingPuzzle.R;
import cc.catgasm.HTLWSlidingPuzzle.parcelable.ImageParcelable;
import cc.catgasm.HTLWSlidingPuzzle.util.Util;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String GAME_SIZE_MESSAGE = "cc.catgasm.HTLWSlidingPuzzle.GAME_SIZE_MESSAGE";
    public static final String IMAGE_MESSAGE = "cc.catgasm.HTLWSlidingPuzzle.IMAGE_MESSAGE";
    public static final String GALLERY_RETURN_MESSAGE = "cc.catgasm.HTLWSlidingPuzzle.GALLERY_RETURN_MESSAGE";
    private static final int PICK_IMAGE_FILE = 9873;
    private static final int OPEN_GALLERY = 9873;
    private int gameSize;
    private Uri customImageUri;
    private int imgResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = findViewById(R.id.size_spinner);
        spinner.setOnItemSelectedListener(this);
        gameSize = 3;
        imgResId = -1;
        customImageUri = null;
    }

    public void startGame(View view) {
        //Gewünschte Größe und Bild an GameActivity schicken
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GAME_SIZE_MESSAGE, gameSize);
        if(customImageUri != null) {
            intent.putExtra(IMAGE_MESSAGE, new ImageParcelable(customImageUri));
        } else {
            intent.putExtra(IMAGE_MESSAGE, new ImageParcelable(imgResId > 0 ? imgResId : R.drawable.htl_wels));
        }
        startActivity(intent);
    }

    public void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");


        startActivityForResult(intent, PICK_IMAGE_FILE);
    }

    public void openGallery(View view) {
        startActivityForResult(new Intent(this, GalleryActivity.class), OPEN_GALLERY);
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
                    setPreviewImg(customImageUri);
                    //Berechtigung speichern
                    final int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(uri, takeFlags);
                }
            }
        }
        if(requestCode == OPEN_GALLERY
                && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                int resId = data.getIntExtra(GALLERY_RETURN_MESSAGE, -1);
                if(resId > 0) {
                    setPreviewImg(resId);
                    imgResId = resId;
                    customImageUri = null; //falls vorher ein eigenes bild gewählt wurde
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gameSize = i + 2;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setPreviewImg(Uri uri) {
        ImageView iv = findViewById(R.id.imgPreview);
        try {
            iv.setImageBitmap(Util.getBitmapFromUri(getContentResolver(), uri));
        } catch (IOException e) {
            Toast t = Toast.makeText(this, R.string.custom_image_error_toast, Toast.LENGTH_SHORT);
            t.show();
            e.printStackTrace();
        }
    }

    private void setPreviewImg(int resId) {
        ImageView iv = findViewById(R.id.imgPreview);
        iv.setImageResource(resId);
    }
}
