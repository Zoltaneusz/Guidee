package zollie.travelblogger.guidee.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * Created by FuszeneckerZ on 2016.12.30..
 */

public class ImageProcessor {
    public ImageProcessor() {
    }

    public Bitmap pulseMarker(int step, Bitmap bitm, Canvas canv, float scale, Bitmap circleBitmap){

        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

        Bitmap bitmap = bitm;
        circleBitmap = Bitmap.createBitmap(bitmap.getWidth()+5, bitmap.getHeight()+5, Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint profilePic = new Paint();
        profilePic.setFlags(Paint.ANTI_ALIAS_FLAG);
        profilePic.setShader(shader);
        profilePic.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        Paint circleFrame = new Paint();
        circleFrame.setFlags(Paint.ANTI_ALIAS_FLAG);
        circleFrame.setColor(Color.BLACK);

        c.drawCircle((int)(bitmap.getWidth()/2.5+4*scale), (int)(bitmap.getHeight()/2.5+4*scale),(int) (bitmap.getWidth()/2.5)-3+step*1, circleFrame);
        circleFrame.setColor(Color.GRAY);
        circleFrame.setStyle(Paint.Style.STROKE);
        c.drawCircle((int)(bitmap.getWidth()/2.5+4*scale), (int)(bitmap.getHeight()/2.5+4*scale),(int) (bitmap.getWidth()/2.5)-5+step*1, circleFrame);
        c.drawCircle((int)(bitmap.getWidth()/2.5+4*scale), (int)(bitmap.getHeight()/2.5+4*scale),(int) (bitmap.getWidth()/2.5)-3+step*1, circleFrame);
        c.drawCircle((int)(bitmap.getWidth()/2.5+4*scale), (int)(bitmap.getHeight()/2.5+4*scale), (int)(bitmap.getWidth()/2.5)-6+step*1, profilePic);

        return circleBitmap;
    }
    public Bitmap resizeMarkerImage(Bitmap myPic, int size){

        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, myPic.getWidth(), myPic.getHeight()), new RectF(0, 0, 50*size, 50*size), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(myPic, 0, 0, myPic.getWidth(), myPic.getHeight(), m, true);

    }
}
