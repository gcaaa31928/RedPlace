package com.gca.red.redplace.helpers;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.gca.red.redplace.fragments.MapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by red on 2017/2/8.
 */

public class GoogleMapHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        SensorEventListener, OnMapReadyCallback {
    private SensorManager sensorManager;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private FragmentManager fragmentManager;
    private Sensor rotationSensor;
    private float[] rotationMatrix = new float[16];
    private Activity activity;
    private Context context;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    protected void getMyLocationNotification() {
    }

    private GoogleMapHelper(SensorManager sensorManager, FragmentManager fragmentManager, LocationRequest locationRequest, Activity activity, Context context, SupportMapFragment mapFragment) {
        this.sensorManager = sensorManager;
        this.fragmentManager = fragmentManager;
        this.locationRequest = locationRequest;
        this.activity = activity;
        this.context = context;
        this.mapFragment = mapFragment;
        googleApiClient = new GoogleApiClient.Builder(this.activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        registerListener();
        if (rotationSensor == null) {
            Log.d("GoogleMapHelper", "Rotation Sensor was null!!");
            Toast.makeText(this.context, "Rotation Sensor was null!!", Toast.LENGTH_SHORT).show();
        }
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
        else
            Toast.makeText(this.context, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
    }


    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = googleApiAvailability.getErrorDialog(activity, resultCode,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                MapFragment.ErrorDialogFragment errorFragment = new MapFragment.ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(fragmentManager, "Location Updates");
            }
            return false;
        }
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private void getMyLocation() {
        getMyLocationNotification();

    }

    public void registerListener() {
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
        }
    }


}
