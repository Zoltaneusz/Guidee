package zollie.travelblogger.guidee;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by FuszeneckerZ on 2016.11.27..
 */

public class ProfileFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        return rootView;
    }

    @Override
    public void onResume() {

        // Change statusbar color ===============================
        if (Build.VERSION.SDK_INT >= 21) {

            Window window = getActivity().getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.lightGreen));
        }
        //========================================================

        LinearLayout horitontalLayout = (LinearLayout) getView().findViewById(R.id.scroll_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < 15; i++){



            //final ImageView imageView = new ImageView (getActivity());
            final ImageView imageView = new ImageView(getActivity());
            //imageView.setTag(i);
            imageView.setImageResource(R.drawable.profile_pic);
            imageView.setBackgroundResource(R.drawable.pic_background);

            imageView.setScaleX(2);
            imageView.setScaleY(2);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            horitontalLayout.addView(imageView, params);

            imageView.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    //      Log.e("Tag",""+imageView.getTag());
                }
            });
        }
//       horitontalLayout.setLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT);
        super.onResume();
    }
}

