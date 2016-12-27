package zollie.travelblogger.guidee;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.03..
 */
public interface DataHandlerListener {

    void onJourneyData(Map<String, Object> rawJourneyData);
    void onUserData(Map<String, Object> rawUserData);

}
