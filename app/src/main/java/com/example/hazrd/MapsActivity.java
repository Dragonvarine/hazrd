package com.example.hazrd;

import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        try{
            boolean isSuccess = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.style_maps)
            );

            if(!isSuccess)
                Toast.makeText(this,"Map Style loads failed", Toast.LENGTH_SHORT).show();
        }
        catch (Resources.NotFoundException ex){
            ex.printStackTrace();
        }


        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng bulgaria = new LatLng(42.733883,25.48583);
        LatLng sofia= new LatLng(42.6977082,23.3218675);
        LatLng plovdiv = new LatLng(42.1354079,24.7452904);
        LatLng varna = new LatLng(43.2140504,27.9147333);
          mMap.addMarker(new MarkerOptions()
                .position(plovdiv)
                .title("Marker Bulgaria")
                .draggable(true)

        );
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                       plovdiv,
                        16f
                )
        );

         mMap.setTrafficEnabled(true);
         mMap.setBuildingsEnabled(true);


         mMap.getUiSettings()
                 .setCompassEnabled(true);
         mMap.getUiSettings()
                .setZoomControlsEnabled(true);
         mMap.getUiSettings()
                 .setRotateGesturesEnabled(true);


        LatLng lastLatLng = new  LatLng(1.1,1.1);

        mMap.addMarker(
                new MarkerOptions()
                        .position(lastLatLng).draggable(true)
        );



    }
}
