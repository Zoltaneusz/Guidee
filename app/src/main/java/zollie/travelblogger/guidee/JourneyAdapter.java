package zollie.travelblogger.guidee;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewGroupCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by FuszeneckerZ on 2016.12.28..
 */

public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder>{


    private ArrayList<JourneyModel> allJourneys = new ArrayList<JourneyModel>();
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mCoverImage;
        public TextView mTitle;

        public ViewHolder(View itemView){
            super(itemView);

            mCoverImage = (ImageView) itemView.findViewById(R.id.mJourney);
            mTitle = (TextView) itemView.findViewById(R.id.mJourneyTitle);
        }
    }

    public JourneyAdapter(Context context, ArrayList<JourneyModel> allJourneyList) {
        allJourneys = allJourneyList;
        mContext = context;

    /*    for(int i = 0; i< Array.getLength(mJourneys); i++)
        {
            mTitles[i] = mJourneys[i].title;
            mCoverImgageUrls[i] = mJourneys[i].coverImageUrl;

        }
*/    }
    private Context getContext() {
        return mContext;
    }

    @Override
    public JourneyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(JourneyAdapter.ViewHolder holder, int position) {
        JourneyModel mJourney = allJourneys.get(position);

        TextView textView = holder.mTitle;
        textView.setText(mJourney.title);
        ImageView imageView = holder.mCoverImage;
        //===================== Adding Image to to Horizontal Slide via Glide =========
        Glide
                .with(mContext)
                .load(mJourney.coverImageUrl)
                .centerCrop()
                .override(160, 160)
                .crossFade()
                .into(imageView);
        //=============================================================================

    }

    @Override
    public int getItemCount() {
        return allJourneys.size();
    }
}
