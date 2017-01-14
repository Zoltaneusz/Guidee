package zollie.travelblogger.guidee.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.models.EventModel;

/**
 * Created by FuszeneckerZ on 2017.01.09..
 */

public class EventView extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        Bundle intentData = getIntent().getExtras();
        EventModel mEvent = (EventModel) intentData.getParcelable("ser_event");
        TextView mJourneySummary = (TextView) findViewById(R.id.journey_summary_content);
        mJourneySummary.setText(mEvent.summary);
    }
}
