package zollie.travelblogger.guidee.models;

import java.util.Map;

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

    public UserModel(Map<String, Object> rawUserModel) {

        this.avatarUrl = (String) rawUserModel.get("avatarUrl");
    //    this.userId = (long) rawUserModel.get("id");
        this.userName = (String) rawUserModel.get("name");
        this.summary = (String) rawUserModel.get("summary");
        this.userJourneys = (Map<String, Object>) rawUserModel.get("journeys");
        this.following = (Map<String, Object>) rawUserModel.get("following");
        this.loves = (Map<String, Object>) rawUserModel.get("loves");
        this.plans = (Map<String, Object>) rawUserModel.get("plans");

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
}
