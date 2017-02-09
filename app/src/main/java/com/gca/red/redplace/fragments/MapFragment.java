package com.gca.red.redplace.fragments;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gca.red.redplace.R;
import com.gca.red.redplace.helpers.GoogleMapHelper;
import com.gca.red.redplace.helpers.GoogleMapHelperListener;
import com.google.android.gms.maps.SupportMapFragment;

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
        View myLocationButton = getView().findViewById(R.id.my_location_button);
        myLocationButton.setOnClickListener(googleMapHelper);
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
