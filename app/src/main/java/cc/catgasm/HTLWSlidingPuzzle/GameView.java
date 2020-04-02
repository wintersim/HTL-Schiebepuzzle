package cc.catgasm.HTLWSlidingPuzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private Canvas canvas;
    private Paint paint;
    private SlidingPuzzle slidingPuzzle;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        paint = new Paint();
        paint.setColor(Color.RED);

        slidingPuzzle = new SlidingPuzzle(3);

        setMinimumHeight(700);
        setMinimumWidth(400);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void drawRect(SurfaceHolder surfaceHolder){
        canvas = surfaceHolder.lockCanvas();

        canvas.drawColor(Color.GREEN); //Hintergrundfarbe

        List<Tile> tiles = slidingPuzzle.getCards();

        int[] cl = new int[] {
                Color.GRAY,
                Color.RED,
                Color.rgb(0.5f,0f,0.5f),
                Color.rgb(0.7f,0.1f, 0.1f),
                Color.BLACK,
                Color.YELLOW,
                Color.CYAN,
                Color.DKGRAY,
                Color.WHITE
        };

        int i = 0;
        for (Tile tile : tiles) {
            paint.setColor(cl[tile.getNumber()]);
            canvas.drawRect(tile.getRect(),paint);
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) //Loslassen Ignorieren
            return true;
        int x = (int) event.getX();
        int y = (int) event.getY();

        Tile tile;
        if((tile = slidingPuzzle.getTouched(x,y)) != null) {
            Toast t = Toast.makeText(getContext(), tile.toString(), Toast.LENGTH_SHORT);
            t.show();
        }

        slidingPuzzle.switchCards(0,1);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("TEST", "Draw");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawRect(surfaceHolder);
        Log.e("TEST", "Create");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
