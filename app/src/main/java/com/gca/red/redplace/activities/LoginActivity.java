package com.gca.red.redplace.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.gca.red.redplace.R;
import com.gca.red.redplace.helpers.FacebookHelper;
import com.gca.red.redplace.helpers.GoogleHelper;
import com.gca.red.redplace.objects.GoogleProfile;
import com.gca.red.redplace.objects.Me;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private FacebookHelper facebookHelper;
    private GoogleHelper googleHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        facebookHelper = new FacebookHelper() {
            @Override
            protected void onFetchProfileSuccess() {
                enterMainActivity();
            }
        };
        facebookHelper.onCreate();
        googleHelper = new GoogleHelper() {
            @Override
            protected void onSignInSuccess(Intent signInIntent) {
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
            }

            @Override
            protected void onFetchProfileSuccess(GoogleProfile profile) {
                enterMainActivity();
            }
        };
        googleHelper.onCreate(this);

        registerGoogleSignInButton();
        registerFacebookSignInButton();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookHelper.onActivityResult(requestCode, resultCode, data);
        googleHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void registerGoogleSignInButton() {
        View loginButton = findViewById(R.id.sign_in_button);
        googleHelper.loginButtonRegisterCallback(loginButton);
    }

    private void registerFacebookSignInButton() {
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        facebookHelper.loginButtonRegisterCallback(loginButton);
    }


//    private void handleFacebook() {
//        callbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//                if (currentProfile != null) {
//                    Me.getInstance().setFbProfile(currentProfile);
//                    enterMainActivity();
//                }
//            }
//        };
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(
//                    AccessToken oldAccessToken,
//                    AccessToken currentAccessToken) {
//                // On AccessToken changes fetch the new profile which fires the event on
//                // the ProfileTracker if the profile is different
//                Profile.fetchProfileForCurrentAccessToken();
//            }
//        };
//        Profile.fetchProfileForCurrentAccessToken();
//        if (Profile.getCurrentProfile() != null) {
//            Me.getInstance().setFbProfile(Profile.getCurrentProfile());
//            enterMainActivity();
//        }
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                AccessToken accessToken = loginResult.getAccessToken();
//                Log.d("LoginActivity", "success");
//                GraphRequest request = GraphRequest.newMeRequest(
//                        accessToken,
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//
//                                Log.d("LoginActivity", object.toString());
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,link");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//            }
//        });
//    }

    private void enterMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}
