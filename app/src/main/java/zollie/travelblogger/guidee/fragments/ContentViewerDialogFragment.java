package zollie.travelblogger.guidee.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import zollie.travelblogger.guidee.R;

/**
 * Created by FuszeneckerZ on 2017.01.16..
 */

public class ContentViewerDialogFragment extends DialogFragment {
    String mImageString;

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
        ImageView imageView = (ImageView) v.findViewById(R.id.contentImageView);
        //===================== Adding Image to to Horizontal Slide via Glide =========
        Glide
                .with(this)
                .load(mImageString)
                .centerCrop()
                .crossFade()
                .into(imageView);
        //=============================================================================

        return v;
    }
}