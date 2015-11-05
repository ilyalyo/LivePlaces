package ilia.liveplaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment {

    MapView mMapView;
    Circle circle;
    GoogleMap googleMap;
    private static final String SHAREDPREFLAT = "lat";
    private static final String SHAREDPREFLNG= "lng";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_location_info, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        double latitude = Double.longBitsToDouble(sharedPref.getLong(SHAREDPREFLAT, 74));
        double longitude = Double.longBitsToDouble(sharedPref.getLong(SHAREDPREFLNG, 40));

        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(1000); // In meters
        circle = googleMap.addCircle(circleOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(10).build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                circle.setCenter(latLng);
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong(SHAREDPREFLAT,Double.doubleToRawLongBits(latLng.latitude));
                editor.putLong(SHAREDPREFLNG,Double.doubleToRawLongBits(latLng.longitude));
                editor.apply();
            }
        });

        SeekBar sb = (SeekBar)v.findViewById(R.id.seekBar);

        assert sb != null;
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circle.setRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button btn = (Button)v.findViewById(R.id.goToSearchBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "IN");
                Bundle bundle = new Bundle();
                bundle.putDouble(AddPlaceActivity.PASS_PARAM_LAT, circle.getCenter().latitude);
                bundle.putDouble(AddPlaceActivity.PASS_PARAM_LNG, circle.getCenter().longitude);
                bundle.putDouble(AddPlaceActivity.PASS_PARAM_RADIUS, circle.getRadius());

                Fragment searchFragment = new AddPlaceSearchFragment();
                searchFragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().
                        replace(R.id.container, searchFragment).commit();
            }
        });
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    private static final String DEBUG_TAG = "ADD_PLACE_MAP";

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}