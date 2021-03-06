package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

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
        EventAdapter.ViewHolder vh = new EventAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        final EventModel mEvent = allEvents.get(position);

        TextView coverText = holder.mTitle;
        coverText.setText("#" + String.valueOf(position+1) + " " + mEvent.title);
        TextView summaryText = holder.mSummary;
        summaryText.setText(mEvent.summary);
        final ImageView imageView = holder.mCoverImage;
        if(!mEvent.carouselModels.isEmpty()) {
            if (mEvent.carouselModels.get(0).carouselType == CarouselModel.CarouselType.IMAGE) {
                if (mEvent.carouselModels.get(0).imageUrl != null) {
                    //===================== Adding Image to to Horizontal Slide via Glide =========
                    RequestOptions options = new RequestOptions();
                    options.centerCrop();
                    options.override(160, 160);
                    Glide
                            .with(mContext)
                            .load(mEvent.carouselModels.get(0).imageUrl)
                            .apply(options)
                            .transition(new DrawableTransitionOptions().crossFade())
                            .into(imageView);
                    //=============================================================================
                }
            } else {
                //===================== Load Video thumbnail to Horizontal Slide ==============


                //=============================================================================
            }
        }
        holder.mEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEventIntent = new Intent(mContext, EventView.class);
                toEventIntent.putExtra("ser_event", mEvent);
                mContext.startActivity(toEventIntent);
            }
        });

        holder.mEvent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mEvent.toDelete == 1){  // Long click happened in EditEventView, therefore Carousel model is deletable
                    mEvent.toDelete = 2; // Prepare Carousel model for deletion
                    imageView.setColorFilter(ContextCompat.getColor(mContext,R.color.PressedTint));
                    //                   holder.itemView.setPressed(true);
                }
                else if(mEvent.toDelete == 2) {  // Second long click happened; user doesn't want to delete the item any more.
                    mEvent.toDelete = 1;
                    imageView.setColorFilter(Color.argb(0, 255, 255, 255));
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return allEvents.size();
    }
}
