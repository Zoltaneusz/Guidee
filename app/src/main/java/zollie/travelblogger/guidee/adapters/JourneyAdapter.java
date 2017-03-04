package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.activities.JourneyView;
import zollie.travelblogger.guidee.models.JourneyModel;

/**
 * Created by FuszeneckerZ on 2016.12.28..
 */

public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder>{


    private ArrayList<JourneyModel> allJourneys = new ArrayList<JourneyModel>();
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mCoverImage;
        public TextView mTitle;
        public FrameLayout mJourney;

        public ViewHolder(View itemView){
            super(itemView);

            mCoverImage = (ImageView) itemView.findViewById(R.id.mJourney);
            mTitle = (TextView) itemView.findViewById(R.id.mJourneyTitle);
            mJourney = (FrameLayout) itemView.findViewById(R.id.mJourneyCard);
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
        final JourneyModel mJourney = allJourneys.get(position);

        TextView textView = holder.mTitle;
        textView.setText(mJourney.title);
        final ImageView imageView = holder.mCoverImage;
        //===================== Adding Image to to Horizontal Slide via Glide =========
        Glide
                .with(mContext)
                .load(mJourney.coverImageUrl)
                .centerCrop()
                .override(160, 160)
                .crossFade()
                .into(imageView);
        //=============================================================================
        holder.mJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toJourneyIntent = new Intent(mContext, JourneyView.class);
                toJourneyIntent.putExtra("ser_journey", mJourney);
                mContext.startActivity(toJourneyIntent);
            }
        });
        holder.mJourney.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                if(mJourney.userEligible == true){
                    if(mJourney.toDelete == false) {
                        imageView.setColorFilter(ContextCompat.getColor(mContext, R.color.PressedTint));
                        mJourney.toDelete=true;
                    }
                    else if(mJourney.toDelete == true){
                        imageView.setColorFilter(Color.argb(0, 255, 255, 255));
                        mJourney.toDelete=false;
                    }
                }

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return allJourneys.size();
    }
}
