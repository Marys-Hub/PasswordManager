package com.example.passwordmanager.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.passwordmanager.R;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class LocationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private TextView locationTextView;
    private GoogleMap googleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private double lastKnownLatitude = 0.0;
    private double lastKnownLongitude = 0.0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Check Google Play Services
        if (isGooglePlayServicesAvailable()) {
            // Google Play Services is available, continue with location updates and map initialization

            // Initialize the TextView
            locationTextView = findViewById(R.id.locationTextView);

            // Request location permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Permission is already granted, start location updates
                startLocationUpdates();
            }

            // Initialize the map
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                // An error occurred, but it can be resolved
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            } else {
                // The device is not supported
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity or take appropriate action
            }
            return false;
        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Update the TextView with the latest location information
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Store the last known location
            lastKnownLatitude = latitude;
            lastKnownLongitude = longitude;

            String locationInfo = "Last Location: " + "\nLatitude: " + latitude + "\nLongitude: " + longitude;
            locationTextView.setText(locationInfo);
        }
    }


    private void startLocationUpdates() {
        // Check for location permission before starting location updates
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, start location updates
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000) // 10 seconds
                    .setFastestInterval(5000); // 5 seconds

            LocationServices.getFusedLocationProviderClient(this)
                    .requestLocationUpdates(locationRequest, this, null);
        } else {
            // Permission is not granted, handle accordingly (e.g., show a message to the user)
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                startLocationUpdates();
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Use the last known location
        LatLng lastKnownLocation = new LatLng(lastKnownLatitude, lastKnownLongitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 15));

        // Customize the map or add markers if needed
    }

}
