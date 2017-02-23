package zollie.travelblogger.guidee.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.adapters.CommentAdapter;
import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;
import zollie.travelblogger.guidee.adapters.EventAdapter;
import zollie.travelblogger.guidee.adapters.JourneyAdapter;
import zollie.travelblogger.guidee.models.CommentModel;
import zollie.travelblogger.guidee.models.EventModel;
import zollie.travelblogger.guidee.models.JourneyModel;
import zollie.travelblogger.guidee.models.UserModel;
import zollie.travelblogger.guidee.utils.ProfileHandlerUtility;

/**
 * Created by FuszeneckerZ on 2016.12.31..
 */

public class EditJourneyView extends Activity{

    ArrayList<CommentModel> allComments = new ArrayList<CommentModel>();
    boolean userEditEight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey_view);
        Bundle intentData = getIntent().getExtras();
        JourneyModel mJourney = (JourneyModel) intentData.getParcelable("ser_journey");
        if(mJourney.coverImageUrl != null){
            ImageView coverImage = (ImageView ) findViewById(R.id.edit_journey_imgFirst);
            //===================== Adding Image to to Horizontal Slide via Glide =========
            Glide
                    .with(this)
                    .load(mJourney.coverImageUrl)
                    .crossFade()
                    .into(coverImage);
            //=============================================================================

        }

        EditText  mJourneySummary = (EditText) findViewById(R.id.edit_journey_summary_content);
        try {
            mJourneySummary.setText(mJourney.summary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EditText mJourneyTitle = (EditText) findViewById(R.id.edit_journey_name);
        try {
            mJourneyTitle.setText(mJourney.title);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.edit_journey_save_FAB);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        FloatingActionButton cancelButton = (FloatingActionButton) findViewById(R.id.edit_journey_cancel_FAB);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        //Get user eligibility for writing this journey
        new AsyncEditRightCheck().execute(mJourney);

        fillRecyclerView(R.id.edit_journey_events_recycle, R.id.edit_journey_events_recycle_placeholder, mJourney.eventModels);

        DataHandler.getInstance().getCommentsWithID(mJourney.ID, new DataHandlerListener() {
            @Override
            public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {

            }

            @Override
            public void onUserData(Map<String, Object> rawUserData) {

            }

            @Override
            public void onCommentData(Map<String, Object> rawCommentData) {
                CommentModel commentModel = new CommentModel(rawCommentData);
                allComments.add(commentModel);
                fillCommentsRecyclerView(R.id.edit_journey_comments_recycle, R.id.edit_journey_comments_recycle_placeholder, allComments);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void fillRecyclerView(int primaryResource, int emptyResource, ArrayList<EventModel> eventModels){

        RecyclerView rvEvents = (RecyclerView) findViewById(primaryResource);

        EventAdapter adapter = new EventAdapter(this, eventModels);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, zollie.travelblogger.guidee.utils.DividerItemDecoration.VERTICAL_LIST);
        rvEvents.addItemDecoration(itemDecoration);
        //      if(eventModels.isEmpty() == true ) showPlaceholderCards(emptyResource);
        //     rvJourneys.setVisibility(View.INVISIBLE);
    }
    public void fillCommentsRecyclerView(int primaryResource, int emptyResource, ArrayList<CommentModel> commentModels){

        RecyclerView rvEvents = (RecyclerView) findViewById(primaryResource);

        CommentAdapter adapter = new CommentAdapter(this, commentModels);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, zollie.travelblogger.guidee.utils.DividerItemDecoration.VERTICAL_LIST);
        rvEvents.addItemDecoration(itemDecoration);
        //      if(eventModels.isEmpty() == true ) showPlaceholderCards(emptyResource);
        //     rvJourneys.setVisibility(View.INVISIBLE);
    }

    class AsyncEditRightCheck extends AsyncTask<Object, Void, JourneyModel>{
        @Override
        protected JourneyModel doInBackground(Object... params) {
            final JourneyModel mJourney =  (JourneyModel) params[0];
            ProfileHandlerUtility mUtility = new ProfileHandlerUtility();
            boolean editRight = false;

            final ArrayList<String> allJourneys = new ArrayList<String>();
            FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser();
            String firUserID = firUser.getUid();
            DataHandler.getInstance().getUserStringWithId(new String(firUserID), new DataHandlerListener() {
                @Override
                public void onJourneyData(final Map<String, Object> rawJourneyData, String journeyReference) {
                }

                @Override
                public void onUserData(Map<String, Object> rawUserData) {
                    UserModel mInstance = new UserModel(rawUserData);
                    for (Map.Entry<String, Object> map : mInstance.userJourneys.entrySet()) {
                        String journeyModel = (String) map.getValue();
                        allJourneys.add(journeyModel);
                        for(String string : allJourneys){
                            if(string.matches(mJourney.ID)) {
                                mJourney.userEligible = true;
                                mJourney.setEventsEligibility();
                            }
                        }
                    }
                }

                @Override
                public void onCommentData(Map<String, Object> rawCommentData) {

                }

            });

            return mJourney;
        }

        @Override
        protected void onPostExecute(JourneyModel mJourney) {
            userEditEight = mJourney.userEligible;
        }
    }
}
