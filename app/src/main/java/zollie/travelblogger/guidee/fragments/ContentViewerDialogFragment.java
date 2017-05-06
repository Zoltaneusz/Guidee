package zollie.travelblogger.guidee.fragments;

import android.app.DialogFragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.ArrayList;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.adapters.CarouselAdapter;
import zollie.travelblogger.guidee.adapters.DialogFragmentAdapter;
import zollie.travelblogger.guidee.models.CarouselModel;

/**
 * Created by FuszeneckerZ on 2017.01.16..
 */

public class ContentViewerDialogFragment extends DialogFragment {
    ArrayList<CarouselModel> allCarousels = new ArrayList<CarouselModel>();
    PhotoViewAttacher mAttacher;

    public static ContentViewerDialogFragment newInstance(ArrayList<CarouselModel> carousels){
        ContentViewerDialogFragment frag = new ContentViewerDialogFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("carousels", carousels);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allCarousels = getArguments().getParcelableArrayList("carousels");
        setStyle(STYLE_NORMAL, R.style.AppTheme);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
        View v = inflater.inflate(R.layout.dialogfragment_content_view, container, false);

        RecyclerView rvCarousels = (RecyclerView) v.findViewById(R.id.dialog_reycler);
        DialogFragmentAdapter adapter = new DialogFragmentAdapter(getActivity(), allCarousels);
        rvCarousels.setAdapter(adapter);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(rvCarousels);
        rvCarousels.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), zollie.travelblogger.guidee.utils.DividerItemDecoration.HORIZONTAL_LIST);
        rvCarousels.addItemDecoration(itemDecoration);

        return v;
    }


}
