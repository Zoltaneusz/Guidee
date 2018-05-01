package zollie.travelblogger.guidee.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import zollie.travelblogger.guidee.models.JourneyModel;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by FuszeneckerZ on 2016.12.30..
 */

public class ImageProcessor {
    public Context mContext;

    public ImageProcessor(Context activityContext) {
        this.mContext = activityContext;
    }

    public Bitmap pulseMarker(int step, Bitmap bitm, Canvas canv, float scale, Bitmap circleBitmap, boolean fixRes){

        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

        // Resize to 100x100 dp
        if(fixRes)
        bitm = scaleDown(bitm, 100, true);

        // Check for square images
        if(bitm.getHeight() > bitm.getWidth())  // portrait image
            bitm = Bitmap.createBitmap(bitm, 0, 0, bitm.getWidth(), bitm.getHeight()-(bitm.getHeight()-bitm.getWidth()));
        else if (bitm.getHeight() < bitm.getWidth())  // landstace image
            bitm = Bitmap.createBitmap(bitm, 0, 0, bitm.getWidth()-(bitm.getWidth()-bitm.getHeight()), bitm.getHeight());

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
    public Bitmap resizeMarkerImage(Bitmap myPic, float size){

        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, myPic.getWidth(), myPic.getHeight()), new RectF(0, 0, 50*size, 50*size), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(myPic, 0, 0, myPic.getWidth(), myPic.getHeight(), m, true);

    }

    public Bitmap loadImgToBitmap(JourneyModel mJourney){
        Bitmap sharedImage = null;
        try {
            sharedImage= Glide.with(this.mContext)
                    .load(mJourney.coverImageUrl)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return sharedImage;
    }

    public void shareImage(JourneyModel mJourney){

        new AsyncImageToBitmap().execute(mJourney);


    }



    public class AsyncImageToBitmap extends AsyncTask<Object, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Object... params) {
            JourneyModel mJourney = (JourneyModel) params[0];
            Bitmap mBitmap = null;
            mBitmap = loadImgToBitmap(mJourney);
            return mBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File sharedImageFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "shared_image1.jpg");
            FileOutputStream fileOut = null;
            try {
                sharedImageFile.createNewFile();
                fileOut = new FileOutputStream(sharedImageFile);
                fileOut.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();

            }
            finally{
                if(fileOut != null) {
                    try {
                        fileOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///storage/emulated/0/shared_image1.jpg"));
            mContext.startActivity(Intent.createChooser(shareIntent, "Share Journey"));
        }
    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.max(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
