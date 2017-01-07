package zollie.travelblogger.guidee.activities;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import zollie.travelblogger.guidee.fragments.ExploreFragment;
import zollie.travelblogger.guidee.fragments.ProfileFragment;
import zollie.travelblogger.guidee.R;


/**
 * Created by zoltanfuszenecker on 10/29/16.
 */

public class MainActivity extends Activity {

    private ExploreFragment _exploreMapFrag = new ExploreFragment();
    private ProfileFragment _profileFrag = new ProfileFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pushing MapView Fragment
        Fragment fragment = Fragment.instantiate(this, ExploreFragment.class.getName());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.contentContainer, fragment);
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
                        fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.contentContainer, _exploreMapFrag).commit();
                        break;

                    case R.id.tab_profile:
                        fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.contentContainer, _profileFrag).commit();
                        break;

                }


            }
        });


    }
}
