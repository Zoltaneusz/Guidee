package zollie.travelblogger.guidee.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;
import zollie.travelblogger.guidee.utils.ImageProcessor;
import zollie.travelblogger.guidee.adapters.JourneyAdapter;
import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.models.JourneyModel;
import zollie.travelblogger.guidee.models.UserModel;

/**
 * Created by FuszeneckerZ on 2016.11.27..
 */

public class ProfileFragment extends Fragment {
    ArrayList<JourneyModel> allJourneys = new ArrayList<JourneyModel>();
    ArrayList<JourneyModel> allFavorites = new ArrayList<JourneyModel>();
    ArrayList<JourneyModel> allPlans = new ArrayList<JourneyModel>();
    Bitmap userAvatarGlobal = null;
    ImageProcessor imageProcessor = new ImageProcessor();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;


    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText messageInput = (EditText) getActivity().findViewById(R.id.my_plans_title);
        messageInput.getText().append("\ud83d\udcdd");
        messageInput = (EditText) getActivity().findViewById(R.id.my_favorites_title);
        messageInput.getText().append("\ud83d\udc9c");
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        }
        //================= Getting data of 1 profile =====================================

        DataHandler.getInstance().getUserWithId(new String("UQEch8jENJc4SjY6ZuMt7rULGho1"), new DataHandlerListener() {
            @Override
            public void onJourneyData(final Map<String, Object> rawJourneyData, String journeyReference) {
                //addMapMarker(journeyModel, mMap);
            }
            @Override
            public void onUserData(Map<String, Object> rawUserData) {
                allJourneys.clear();
                allFavorites.clear();
                allPlans.clear();
                UserModel userModel = new UserModel(rawUserData);
                new AsyncProfilePic().execute(userModel);

                //================= Getting journeys of profile =====================================

                DataHandler.getInstance().getJourneyWithIds(getUserJourneys(userModel), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {
                        JourneyModel journeyModel = new JourneyModel(rawJourneyData, journeyReference);
                        if(journeyModel.title != null) {
                            allJourneys.add(journeyModel);
                            fillRecyclerView(R.id.my_journeys_recycle, R.id.my_journeys_recycle_placeholder, allJourneys);
                        }
                    }

                    @Override
                    public void onUserData(Map<String, Object> rawUserData) {

                    }

                    @Override
                    public void onCommentData(Map<String, Object> rawCommentData) {

                    }
                });


                DataHandler.getInstance().getJourneyWithIds(getUserFavorites(userModel), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {
                        JourneyModel journeyModel = new JourneyModel(rawJourneyData, journeyReference);
                        if(journeyModel.title != null) {
                            allFavorites.add(journeyModel);
                            fillRecyclerView(R.id.following_journeys_recycle, R.id.following_journeys_recycle_placeholder, allFavorites);
                        }
                    }

                    @Override
                    public void onUserData(Map<String, Object> rawUserData) {

                    }

                    @Override
                    public void onCommentData(Map<String, Object> rawCommentData) {

                    }
                });

                DataHandler.getInstance().getJourneyWithIds(getUserPlans(userModel), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {
                        JourneyModel journeyModel = new JourneyModel(rawJourneyData, journeyReference);
                        if(journeyModel.title != null) {
                            allPlans.add(journeyModel);
                            fillRecyclerView(R.id.plan_journeys_recycle, R.id.plan_journeys_recycle_placeholder, allPlans);
                        }
                    }

                    @Override
                    public void onUserData(Map<String, Object> rawUserData) {

                    }

                    @Override
                    public void onCommentData(Map<String, Object> rawCommentData) {

                    }
                });
            }

            @Override
            public void onCommentData(Map<String, Object> rawCommentData) {

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // Change statusbar color ===============================


    }

    public ArrayList<String> getUserJourneys(UserModel userModel) {
        ArrayList<String> allJourneys = new ArrayList<String>();
        if(userModel.userJourneys == null) return null;
        for (Map.Entry<String, Object> map : userModel.userJourneys.entrySet()) {
            String journeyModel = (String) map.getValue();
            allJourneys.add(journeyModel);
        }
        return allJourneys;
    }

    public ArrayList<String> getUserFavorites(UserModel userModel) {
        ArrayList<String> allJourneys = new ArrayList<String>();
        if(userModel.loves == null) return null;
        for (Map.Entry<String, Object> map : userModel.loves.entrySet()) {
            String journeyModel = (String) map.getValue();
            allJourneys.add(journeyModel);
        }
        return allJourneys;
    }

    public ArrayList<String> getUserPlans(UserModel userModel) {
        ArrayList<String> allJourneys = new ArrayList<String>();
        if(userModel.plans == null) return null;
        for (Map.Entry<String, Object> map : userModel.plans.entrySet()) {
            String journeyModel = (String) map.getValue();
            allJourneys.add(journeyModel);
        }
        return allJourneys;
    }

    public void fillRecyclerView(int primaryResource, int emptyResource, ArrayList<JourneyModel> journeyModels){

        RecyclerView rvJourneys = (RecyclerView) getActivity().findViewById(primaryResource);

        JourneyAdapter adapter = new JourneyAdapter(getActivity(), journeyModels);
        rvJourneys.setAdapter(adapter);
        rvJourneys.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), zollie.travelblogger.guidee.utils.DividerItemDecoration.HORIZONTAL_LIST);
        rvJourneys.addItemDecoration(itemDecoration);
        if(journeyModels.isEmpty() == true ) showPlaceholderCards(emptyResource);
   //     rvJourneys.setVisibility(View.INVISIBLE);
    }

    public void showPlaceholderCards(int id){
        int card = R.layout.journey_card_placeholder;
        if(id == R.id.following_journeys_recycle_placeholder) card = R.layout.following_card_placeholder;

        View viewToLoad = LayoutInflater.from(
                getActivity()).inflate(
                card, null);
        ((LinearLayout) getActivity().findViewById(id)).addView(viewToLoad);
    //    LinearLayout itemForm=(LinearLayout) viewToLoad.findViewById(R.id.my_journeys_recycle_placeholder);
    //    itemForm.addView(itemForm);

    }

    class AsyncProfilePic extends AsyncTask<Object, Void, UserModel>{


        @Override
        protected UserModel doInBackground(Object... params) {
            UserModel mUser = new UserModel((UserModel)(params[0]));
            //===================== Adding Image to to Horizontal Slide via Glide =========
            try {
                userAvatarGlobal= Glide.with(getActivity())
                        .load(mUser.avatarUrl)
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //=============================================================================
            userAvatarGlobal = imageProcessor.resizeMarkerImage(userAvatarGlobal, 2);
            return mUser;
        }

        @Override
        protected void onPostExecute(UserModel mUser) {
            ImageView mProfileImage = (ImageView) getActivity().findViewById(R.id.prof_pic);
            final float scale = getResources().getDisplayMetrics().density;
            final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bmp = Bitmap.createBitmap((int)(120*scale),(int) (120*scale), conf);
            Canvas canvas1 = new Canvas(bmp);
            Bitmap circleBitmap = imageProcessor.pulseMarker(4, bmp, canvas1, scale*2, userAvatarGlobal);
            circleBitmap = imageProcessor.pulseMarker(4, userAvatarGlobal, canvas1, scale*2, circleBitmap);
            mProfileImage.setImageBitmap(circleBitmap);
            TextView mProfileName = (TextView) getActivity().findViewById(R.id.profile_name);
            mProfileName.setText(mUser.userName);
        }
    }


}