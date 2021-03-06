package zollie.travelblogger.guidee.activities;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;


import com.google.android.gms.maps.model.LatLng;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import zollie.travelblogger.guidee.fragments.ExploreFragment;
import zollie.travelblogger.guidee.fragments.FaceLoginFragment;
import zollie.travelblogger.guidee.fragments.ProfileFragment;
import zollie.travelblogger.guidee.R;


/**
 * Created by zoltanfuszenecker on 10/29/16.
 */

public class MainActivity extends AppCompatActivity  {

    private ExploreFragment _exploreMapFrag = new ExploreFragment();
    private ProfileFragment _profileFrag = new ProfileFragment();
    private FaceLoginFragment _loginFrag = new FaceLoginFragment();
    double showedLat = 39.43681513892361;
    double showedLng = 3.224011088360298;
    public boolean googleSignOut = false;
    String previousFragment = new String("0");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printHashKey();
        setContentView(R.layout.activity_main);

        Bundle intentData = null;
        try {
            intentData = getIntent().getExtras();
            showedLat = intentData.getDouble("lat");
            showedLng = intentData.getDouble("lng");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        showedLat = (showedLat == 0) ? 47.49801 : showedLat;
        showedLng = (showedLng == 0) ? 19.03991 : showedLng;


        // Pushing MapView Fragment
        Fragment fragment = Fragment.instantiate(this, ExploreFragment.class.getName());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.contentContainer, fragment); // Pushing Facelogin
        ft.commit();


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
      //  bottomBar.setHovered(true);
      //  if (Build.VERSION.SDK_INT >= 21)      bottomBar.setElevation(100);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fm;
                switch(tabId)
                {
                    case R.id.tab_explore:
                    /*    Bundle bundle = new Bundle();
                        bundle.putDouble("lat", showedLat);
                        bundle.putDouble("lng", showedLng);
                        _exploreMapFrag.setArguments(bundle);*/
                        fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.contentContainer, _exploreMapFrag).commit();
                        break;

                    case R.id.tab_profile:
                        fm = getFragmentManager();
                        //fm.beginTransaction().replace(R.id.contentContainer, _profileFrag).commit();
                        fm.beginTransaction().replace(R.id.contentContainer, _loginFrag).commit();
                        break;

                }


            }
        });


    }
    public Fragment getLoginFrag(){return _loginFrag;}
    public Fragment getProfileFrag(){return _profileFrag;}
    public double getShowedLat(){
        return  showedLat;
    }
    public double getShowedLng(){
        return  showedLng;
    }
    public void setGoogleSignOut(boolean inp){
        this.googleSignOut = inp;
    }
    public boolean getGoogleSignOut(){
        return this.googleSignOut;
    }
    public boolean previousFragmentEquals(String prevFrag){
        if(prevFrag.equals(previousFragment))
            return true;
        else return false;
    }
    public String setPreviousFragment(String prevFrag){
        previousFragment = prevFrag;
        return previousFragment;
    }

    public void printHashKey(){

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "zollie.travelblogger.guidee",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

