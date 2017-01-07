package zollie.travelblogger.guidee.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.models.JourneyModel;

/**
 * Created by FuszeneckerZ on 2016.12.31..
 */

public class JourneyView extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_view);
        Bundle intentData = getIntent().getExtras();
        JourneyModel mJourney = (JourneyModel) intentData.getParcelable("ser_journey");
        TextView mJourneySummary = (TextView) findViewById(R.id.journey_summary_content);
        mJourneySummary.setText(mJourney.summary);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
