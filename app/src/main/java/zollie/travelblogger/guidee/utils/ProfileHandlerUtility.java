package zollie.travelblogger.guidee.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;

import zollie.travelblogger.guidee.activities.EventView;
import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;
import zollie.travelblogger.guidee.models.JourneyModel;
import zollie.travelblogger.guidee.models.UserModel;

/**
 * Created by FuszeneckerZ on 2017.02.12..
 */

public class ProfileHandlerUtility {
    public FirebaseUser mUser;

    public ProfileHandlerUtility(){
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public JourneyModel getJourneyWriteRight(final JourneyModel mJourney){
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
                        if(string.matches(mJourney.ID)) mJourney.userEligible = true;
                    }
                }
            }

            @Override
            public void onCommentData(Map<String, Object> rawCommentData) {

            }

        });


        return mJourney;
    }

}
