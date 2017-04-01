package zollie.travelblogger.guidee.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import zollie.travelblogger.guidee.R;

/**
 * Created by FuszeneckerZ on 2017.01.16..
 */

public class ContentViewerDialogFragment extends DialogFragment {
    String mImageString;
    PhotoViewAttacher mAttacher;

    public static ContentViewerDialogFragment newInstance(String image){
        ContentViewerDialogFragment frag = new ContentViewerDialogFragment();

        Bundle args = new Bundle();
        args.putString("image", image);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageString = getArguments().getString("image");
        setStyle(STYLE_NORMAL, R.style.AppTheme);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View v = inflater.inflate(R.layout.dialogfragment_content_view, container, false);
        PhotoView imageView = (PhotoView) v.findViewById(R.id.contentImageView);
  //      Animation zoomAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_animation);

        //===================== Adding Image to to Horizontal Slide via Glide =========
        Glide
                .with(this)
                .load(mImageString)
                .fitCenter()
                .crossFade()
                .into(imageView);
        //=============================================================================
    //    imageView.startAnimation(zoomAnimation);


        return v;
    }
}
