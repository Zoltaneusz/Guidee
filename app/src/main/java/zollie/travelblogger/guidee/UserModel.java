package zollie.travelblogger.guidee;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

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
}
