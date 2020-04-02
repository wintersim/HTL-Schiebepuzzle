package cc.catgasm.HTLWSlidingPuzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private Canvas canvas;
    private Paint paint;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        paint = new Paint();
        paint.setColor(Color.RED);
    }

    public void drawRect(SurfaceHolder surfaceHolder){
        canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.GREEN);
        canvas.drawRect(500,500,800,800,paint);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawRect(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
