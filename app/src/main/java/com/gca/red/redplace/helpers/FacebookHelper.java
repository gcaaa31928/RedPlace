package com.gca.red.redplace.helpers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
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
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                fetchUserInfo();
//                onAccessTokenChanged(oldAccessToken, currentAccessToken);
//            }
//        };
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//                if (currentProfile != null) {
//                    Me.getInstance().setFbProfile(Profile.getCurrentProfile());
//                    onFetchProfileSuccess();
//                }
//            }
//        };
        callbackManager = CallbackManager.Factory.create();
        fetchUserInfo(null);
    }

    public void loginButtonRegisterCallback(LoginButton loginButton) {
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fetchUserInfo(loginResult);
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

    private void fetchUserInfo(LoginResult loginResult) {
        AccessToken accessToken;
        if (loginResult != null)
            accessToken = loginResult.getAccessToken();
        else
            accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                        } else {
                            try {
                                Me.getInstance().setFbProfile(me);
                                userLogin();
                            } catch (JSONException e) {
                                Logger.e(e, "JSON exception");
                            }
                            Logger.d(response.getRawResponse());
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void userLogin() {
        Me.ResultCallback<JsonObject> loginResultCallback = new Me.ResultCallback<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {
                onFetchProfileSuccess();
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        Me.getInstance().login(loginResultCallback);
    }

}
