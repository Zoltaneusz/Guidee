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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;
import zollie.travelblogger.guidee.adapters.LikeListAdapter;
import zollie.travelblogger.guidee.models.UserModel;

/**
 * Created by FuszeneckerZ on 2017. 06. 03..
 */

public class LikeListDialogFragment extends DialogFragment {
    Map<String, Object> allLikersRaw = null;
    ArrayList<UserModel> allLikers = new ArrayList<UserModel>(30);
    RecyclerView.ItemDecoration itemDecoration = null;

    public static LikeListDialogFragment newInstance(Map<String, Object> likeList)
    {
        LikeListDialogFragment frag = new LikeListDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("like_list", (Serializable) likeList);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allLikersRaw = (Map<String, Object>) getArguments().getSerializable("like_list");
        setStyle(STYLE_NORMAL, R.style.AppTheme);
        itemDecoration = new
                DividerItemDecoration(getActivity(), zollie.travelblogger.guidee.utils.DividerItemDecoration.VERTICAL_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialogfragment_like_list, container, false);

        final RecyclerView rvLikes = (RecyclerView) v.findViewById(R.id.like_list_recycler);

        for (final Map.Entry<String, Object> rawLoverID : allLikersRaw.entrySet()) {
            if ((boolean) rawLoverID.getValue()) {    // only get "true" lovers
                DataHandler.getInstance().getUserWithId((String) rawLoverID.getKey(), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyID) {

                    }

                    @Override
                    public void onUserData(final Map<String, Object> rawUserData, String userID) {

                        UserModel userModel = new UserModel(rawUserData, userID);
                        allLikers.add(userModel);
                        LikeListAdapter adapter = new LikeListAdapter(getActivity(), allLikers);
                        rvLikes.setAdapter(adapter);
                        rvLikes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    }

                    @Override
                    public void onCommentData(Map<String, Object> rawCommentData, String commentID, String journeyIdent) {

                    }
                });
            }
        }



        rvLikes.addItemDecoration(itemDecoration);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

}
