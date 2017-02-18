package com.gca.red.redplace.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.login.widget.LoginButton;
import com.gca.red.redplace.R;
import com.gca.red.redplace.helpers.FacebookHelper;
import com.gca.red.redplace.helpers.GoogleHelper;
import com.gca.red.redplace.objects.Me;
import com.gca.red.redplace.objects.Profile;

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
        Me.getInstance().setContext(getApplicationContext());
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
            protected void onFetchProfileSuccess(Profile profile) {
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


    private void enterMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}
