package com.gca.red.redplace.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gca.red.redplace.R;
import com.gca.red.redplace.fragments.ErrorDialogFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;

/**
 * Created by red on 2017/2/8.
 */

public class GoogleMapHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        SensorEventListener, OnMapReadyCallback, LocationListener, View.OnClickListener {
    private SensorManager sensorManager;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private FragmentManager fragmentManager;
    private Sensor rotationSensor;
    private float[] rotationMatrix = new float[16];
    private Activity activity;
    private Context context;
    private MapView mapView;
    private GoogleMap map;
    private float currentDeclination;
    private double currentBearing;
    private LatLng currentLatLng = null;
    private GoogleMapHelperListener listener;
    private Marker myLocationMarker;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private long UPDATE_INTERVAL = 60000;
    private long FASTEST_INTERVAL = 5000;
    private final static int CAMERA_ZOOM = 17;
    private Handler handler = new Handler();
    Runnable updateLocationRunnable = new Runnable() {
        @Override
        public void run() {
            updateMyLocationMarker();
            handler.postDelayed(this, 2000);
        }
    };

    public GoogleMapHelper(FragmentManager fragmentManager, Activity activity, Context context, MapView mapView, GoogleMapHelperListener listener) {
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        this.context = context;
        this.mapView = mapView;
        this.listener = listener;
        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        registerListener();
        if (rotationSensor == null) {
            Log.d("GoogleMapHelper", "Rotation Sensor was null!!");
            Toast.makeText(this.context, "Rotation Sensor was null!!", Toast.LENGTH_SHORT).show();
        }
        if (mapView != null) {
            mapView.getMapAsync(this);
            handler.postDelayed(updateLocationRunnable, 2000);
        } else
            Toast.makeText(this.context, "Error - Map View was null!!", Toast.LENGTH_SHORT).show();
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
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(fragmentManager, "Location Updates");
            }
            return false;
        }
    }

    private void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private void getMyLocationBeforePermissionCheck() {
        if (map != null) {
            listener.onGettingMyLocation();
        }
    }

    @SuppressWarnings("all")
    public void getMyLocationAfterPermissionCheck() {
        if (map != null) {
            map.setMyLocationEnabled(false);
            googleApiClient = new GoogleApiClient.Builder(this.activity)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            connectClient();
        }
    }


    public void registerListener() {
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    public void onStart() {
        connectClient();
    }

    public void onStop() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    private void updateMyLocationMarker() {
        if (currentLatLng == null)
            return;
        if (myLocationMarker == null)
            myLocationMarker = map.addMarker(new MarkerOptions()
                    .flat(true)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.ic_navigation))
                    .anchor(0.5f, 0.5f)
                    .position(currentLatLng));
        myLocationMarker.setPosition(currentLatLng);
        myLocationMarker.setRotation((float) currentBearing);
    }

    private void updateCamera() {
        if (map == null || currentLatLng == null)
            return;

        CameraPosition pos = CameraPosition.builder()
                .target(currentLatLng)
                .zoom(CAMERA_ZOOM)
                .bearing((float) currentBearing).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(pos));
    }

    @SuppressWarnings("all")
    protected void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                locationRequest, this);
    }

    @Override
    @SuppressWarnings("all")
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            Toast.makeText(context, "GPS location was found!", Toast.LENGTH_SHORT).show();
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            Toast.makeText(context, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(context, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(context, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(activity,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context,
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(
                    rotationMatrix, event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientation);
            currentBearing = Math.toDegrees(orientation[0]) + currentDeclination;
            updateMyLocationMarker();

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        mapView.onResume();
        Logger.d("google map done");
        if (map != null) {
            getMyLocationBeforePermissionCheck();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        GeomagneticField field = new GeomagneticField(
                (float) location.getLatitude(),
                (float) location.getLongitude(),
                (float) location.getAltitude(),
                System.currentTimeMillis()
        );
        currentDeclination = field.getDeclination();
        Toast.makeText(context, Float.toString(currentDeclination), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        updateCamera();
    }
}

