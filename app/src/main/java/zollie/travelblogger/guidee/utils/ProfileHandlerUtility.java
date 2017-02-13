package zollie.travelblogger.guidee.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;

import zollie.travelblogger.guidee.activities.EventView;
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

    public boolean getJourneyWriteRight(JourneyModel mJourney){
        ArrayList<String> allJourneys = new ArrayList<String>();
        UserModel mUser = UserModel.getInstance();
        for (Map.Entry<String, Object> map : mUser.userJourneys.entrySet()) {
            String journeyModel = (String) map.getValue();
            allJourneys.add(journeyModel);
        }
        for(String string : allJourneys){
            if(string.matches(mJourney.ID)) return true;
        }
        return false;
    }

}
