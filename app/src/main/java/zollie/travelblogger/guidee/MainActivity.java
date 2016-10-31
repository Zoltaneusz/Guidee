package zollie.travelblogger.guidee;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


/**
 * Created by zoltanfuszenecker on 10/29/16.
 */

public class MainActivity extends Activity {

    private ExploreFragment _exploreMap = new ExploreFragment();

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
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.contentContainer, _exploreMap).commit();

            }
        });


    }
}