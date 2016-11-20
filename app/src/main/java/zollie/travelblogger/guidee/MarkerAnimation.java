package zollie.travelblogger.guidee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by FuszeneckerZ on 2016.11.19..
 */

public class MarkerAnimation extends SurfaceView implements Runnable {

    Thread thread = null;
    boolean CanDraw = false;

    SurfaceHolder surfaceHolder;

    public MarkerAnimation(Context context) {
        super(context);
        surfaceHolder = getHolder();

    }

    @Override
    public void run() {

        while(CanDraw) {
            // Drawing here

            if (!surfaceHolder.getSurface().isValid())
                continue;





        }


    }

    public void pause() {
        CanDraw = false;

        while (true){
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread = null;
    }

    public void resume(){
        CanDraw = true;
        thread = new Thread(this);
        thread.start();
    }

}
