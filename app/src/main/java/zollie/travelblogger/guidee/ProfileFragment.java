package zollie.travelblogger.guidee;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.api.services.youtube.YouTube;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by FuszeneckerZ on 2016.11.27..
 */

public class ProfileFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        return rootView;
    }

    @Override
    public void onResume() {

        // Change statusbar color ===============================
        if (Build.VERSION.SDK_INT >= 21) {

            Window window = getActivity().getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.lightGreen));
        }
        //========================================================

        LinearLayout horitontalLayout = (LinearLayout) getView().findViewById(R.id.scroll_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < 15; i++){

            //Circle bitmap into profile picture test ============================================
            ImageView profilePicImg = (ImageView) getActivity().findViewById(R.id.prof_pic);
            Bitmap circleBitmap = null;
            int step = 0;
            Canvas canv = new Canvas();
            float scale = 0;
            Paint color = new Paint();

            color.setTextSize(35);
            color.setColor(Color.BLACK);

            Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.profile_pic);
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

            c.drawCircle((int)(bitmap.getWidth()/2.5+10), (int)(bitmap.getHeight()/2.5+10),(int) (bitmap.getWidth()/2.5)-3+step*1, circleFrame);
            circleFrame.setColor(Color.GRAY);
            circleFrame.setStyle(Paint.Style.STROKE);
            c.drawCircle((int)(bitmap.getWidth()/2.5+10), (int)(bitmap.getHeight()/2.5+10),(int) (bitmap.getWidth()/2.5)-7+step*1, circleFrame);
            c.drawCircle((int)(bitmap.getWidth()/2.5+10), (int)(bitmap.getHeight()/2.5+10),(int) (bitmap.getWidth()/2.5)-3+step*1, circleFrame);
            c.drawCircle((int)(bitmap.getWidth()/2.5+10), (int)(bitmap.getHeight()/2.5+10), (int)(bitmap.getWidth()/2.5)-8+step*1, profilePic);


            profilePicImg.setImageBitmap(circleBitmap);
            //=======================================================================
            //final ImageView imageView = new ImageView (getActivity());
            final ImageView imageView = new ImageView(getActivity());
            //imageView.setTag(i);
            //===================== Adding Image to to Horizontal Slide via Glide =========
            Glide
                    .with(getActivity())
                    .load("http://goo.gl/gEgYUd")
                    .centerCrop()
                    .placeholder(R.drawable.profile_pic)
                    .crossFade()
                    .into(imageView);
            //=============================================================================
    //        imageView.setImageResource(R.drawable.profile_pic);
            imageView.setBackgroundResource(R.drawable.pic_background);

    //        imageView.setScaleX(2);
    //        imageView.setScaleY(2);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            horitontalLayout.addView(imageView, params);

            imageView.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    //      Log.e("Tag",""+imageView.getTag());
                }
            });
        }
//       horitontalLayout.setLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT);
        super.onResume();
    }
}

