package zollie.travelblogger.guidee.adapters;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.03..
 */
public interface DataHandlerListener {

    void onJourneyData(Map<String, Object> rawJourneyData, String journeyID);
    void onUserData(Map<String, Object> rawUserData);

}
