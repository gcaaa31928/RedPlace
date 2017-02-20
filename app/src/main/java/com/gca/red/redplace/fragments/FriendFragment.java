package com.gca.red.redplace.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gca.red.redplace.R;
import com.gca.red.redplace.adapters.FriendAdapter;
import com.gca.red.redplace.objects.Friend;
import com.gca.red.redplace.objects.Me;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.Result;
import com.orhanobut.logger.Logger;
import com.victor.loading.rotate.RotateLoading;

import net.glxn.qrgen.android.QRCode;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

@RuntimePermissions
public class FriendFragment extends Fragment {

    private RotateLoading loading;
    private RecyclerView.Adapter friendAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView friendView;
    private FloatingActionButton fab;
    private AlertDialog dialog;
    private AlertDialog qrcodeDialog;
    private AlertDialog qrcodeReaderDialog;
    private ZXingScannerView scannerView;
    private ZXingScannerView.ResultHandler resultHandler;
    private Me.ResultCallback<JsonObject> getFriendTokenCallback;
    private Me.ResultCallback<JsonObject> addFriendCallback;

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

    @NeedsPermission({Manifest.permission.CAMERA})
    public void runQRCodeReader() {
        qrcodeReaderDialog.show();
        scannerView.startCamera();
    }




    private void initialQRCodeReaderDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("QRCode Reader");
        scannerView = new ZXingScannerView(getContext());
        builder.setView(scannerView);
        qrcodeReaderDialog = builder.create();
        resultHandler = new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                Me.getInstance().addFriend(addFriendCallback, result.getText());
                scannerView.stopCamera();
                qrcodeReaderDialog.hide();
            }
        };
        scannerView.setResultHandler(resultHandler);
        qrcodeReaderDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                scannerView.stopCamera();
            }
        });
        addFriendCallback = new Me.ResultCallback<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {
                getFriends();
            }

            @Override
            public void onFailure(Exception e) {
                Logger.e("add friend error", e);
            }
        };
    }

    private void initialAddFriendDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        final View dialogView = inflater.inflate(R.layout.add_friend_dialog, (ViewGroup) getView().findViewById(R.id.add_friend_dialog));
        View showQRCodeButton = dialogView.findViewById(R.id.show_qrcode_button);
        final View qrcodeCameraButton = dialogView.findViewById(R.id.qrcode_camra_button);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("選擇加入好友的方式");
        builder.setView(dialogView);
        dialog = builder.create();

        showQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Me.getInstance().getFriendToken(getFriendTokenCallback);
                dialog.hide();
                qrcodeDialog.show();
            }
        });

        qrcodeCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendFragmentPermissionsDispatcher.runQRCodeReaderWithCheck(FriendFragment.this);
            }
        });
    }

    private void initialQRCodeDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        final View dialogView = inflater.inflate(R.layout.qrcode_dialog, (ViewGroup) getView().findViewById(R.id.qrcodeDialog));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("QRCode");
        builder.setView(dialogView);
        qrcodeDialog = builder.create();
        final ImageView qrcodeImageView = (ImageView) dialogView.findViewById(R.id.qrcodeImageView);
        getFriendTokenCallback = new Me.ResultCallback<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {
                String token = response.get("friendToken").getAsString();
                Bitmap bitmap = QRCode.from(token).withColor(Color.BLACK
                        , ContextCompat.getColor(getContext(), R.color.colorBackground)).bitmap();
                qrcodeImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

    }

    private void getFriends() {
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initialAddFriendDialog(savedInstanceState);
        initialQRCodeDialog(savedInstanceState);
        initialQRCodeReaderDialog(savedInstanceState);
        fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        friendView = (RecyclerView) getView().findViewById(R.id.friend_recycler_view);
        friendView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        friendView.setLayoutManager(layoutManager);
        friendAdapter = new FriendAdapter(getContext());
        friendView.setAdapter(friendAdapter);
        loading = (RotateLoading) getView().findViewById(R.id.rotateloading);
        getFriends();
        super.onActivityCreated(savedInstanceState);
    }
}
