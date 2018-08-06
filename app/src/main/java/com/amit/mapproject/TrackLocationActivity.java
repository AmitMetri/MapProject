package com.amit.mapproject;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    FirebaseDatabase database;
    DatabaseReference userId;
    DatabaseReference longitude;
    DatabaseReference latitude;
    LatLng currentLocation;
    public static  double lat, lon, user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_location);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Bangalore and move the camera
        currentLocation = new LatLng(12.97, 77.59);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Bangalore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20.0f));


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);


        // Get database Instance
        database = FirebaseDatabase.getInstance();
        userId =  database.getReference("userId");
        longitude =  database.getReference("longitude");
        latitude =  database.getReference("latitude");

        /*Firebase real time db value change listener*/
        database.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("datasnap", String.valueOf(dataSnapshot.getChildrenCount()));

                /*Iterate through children*/
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    if(dsp.getKey().equals("latitude")){
                        lat=Double.parseDouble(dsp.getValue().toString());
                    }
                    if(dsp.getKey().equals("longitude")){
                        lon=Double.parseDouble(dsp.getValue().toString());
                    }
                }

                currentLocation = new LatLng(lat, lon);
                //mMap.addMarker(new MarkerOptions().position(currentLocation).title("Aarti"+ Build.MODEL));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}
