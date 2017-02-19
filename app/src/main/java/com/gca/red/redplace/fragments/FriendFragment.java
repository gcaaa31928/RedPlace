package com.gca.red.redplace.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gca.red.redplace.R;
import com.gca.red.redplace.adapters.FriendAdapter;
import com.gca.red.redplace.objects.Me;
import com.google.gson.JsonArray;
import com.victor.loading.rotate.RotateLoading;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {

    private RotateLoading loading;
    private RecyclerView.Adapter friendAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView friendView;

    public FriendFragment() {
        // Required empty public constructor
    }

    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        friendView = (RecyclerView)getView().findViewById(R.id.friend_recycler_view);
        friendView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        friendView.setLayoutManager(layoutManager);
        friendAdapter = new FriendAdapter();
        friendView.setAdapter(friendAdapter);
        loading = (RotateLoading) getView().findViewById(R.id.rotateloading);
        loading.start();
        Me.ResultCallback<JsonArray> callback = new Me.ResultCallback<JsonArray>() {
            @Override
            public void onSuccess(JsonArray response) {
                loading.stop();
            }
            @Override
            public void onFailure(Exception e) {
            }
        };
        Me.getInstance().getFriends(callback);
        super.onActivityCreated(savedInstanceState);
    }
}
