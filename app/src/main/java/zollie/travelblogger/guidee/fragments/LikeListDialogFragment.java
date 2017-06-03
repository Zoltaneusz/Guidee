package zollie.travelblogger.guidee.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import zollie.travelblogger.guidee.R;

/**
 * Created by FuszeneckerZ on 2017. 06. 03..
 */

public class LikeListDialogFragment extends DialogFragment {
    ArrayList<String> allLikers = new ArrayList<String>();

    public static LikeListDialogFragment newInstance(ArrayList<String> likeList)
    {
        LikeListDialogFragment frag = new LikeListDialogFragment();

        Bundle args = new Bundle();
        args.putStringArrayList("like_list", likeList);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allLikers = getArguments().getStringArrayList("like_list");
        setStyle(STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
