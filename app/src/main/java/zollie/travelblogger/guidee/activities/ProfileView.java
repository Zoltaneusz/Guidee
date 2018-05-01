package zollie.travelblogger.guidee.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;
import zollie.travelblogger.guidee.adapters.FollowedAdapter;
import zollie.travelblogger.guidee.adapters.FollowingListener;
import zollie.travelblogger.guidee.adapters.JourneyAdapter;
import zollie.travelblogger.guidee.models.JourneyModel;
import zollie.travelblogger.guidee.models.UserModel;
import zollie.travelblogger.guidee.utils.ImageProcessor;

/**
 * Created by FuszeneckerZ on 2017. 03. 18..
 */

public class ProfileView extends AppCompatActivity {
    ArrayList<JourneyModel> allJourneys = new ArrayList<JourneyModel>(30);
    ArrayList<JourneyModel> allFavorites = new ArrayList<JourneyModel>(30);
    ArrayList<UserModel> allFollowers = new ArrayList<UserModel>(30);
    Bitmap userAvatarGlobal = null;
    ImageProcessor imageProcessor = new ImageProcessor(this);
    String userAvatarUrl = new String();
    Context mContext;
    FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser(); // Should be in onResume() with almost every other method.
    RecyclerView.ItemDecoration itemDecoration = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDecoration = new
                DividerItemDecoration(this, zollie.travelblogger.guidee.utils.DividerItemDecoration.HORIZONTAL_LIST);
        setContentView(R.layout.activity_profile_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle intentData = getIntent().getExtras();
        final String ownerID = intentData.getString("owner_ID");
        mContext = this;

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        DataHandler.getInstance().getUserWithId(new String(ownerID), new DataHandlerListener() {
            @Override
            public void onJourneyData(final Map<String, Object> rawJourneyData, String journeyReference) {
                //addMapMarker(journeyModel, mMap);
            }
            @Override
            public void onUserData(Map<String, Object> rawUserData, String userID) {
                allJourneys.clear();
                allFavorites.clear();
                //             allPlans.clear();
                allFollowers.clear();
                final UserModel userModel = new UserModel(rawUserData, userID);
                // ========================= Following user on click ===================================
                FloatingActionButton followUser = (FloatingActionButton) findViewById(R.id.owner_follow_icon);
                DataHandler.getInstance().setUserFollowed(userModel, firUser, followUser);
                followUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataHandler.getInstance().followUserInFIR(userModel, firUser, (FloatingActionButton) v, mContext);
                    }
                });
                //======================================================================================
                try {
                    userAvatarUrl = userModel.avatarUrl;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new ProfileView.AsyncProfilePic().execute(userModel);

                //================= Getting journeys of profile =====================================

                DataHandler.getInstance().getJourneyWithIds(getUserJourneys(userModel), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {
                        JourneyModel journeyModel = new JourneyModel(rawJourneyData, journeyReference, true);
                        if(journeyModel.title != null) {
                            allJourneys.add(journeyModel);
                            fillRecyclerView(R.id.owner_journeys_recycle, R.id.owner_journeys_recycle_placeholder, allJourneys);
                            changeProfileCover(userModel);
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
                            fillRecyclerView(R.id.owner_following_journeys_recycle, R.id.owner_following_journeys_recycle_placeholder, allFavorites);
                        }
                    }

                    @Override
                    public void onUserData(Map<String, Object> rawUserData, String userID) {

                    }

                    @Override
                    public void onCommentData(Map<String, Object> rawCommentData, String commentReference, String journeyIdent) {

                    }
                });

               /* DataHandler.getInstance().getJourneyWithIds(getUserPlans(userModel), new DataHandlerListener() {
                    @Override
                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {
                        JourneyModel journeyModel = new JourneyModel(rawJourneyData, journeyReference, false);
                        if(journeyModel.title != null) {
                            allPlans.add(journeyModel);
                            fillRecyclerView(R.id.owner_plan_journeys_recycle, R.id.owner_plan_journeys_recycle_placeholder, allPlans);
                        }
                    }

                    @Override
                    public void onUserData(Map<String, Object> rawUserData, String userID) {

                    }

                    @Override
                    public void onCommentData(Map<String, Object> rawCommentData, String commentReference, String journeyIdent) {

                    }
                });
*/
                DataHandler.getInstance().getUserFollowing(ownerID, new FollowingListener() {
                    @Override
                    public void onFollowing(UserModel followedUser) {
                        allFollowers.add(followedUser);
                        fillRecyclerViewFollow(R.id.owner_followed_users_recycle, R.id.owner_followed_users_recycle_placeholder, allFollowers);
                    }
                });
            }

