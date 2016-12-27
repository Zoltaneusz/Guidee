package zollie.travelblogger.guidee;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.03..
 */

public class DataHandler {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();



    private static DataHandler myInstance = null;

    private String myProfPic;

    private DataHandler(){
        myProfPic = "Default";
    }

    public static DataHandler getInstance(){
        if(myInstance == null){
            myInstance = new DataHandler();
        }
        return myInstance;
    }

    public String getProfPic(){
        return this.myProfPic;
    }

    public void getJourneys(final DataHandlerListener dataHandlerListener) {
        DatabaseReference mJourneyRef = mRootRef.child("Journeys");
        mJourneyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> kutya = (Map<String, Object>) dataSnapshot.getValue();
                dataHandlerListener.onJourneyData(kutya);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserWithId(String userId, final DataHandlerListener dataHandlerListener) {
        DatabaseReference mUserReference = mRootRef.child("Users");
       // Map<String, Object> mUser = (Map<String, Object>) mUserReference.child(userId);
        mUserReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                dataHandlerListener.onUserData(userInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void setProfPic(String value){
        myProfPic = value;
    }

}
