package zollie.travelblogger.guidee.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.adapters.LikeListAdapter;

/**
 * Created by FuszeneckerZ on 2017. 06. 03..
 */

public class LikeListDialogFragment extends DialogFragment {
    ArrayList<HashMap<String, String>> allLikers = new ArrayList<HashMap<String, String>>(30);
    RecyclerView.ItemDecoration itemDecoration = null;

    public static LikeListDialogFragment newInstance(ArrayList<HashMap<String, String>> likeList)
    {
        LikeListDialogFragment frag = new LikeListDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("like_list", likeList);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allLikers = (ArrayList<HashMap<String, String>>) getArguments().getSerializable("like_list");
        setStyle(STYLE_NORMAL, R.style.AppTheme);
        itemDecoration = new
                DividerItemDecoration(getActivity(), zollie.travelblogger.guidee.utils.DividerItemDecoration.VERTICAL_LIST);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialogfragment_like_list, container, false);

        RecyclerView rvLikes = (RecyclerView) v.findViewById(R.id.like_list_recycler);
        LikeListAdapter adapter = new LikeListAdapter(getActivity(), allLikers);
        rvLikes.setAdapter(adapter);
        rvLikes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        rvLikes.addItemDecoration(itemDecoration);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

}
