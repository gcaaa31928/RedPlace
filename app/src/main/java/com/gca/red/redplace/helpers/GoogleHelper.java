package com.gca.red.redplace.helpers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.gca.red.redplace.objects.GoogleProfile;
import com.gca.red.redplace.objects.Me;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by redhuang on 2017/2/4.
 */
public class GoogleHelper implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    public static final int GOOGLE_SIGN_IN = 9001;


    protected void onSignInSuccess(Intent signInIntent) {
    }

    protected void onFetchProfileSuccess(GoogleProfile profile) {
    }

    public void onCreate(AppCompatActivity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(activity.getApplicationContext())
                .enableAutoManage(activity/* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void loginButtonRegisterCallback(View loginButton) {
        loginButton.setOnClickListener(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInGoogleResult(result);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        onSignInSuccess(signInIntent);
    }

    private void handleSignInGoogleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Me.getInstance().setGoogleProfile(acct);
            onFetchProfileSuccess(Me.getInstance().getGoogleProfile());
        } else {
            // Signed out, show unauthenticated UI.
        }
    }


    @Override
    public void onClick(View v) {
        signIn();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
