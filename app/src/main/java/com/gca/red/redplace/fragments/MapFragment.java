package com.gca.red.redplace.fragments;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gca.red.redplace.R;
import com.gca.red.redplace.adapters.MapFriendAdapter;
import com.gca.red.redplace.helpers.GoogleMapHelper;
import com.gca.red.redplace.helpers.GoogleMapHelperListener;
import com.gca.red.redplace.utils.MapSocketIOUtil;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.orhanobut.logger.Logger;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * A simple {@link Fragment} subclass.
 */
@RuntimePermissions
public class MapFragment extends Fragment implements GoogleMapHelperListener {

    private GoogleMapHelper googleMapHelper;
    private MapView mapView;

    public static MapFragment newInstance(String title) {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        Logger.d(title);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.maps, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MapSocketIOUtil.getInstance().instanceSocket(getContext().getString(R.string.server_address));
        MapSocketIOUtil.getInstance().connect();
        mapView = (MapView) getView().findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        googleMapHelper = new GoogleMapHelper(getChildFragmentManager(), getActivity(), getContext(), mapView, this);
        View myLocationButton = getView().findViewById(R.id.my_location_button);
        myLocationButton.setOnClickListener(googleMapHelper);

        ListView mapFriendListView = (ListView) getView().findViewById(R.id.mapFriendsList);
        mapFriendListView.setAdapter(new MapFriendAdapter(getContext()));
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
    public void onDestroy() {
        super.onDestroy();
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
