package com.gca.red.redplace.helpers;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gca.red.redplace.objects.Me;

import org.json.JSONObject;

/**
 * Created by redhuang on 2017/2/4.
 */

public class FacebookHelper {

    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    protected void onAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
    }

    protected void onFetchProfileSuccess() {
    }

    public void onCreate() {
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                fetchUserInfo();
                onAccessTokenChanged(oldAccessToken, currentAccessToken);
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    Me.getInstance().setFbProfile(Profile.getCurrentProfile());
                    onFetchProfileSuccess();
                }
            }
        };
        callbackManager = CallbackManager.Factory.create();
        fetchUserInfo();
    }

    public void loginButtonRegisterCallback(LoginButton loginButton) {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    public void onDestroy() {
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private boolean isFacebookRequestCode(int requestCode) {
        return FacebookSdk.isFacebookRequestCode(requestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isFacebookRequestCode(requestCode))
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void fetchUserInfo() {
        Profile.fetchProfileForCurrentAccessToken();
        if (Profile.getCurrentProfile() != null) {
            Me.getInstance().setFbProfile(Profile.getCurrentProfile());
            onFetchProfileSuccess();
        }
    }

}
