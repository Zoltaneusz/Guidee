package zollie.travelblogger.guidee.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import zollie.travelblogger.guidee.activities.EditJourneyView;
import zollie.travelblogger.guidee.activities.JourneyView;
import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;
import zollie.travelblogger.guidee.adapters.FollowedAdapter;
import zollie.travelblogger.guidee.adapters.FollowingListener;
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
    ArrayList<UserModel> allFollowers = new ArrayList<UserModel>();
    Bitmap userAvatarGlobal = null;
    ImageProcessor imageProcessor = new ImageProcessor();

    String userAvatarUrl = new String();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;


    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Getting Firebase user ID
        FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser();
        final String firUserID = firUser.getUid();
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
        DataHandler.getInstance().getUserWithId(new String(firUserID), new DataHandlerListener() {
            @Override
            public void onJourneyData(final Map<String, Object> rawJourneyData, String journeyReference) {
                //addMapMarker(journeyModel, mMap);
            }
            @Override
            public void onUserData(Map<String, Object> rawUserData, String userID) {
                allJourneys.clear();
                allFavorites.clear();
                allPlans.clear();
                allFollowers.clear();
                UserModel userModel = new UserModel(rawUserData, userID);
                try {
                    userAvatarUrl = userModel.avatarUrl;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new AsyncProfilePic().execute(userModel);

                //================= Getting journeys of profile =====================================

                DataHandler.getInstance().getJourneyWithIds(getUserJourneys(userModel), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {
                        JourneyModel journeyModel = new JourneyModel(rawJourneyData, journeyReference, true);
                        if(journeyModel.title != null) {
                            allJourneys.add(journeyModel);
                            fillRecyclerView(R.id.my_journeys_recycle, R.id.my_journeys_recycle_placeholder, allJourneys);
                            changeProfileCover();
                        }
                    }

                    @Override
                    public void onUserData(Map<String, Object> rawUserData, String userID) {

                    }

                    @Override
                    public void onCommentData(Map<String, Object> rawCommentData, String commentReference, String journeyIdent) {

                    }
                });


                DataHandler.getInstance().getJourneyWithIds(getUserFavorites(userModel), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {
                        JourneyModel journeyModel = new JourneyModel(rawJourneyData, journeyReference, false);
                        if(journeyModel.title != null) {
                            allFavorites.add(journeyModel);
                            fillRecyclerView(R.id.following_journeys_recycle, R.id.following_journeys_recycle_placeholder, allFavorites);
                        }
                    }

                    @Override
                    public void onUserData(Map<String, Object> rawUserData, String userID) {

                    }

                    @Override
                    public void onCommentData(Map<String, Object> rawCommentData, String commentReference, String journeyIdent) {

                    }
                });

                DataHandler.getInstance().getJourneyWithIds(getUserPlans(userModel), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {
                        JourneyModel journeyModel = new JourneyModel(rawJourneyData, journeyReference, false);
                        if(journeyModel.title != null) {
                            allPlans.add(journeyModel);
                            fillRecyclerView(R.id.plan_journeys_recycle, R.id.plan_journeys_recycle_placeholder, allPlans);
                        }
                    }

                    @Override
                    public void onUserData(Map<String, Object> rawUserData, String userID) {

                    }

                    @Override
                    public void onCommentData(Map<String, Object> rawCommentData, String commentReference, String journeyIdent) {

                    }
                });

                DataHandler.getInstance().getUserFollowing(firUserID, new FollowingListener() {
                    @Override
                    public void onFollowing(UserModel followedUser) {
                        allFollowers.add(followedUser);
                        fillRecyclerViewFollow(R.id.followed_users_recycle, R.id.followed_users_recycle_placeholder, allFollowers);
                    }
                });
            }

            @Override
            public void onCommentData(Map<String, Object> rawCommentData, String commentReference, String journeyIdent) {

            }
        });
        journeyEdit(firUserID);

    }
    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
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

    public void fillRecyclerViewFollow(int primaryResource, int emptyResource, ArrayList<UserModel> users) {
        RecyclerView rvFollows = (RecyclerView) getActivity().findViewById(primaryResource);
        FollowedAdapter adapter = new FollowedAdapter(getActivity(), users);
        rvFollows.setAdapter(adapter);
        rvFollows.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), zollie.travelblogger.guidee.utils.DividerItemDecoration.HORIZONTAL_LIST);
        rvFollows.addItemDecoration(itemDecoration);
        if(users.isEmpty() == true ) showPlaceholderCards(emptyResource);

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
            float scale = getResources().getDisplayMetrics().density;
            userAvatarGlobal = imageProcessor.resizeMarkerImage(userAvatarGlobal, scale*4/3);
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

    public void journeyEdit(final String firUserID){
        FloatingActionButton journeyFAB = (FloatingActionButton) getActivity().findViewById(R.id.profile_j_edit_FAB);
        journeyFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setItems(R.array.journey_edit_FAB, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i){
                            case 0: { // Add new journey
                                String FIRKey = DataHandler.getInstance().createJourneyInFIR(firUserID);
                                JourneyModel mJourney = new JourneyModel(FIRKey, userAvatarUrl, firUserID);

                                Intent toJourneyIntent = new Intent(getActivity(), EditJourneyView.class);
                                toJourneyIntent.putExtra("ser_journey", mJourney);
                                toJourneyIntent.putExtra("parent", "ProfileFragment");
                                getActivity().startActivity(toJourneyIntent);
                                break;
                            }
                            case 1: { //Delete journey
                                Iterator<JourneyModel> iterator = allJourneys.iterator();
                                while(iterator.hasNext()){
                                    JourneyModel journeyM = iterator.next();
                                    if(journeyM.toDelete == true){
                                        DataHandler.getInstance().deleteJourneyInFIR(journeyM.ID, firUserID);
                                        iterator.remove();
                                    }
                                }

                                break;
                            }
                            case 2: { // Cancel
                                break;
                            }

                        }
                    }
                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }
    public void changeProfileCover(){
        if(allJourneys.size() != 0) {
            if (allJourneys.get(0).coverImageUrl != null) {
                ImageView coverImg = (ImageView) getActivity().findViewById(R.id.prof_cover);
                //===================== Adding Image to to Horizontal Slide via Glide =========
                Glide
                        .with(this)
                        .load(allJourneys.get(0).coverImageUrl)
                        .crossFade()
                        .into(coverImg);
                //=============================================================================
            }
        }
    }

}