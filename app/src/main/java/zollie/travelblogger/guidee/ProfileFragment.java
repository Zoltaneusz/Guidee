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
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
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
    ArrayList<JourneyModel> allFavorites = new ArrayList<JourneyModel>();
    ArrayList<JourneyModel> allPlans = new ArrayList<JourneyModel>();

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
        EditText messageInput = (EditText) getActivity().findViewById(R.id.my_plans_title);
        messageInput.getText().append("\ud83d\udcdd");
        messageInput = (EditText) getActivity().findViewById(R.id.my_favorites_title);
        messageInput.getText().append("\ud83d\udc9c");
        DataHandler.getInstance().getUserWithId(new String("0"), new DataHandlerListener() {
            @Override
            public void onJourneyData(final Map<String, Object> rawJourneyData) {
                //addMapMarker(journeyModel, mMap);
            }
            @Override
            public void onUserData(Map<String, Object> rawUserData) {
                allJourneys.clear();
                allFavorites.clear();
                allPlans.clear();
                UserModel userModel = new UserModel(rawUserData);
                //================= Getting journeys of profile =====================================

                    DataHandler.getInstance().getJourneyWithIds(getUserJourneys(userModel), new DataHandlerListener() {
                        @Override
                        public void onJourneyData(Map<String, Object> rawJourneyData) {
                            JourneyModel journeyModel = new JourneyModel(rawJourneyData);
                            allJourneys.add(journeyModel);
                            fillRecyclerView(R.id.my_journeys_recycle, allJourneys);
                        }

                        @Override
                        public void onUserData(Map<String, Object> rawUserData) {

                        }
                    });

                DataHandler.getInstance().getJourneyWithIds(getUserFavorites(userModel), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData) {
                        JourneyModel journeyModel = new JourneyModel(rawJourneyData);
                        allFavorites.add(journeyModel);
                        fillRecyclerView(R.id.following_journeys_recycle, allFavorites);
                    }

                    @Override
                    public void onUserData(Map<String, Object> rawUserData) {

                    }
                });

                DataHandler.getInstance().getJourneyWithIds(getUserPlans(userModel), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData) {
                        JourneyModel journeyModel = new JourneyModel(rawJourneyData);
                        allPlans.add(journeyModel);
                        fillRecyclerView(R.id.plan_journeys_recycle, allPlans);
                    }

                    @Override
                    public void onUserData(Map<String, Object> rawUserData) {

                    }
                });
            }
        });
        showPlaceholderCards();
    }

    public ArrayList<String> getUserJourneys(UserModel userModel) {
        ArrayList<String> allJourneys = new ArrayList<String>();
        for (Map.Entry<String, Object> map : userModel.userJourneys.entrySet()) {
            String journeyModel = (String) map.getValue();
            allJourneys.add(journeyModel);
        }
        return allJourneys;
    }

    public ArrayList<String> getUserFavorites(UserModel userModel) {
        ArrayList<String> allJourneys = new ArrayList<String>();
        for (Map.Entry<String, Object> map : userModel.loves.entrySet()) {
            String journeyModel = (String) map.getValue();
            allJourneys.add(journeyModel);
        }
        return allJourneys;
    }

    public ArrayList<String> getUserPlans(UserModel userModel) {
        ArrayList<String> allJourneys = new ArrayList<String>();
        for (Map.Entry<String, Object> map : userModel.plans.entrySet()) {
            String journeyModel = (String) map.getValue();
            allJourneys.add(journeyModel);
        }
        return allJourneys;
    }

    public void fillRecyclerView(int resource, ArrayList<JourneyModel> journeyModels){

        RecyclerView rvJourneys = (RecyclerView) getActivity().findViewById(resource);

        JourneyAdapter adapter = new JourneyAdapter(getActivity(), journeyModels);
        rvJourneys.setAdapter(adapter);
        rvJourneys.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), zollie.travelblogger.guidee.DividerItemDecoration.HORIZONTAL_LIST);
        rvJourneys.addItemDecoration(itemDecoration);
        rvJourneys.setVisibility(View.INVISIBLE);
    }

    public void showPlaceholderCards(){
        View viewToLoad = LayoutInflater.from(
                getActivity()).inflate(
                R.layout.card_placeholder, null);
        ((LinearLayout) getActivity().findViewById(R.id.my_journeys_recycle_placeholder)).addView(viewToLoad);
        LinearLayout itemForm=(LinearLayout) viewToLoad.findViewById(R.id.my_journeys_recycle_placeholder);
        itemForm.addView(itemForm);

    }
}