            @Override
            public void onCommentData(Map<String, Object> rawCommentData, String commentReference, String journeyIdent) {

            }
        });
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
/*
    public ArrayList<String> getUserPlans(UserModel userModel) {
        ArrayList<String> allJourneys = new ArrayList<String>();
        if(userModel.plans == null) return null;
        for (Map.Entry<String, Object> map : userModel.plans.entrySet()) {
            String journeyModel = (String) map.getValue();
            allJourneys.add(journeyModel);
        }
        return allJourneys;
    }
*/
    public void fillRecyclerView(int primaryResource, int emptyResource, ArrayList<JourneyModel> journeyModels){

        RecyclerView rvJourneys = (RecyclerView) findViewById(primaryResource);

        JourneyAdapter adapter = new JourneyAdapter(this, journeyModels);
        rvJourneys.setAdapter(adapter);
        rvJourneys.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvJourneys.removeItemDecoration(itemDecoration);
        rvJourneys.addItemDecoration(itemDecoration);
        if(journeyModels.isEmpty() == true ) showPlaceholderCards(emptyResource);
        //     rvJourneys.setVisibility(View.INVISIBLE);
    }

    public void fillRecyclerViewFollow(int primaryResource, int emptyResource, ArrayList<UserModel> users) {
        RecyclerView rvFollows = (RecyclerView) findViewById(primaryResource);
        FollowedAdapter adapter = new FollowedAdapter(this, users);
        rvFollows.setAdapter(adapter);
        rvFollows.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFollows.removeItemDecoration(itemDecoration);
        rvFollows.addItemDecoration(itemDecoration);
        if(users.isEmpty() == true ) showPlaceholderCards(emptyResource);

    }

    public void showPlaceholderCards(int id){
        int card = R.layout.journey_card_placeholder;
        if(id == R.id.following_journeys_recycle_placeholder) card = R.layout.following_card_placeholder;

        View viewToLoad = LayoutInflater.from(
                this).inflate(
                card, null);
        ((LinearLayout) findViewById(id)).addView(viewToLoad);
        //    LinearLayout itemForm=(LinearLayout) viewToLoad.findViewById(R.id.my_journeys_recycle_placeholder);
        //    itemForm.addView(itemForm);

    }

    class AsyncProfilePic extends AsyncTask<Object, Void, UserModel> {


        @Override
        protected UserModel doInBackground(Object... params) {
            UserModel mUser = new UserModel((UserModel)(params[0]));
            //===================== Adding Image to to Horizontal Slide via Glide =========
            try {
                userAvatarGlobal= Glide.with(mContext)
                        .asBitmap()
                        .load(mUser.avatarUrl)
                        .into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //=============================================================================

            return mUser;
        }

        @Override
        protected void onPostExecute(UserModel mUser) {
            float scale = getResources().getDisplayMetrics().density;
            if(userAvatarGlobal != null) {
                userAvatarGlobal = imageProcessor.resizeMarkerImage(userAvatarGlobal, scale * 4 / 3);
                ImageView mProfileImage = (ImageView) findViewById(R.id.owner_prof_pic);
                final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                Bitmap bmp = Bitmap.createBitmap((int) (120 * scale), (int) (120 * scale), conf);
                Canvas canvas1 = new Canvas(bmp);
                Bitmap circleBitmap = imageProcessor.pulseMarker(4, bmp, canvas1, scale * 2, userAvatarGlobal, false);
                circleBitmap = imageProcessor.pulseMarker(4, userAvatarGlobal, canvas1, scale * 2, circleBitmap, false);
                mProfileImage.setImageBitmap(circleBitmap);
            }
    //        TextView mProfileName = (TextView) findViewById(R.id.owner_profile_name);
      //      mProfileName.setText(mUser.userName);
        }
    }
    public void changeProfileCover(UserModel userModel){

        // ================== Change Scrolling Toolbar ========================================
        Toolbar toolbar = (Toolbar) findViewById(R.id.prof_act_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.backButton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.prof_act_collapse_toolbar);
        collapsingToolbar.setTitle(userModel.userName);
        ImageView appBarImage = (ImageView) findViewById(R.id.prof_act_appbar_image);
        //===================== Adding Image to to Horizontal Slide via Glide =========
        if(allJourneys.size() != 0) {
            if (allJourneys.get(0).coverImageUrl != null) {
                Glide
                        .with(this)
                        .load(allJourneys.get(0).coverImageUrl)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(appBarImage);
            }
        }
        //=============================================================================
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
