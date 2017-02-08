package com.gca.red.redplace.fragments;


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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gca.red.redplace.R;
import com.gca.red.redplace.helpers.GoogleMapHelper;
import com.gca.red.redplace.helpers.GoogleMapHelperListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * A simple {@link Fragment} subclass.
 */
@RuntimePermissions
public class MapFragment extends Fragment implements GoogleMapHelperListener {

    private GoogleMapHelper googleMapHelper;

    public static MapFragment newInstance(String title) {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        googleMapHelper = new GoogleMapHelper(getChildFragmentManager(), getActivity(), getContext(), mapFragment, this);
        return view;
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void requestPermission() {
        Log.d("MapFragment", "request permission");
        googleMapHelper.getMyLocationAfterPermissionCheck();
    }

    @Override
    public void onGettingMyLocation() {
        MapFragmentPermissionsDispatcher.requestPermissionWithCheck(this);
    }


    public void onResume() {
        super.onResume();
        googleMapHelper.registerListener();
    }

    public void onPause() {
        super.onPause();
        googleMapHelper.unregisterListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @Override
    public void onStart() {
        super.onStart();
//        googleMapHelper.onStart();
    }

    @Override
    public void onStop() {
//        googleMapHelper.onStop();
        super.onStop();
    }

}
