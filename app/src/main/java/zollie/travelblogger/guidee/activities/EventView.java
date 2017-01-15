package zollie.travelblogger.guidee.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.adapters.CarouselAdapter;
import zollie.travelblogger.guidee.adapters.EventAdapter;
import zollie.travelblogger.guidee.models.CarouselModel;
import zollie.travelblogger.guidee.models.EventModel;

/**
 * Created by FuszeneckerZ on 2017.01.09..
 */

public class EventView extends YouTubeBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        Bundle intentData = getIntent().getExtras();
        EventModel mEvent = (EventModel) intentData.getParcelable("ser_event");
        TextView mEventSummary = (TextView) findViewById(R.id.event_summary_content);
        try {
            mEventSummary.setText(mEvent.summary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fillRecyclerView(R.id.event_pictures_recycle_test, R.id.event_pictures_recycle_placeholder, mEvent.carouselModels);
    }

    public void fillRecyclerView(int primaryResource, int emptyResource, ArrayList<CarouselModel> carouselModels){

        RecyclerView rvCarousels = (RecyclerView) findViewById(primaryResource);

        CarouselAdapter adapter = new CarouselAdapter(this, carouselModels);
        rvCarousels.setAdapter(adapter);
        rvCarousels.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, zollie.travelblogger.guidee.utils.DividerItemDecoration.HORIZONTAL_LIST);
        rvCarousels.addItemDecoration(itemDecoration);
        //      if(eventModels.isEmpty() == true ) showPlaceholderCards(emptyResource);
        //     rvJourneys.setVisibility(View.INVISIBLE);
    }
}
