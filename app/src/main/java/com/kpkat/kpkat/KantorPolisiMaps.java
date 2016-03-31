package com.kpkat.kpkat;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class KantorPolisiMaps extends AppCompatActivity implements OnMapReadyCallback {

    GPSTracker gps;

    private GoogleMap mMap;
    private ArrayList<MarkerPolisi> markerPolisiArrayList = new ArrayList<MarkerPolisi>();
    private HashMap<Marker, MarkerPolisi> markerPolisiHashMap;

    double lat;
    double lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kantor_polisi_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        gps = new GPSTracker(KantorPolisiMaps.this);

        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lng = gps.getLongitude();

            LatLng latLng = new LatLng(lat, lng);

            mMap.setMyLocationEnabled(true);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("My Places");
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            markerPolisiHashMap = new HashMap<Marker, MarkerPolisi>();

            markerPolisiArrayList.add(new MarkerPolisi("Polres Bandung Tengah", new Double(-6.910342), new Double(107.639502)));
            markerPolisiArrayList.add(new MarkerPolisi("Polsekta Bandung Wetan", new Double(-6.907328), new Double(107.623549)));
            markerPolisiArrayList.add(new MarkerPolisi("Polsekta Ujung Berung", new Double(-6.910365), new Double(107.703155)));

            plotMarkers(markerPolisiArrayList);

            Location locTujuan = new Location("Polres");
            locTujuan.setLatitude(-6.910342);
            locTujuan.setLongitude(107.639502);
            double jdkt = gps.getLocation().distanceTo(locTujuan);
            Toast.makeText(
                    getApplicationContext(),
                    "Lokasi Anda -\nLat: " + lat + "\nLong: "
                            + lng + "\nJarak : " + String.valueOf(jdkt), Toast.LENGTH_LONG).show();


        } else {
            gps.showSettingAlert();
        }

    }


    private void plotMarkers(ArrayList<MarkerPolisi> markers) {
        if (markers.size() > 0) {
            for (MarkerPolisi myMarker : markers) {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));

                Marker currentMarker = mMap.addMarker(markerOption);
                markerPolisiHashMap.put(currentMarker, myMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    private class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            View v  = getLayoutInflater().inflate(R.layout.infowidow_layout, null);

            MarkerPolisi myMarker = markerPolisiHashMap.get(marker);

            TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);

            markerLabel.setText(myMarker.getmLabel());

            return v;
        }
    }
}
