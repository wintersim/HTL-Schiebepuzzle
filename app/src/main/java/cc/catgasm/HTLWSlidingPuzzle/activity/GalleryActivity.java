package cc.catgasm.HTLWSlidingPuzzle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cc.catgasm.HTLWSlidingPuzzle.R;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        LinearLayout ll = findViewById(R.id.gallery_linear_layout);

        int[] resIds = new int[] {
                R.drawable.htl_haupteingang,
                R.drawable.htl_logo,
                R.drawable.htl_seite,
                R.drawable.htl_tdot,
                R.drawable.htl_wels
        };

        for (int resId : resIds) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(resId);
            iv.setTag(resId);
            iv.setOnClickListener(this);
            ll.addView(iv);
        }
    }

    @Override
    public void onClick(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(MainActivity.GALLERY_RETURN_MESSAGE,(int)view.getTag());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
