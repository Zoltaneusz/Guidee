package zollie.travelblogger.guidee;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


/**
 * Created by zsomborfuszenecker on 10/29/16.
 */

public class MainActivity extends Activity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager ft = getFragmentManager();
                ft.beginTransaction().replace(R.id.contentContainer, new ExploreFragment()).commit();

            }
        });


    }
}