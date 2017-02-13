package zollie.travelblogger.guidee.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;

/**
 * Created by FuszeneckerZ on 2016.12.27..
 */

public class UserModel {
    public String avatarUrl;
    public long userId;
    public String userName;
    public String summary;
    public Map<String, Object> userJourneys = null;
    public Map<String, Object> following = null;
    public Map<String, Object> loves = null;
    public Map<String, Object> plans = null;

    private static UserModel mInstance = null;

    public UserModel(Map<String, Object> rawUserModel) {

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
        this.userJourneys = userModel.userJourneys;
        this.following = userModel.following;
        this.loves = userModel.loves;
        this.plans = userModel.plans;
    }
    public static synchronized UserModel getInstance(){
        if(null == mInstance) {
            FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser();
            String firUserID = firUser.getUid();
            DataHandler.getInstance().getUserWithId(new String(firUserID), new DataHandlerListener() {
                @Override
                public void onJourneyData(final Map<String, Object> rawJourneyData, String journeyReference) {
                }

                @Override
                public void onUserData(Map<String, Object> rawUserData) {
                    UserModel mInstance = new UserModel(rawUserData);
                }

                @Override
                public void onCommentData(Map<String, Object> rawCommentData) {

                }

            });
        }
        return mInstance;
    }

}
