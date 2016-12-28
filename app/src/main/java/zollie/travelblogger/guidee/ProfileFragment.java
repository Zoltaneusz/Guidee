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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.api.services.youtube.YouTube;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.11.27..
 */

public class ProfileFragment extends Fragment {
    ArrayList<JourneyModel> allJourneys = new ArrayList<JourneyModel>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        //================= Getting data of 1 profile =====================================

        DataHandler.getInstance().getUserWithId(new String("0"), new DataHandlerListener() {
            @Override
            public void onJourneyData(final Map<String, Object> rawJourneyData) {
                //addMapMarker(journeyModel, mMap);
            }
            @Override
            public void onUserData(Map<String, Object> rawUserData) {
                allJourneys.clear();
                UserModel userModel = new UserModel(rawUserData);
                //================= Getting journeys of profile =====================================
                for (String userJourneyString : getUserJourneys(userModel)) {
                    DataHandler.getInstance().getJourneyWithId(userJourneyString, new DataHandlerListener() {
                        @Override
                        public void onJourneyData(Map<String, Object> rawJourneyData) {
                            JourneyModel journeyModel = new JourneyModel(rawJourneyData);
                            allJourneys.add(journeyModel);
                            fillHorizontalScrollBar();
                            fillRecyclerView();
                        }

                        @Override
                        public void onUserData(Map<String, Object> rawUserData) {

                        }
                    });
                }
            }
        });



    }

    public ArrayList<String> getUserJourneys(UserModel userModel) {
        ArrayList<String> allJourneys = new ArrayList<String>();
        for (Map.Entry<String, Object> map : userModel.userJourneys.entrySet()) {
            String journeyModel = (String) map.getValue();
            allJourneys.add(journeyModel);
        }
        return allJourneys;
    }

    public void fillRecyclerView(){

        RecyclerView rvJourneys = (RecyclerView) getActivity().findViewById(R.id.recycler_view);

        JourneyAdapter adapter = new JourneyAdapter(getActivity(), allJourneys);
        rvJourneys.setAdapter(adapter);
        rvJourneys.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), zollie.travelblogger.guidee.DividerItemDecoration.HORIZONTAL_LIST);
        rvJourneys.addItemDecoration(itemDecoration);
    }

    public void fillHorizontalScrollBar() {
        //========================================================

        LinearLayout horitontalLayout = (LinearLayout) getView().findViewById(R.id.journey_scroll_layout);
        if (horitontalLayout.getChildAt(0) == null) {
            LinearLayout.LayoutParams params = new LinearLayout.
                    LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            //======================== Setting Journey Images ==================================
            for (JourneyModel mJourney : allJourneys) {

                final ImageView imageView = new ImageView(getActivity());
                if (mJourney == null) break;
                String coverImageUrl = mJourney.coverImageUrl;
                //===================== Adding Image to to Horizontal Slide via Glide =========
                Glide
                        .with(getActivity())
                        .load(coverImageUrl)
                        .centerCrop()
                        .override(160, 160)
                        .crossFade()
                        .into(imageView);
                //=============================================================================
                //        imageView.setImageResource(R.drawable.profile_pic);
                  imageView.setBackgroundResource(R.drawable.pic_background);

                //        imageView.setScaleX(2);
                //        imageView.setScaleY(2);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                horitontalLayout.addView(imageView, params);
                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //      Log.e("Tag",""+imageView.getTag());
                    }
                });

            }
        }
    }
}