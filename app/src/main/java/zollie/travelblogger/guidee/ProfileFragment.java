package zollie.travelblogger.guidee;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

        LinearLayout horitontalLayout = (LinearLayout) getView().findViewById(R.id.scroll_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < 15; i++){



            //final ImageView imageView = new ImageView (getActivity());
            final ImageView imageView = new ImageView(getActivity());
            //imageView.setTag(i);
            imageView.setImageResource(R.drawable.profile_pic);


            imageView.setScaleX(2);
            imageView.setScaleY(2);
            imageView.setBackgroundResource(R.drawable.pic_background);
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

