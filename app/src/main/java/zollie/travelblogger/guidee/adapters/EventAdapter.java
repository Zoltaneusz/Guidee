package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.activities.EventView;
import zollie.travelblogger.guidee.activities.JourneyView;
import zollie.travelblogger.guidee.models.CarouselModel;
import zollie.travelblogger.guidee.models.EventModel;

/**
 * Created by FuszeneckerZ on 2017.01.07..
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{


    private ArrayList<EventModel> allEvents = new ArrayList<EventModel>();
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mCoverImage;
        public TextView mTitle;
        public TextView mSummary;
        public RelativeLayout mEvent;

        public ViewHolder(View itemView){
            super(itemView);

            mCoverImage = (ImageView) itemView.findViewById(R.id.event_cover_mLoved);
            mTitle = (TextView) itemView.findViewById(R.id.event_title_right);
            mEvent = (RelativeLayout) itemView.findViewById(R.id.event_card_placeholder);
            mSummary = (TextView) itemView.findViewById(R.id.event_summary_right);
        }

    }

    public EventAdapter(Context context, ArrayList<EventModel> allEventList) {
        allEvents = allEventList;
        mContext = context;

    }
    private Context getContext() {
        return mContext;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        EventAdapter.ViewHolder vh = new EventAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        final EventModel mEvent = allEvents.get(position);

        TextView textView = holder.mTitle;
        textView.setText(mEvent.title);
        ImageView imageView = holder.mCoverImage;
        if(mEvent.carouselModels.get(0).carouselType == CarouselModel.CarouselType.IMAGE) {
            //===================== Adding Image to to Horizontal Slide via Glide =========
            Glide
                    .with(mContext)
                    .load(mEvent.carouselModels.get(0).imageUrl)
                    .centerCrop()
                    .override(160, 160)
                    .crossFade()
                    .into(imageView);
            //=============================================================================
        }
        else {
            //===================== Load Video to Horizontal Slide ========================


            //=============================================================================
        }
        holder.mEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEventIntent = new Intent(mContext, EventView.class);
                toEventIntent.putExtra("ser_event", mEvent);
                mContext.startActivity(toEventIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allEvents.size();
    }
}
