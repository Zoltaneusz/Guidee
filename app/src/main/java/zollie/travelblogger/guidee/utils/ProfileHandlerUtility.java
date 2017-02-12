package zollie.travelblogger.guidee.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import zollie.travelblogger.guidee.activities.EventView;

/**
 * Created by FuszeneckerZ on 2017.02.12..
 */

public class ProfileHandlerUtility {
    public FirebaseUser mUser;

    public ProfileHandlerUtility(){
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public boolean getUserEligibility(EventView mEvent){

        return true;
    }

}
