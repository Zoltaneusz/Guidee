package zollie.travelblogger.guidee.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;

import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;

/**
 * Created by FuszeneckerZ on 2016.12.27..
 */

public class UserModel {
    public String avatarUrl;
    public String userFIRId;
    public String userName;
    public String summary;
    public Map<String, Object> userJourneys = null;
    public Map<String, Object> following = null;
    public Map<String, Object> loves = null;
    public Map<String, Object> plans = null;

    private static UserModel mInstance = null;

    public UserModel()
    {
        this.avatarUrl = "https://firebasestorage.googleapis.com/v0/b/guidee-f0453.appspot.com/o/images%2F9F4DD9C8-5465-464B-9249-9127AD09E729.jpg?alt=media&token=32a14ec9-a298-4c68-89b3-211eb0f86e7e";
        this.userName = "Your user name";
        this.summary = "Description about you";
    }

    public UserModel(Map<String, Object> rawUserModel, String userID) {

        try {
            this.avatarUrl = (String) rawUserModel.get("avatarUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //    this.userId = (long) rawUserModel.get("id");
        try {
            this.userName = (String) rawUserModel.get("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.userFIRId = userID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.summary = (String) rawUserModel.get("summary");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.userJourneys = (Map<String, Object>) rawUserModel.get("journeys");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.following = (Map<String, Object>) rawUserModel.get("following");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.loves = (Map<String, Object>) rawUserModel.get("loved");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.plans = (Map<String, Object>) rawUserModel.get("plans");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public UserModel(UserModel userModel){
        this.avatarUrl = userModel.avatarUrl;
        this.userName = userModel.userName;
        this.summary = userModel.summary;
        this.userFIRId = userModel.userFIRId;
        this.userJourneys = userModel.userJourneys;
        this.following = userModel.following;
        this.loves = userModel.loves;
        this.plans = userModel.plans;
    }
   /* public static synchronized boolean getInstance(final JourneyModel mJourney){
        if(null == mInstance) {
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
                    }


                }

                @Override
                public void onCommentData(Map<String, Object> rawCommentData) {

                }

            });
            for(String string : allJourneys){
                if(string.matches(mJourney.ID)) return true;
            }
        }

        return false;
    }*/

}
