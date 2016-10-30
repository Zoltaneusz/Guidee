package zollie.travelblogger.guidee;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by zsomborfuszenecker on 10/29/16.
 */



public class ExploreFragment extends Fragment implements OnMapReadyCallback {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.exploremap);
        fragment.getMapAsync(this);
    }

    @Override
       public void onMapReady(GoogleMap googleMap) {

       }
}
