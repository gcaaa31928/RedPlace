package com.gca.red.redplace.objects;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by redhuang on 2017/2/3.
 */
public class Me {
    private static Me ourInstance = new Me();

    public static Me getInstance() {
        return ourInstance;
    }

    private Me() {
    }

    private Profile fb_profile;
    private GoogleProfile googleProfile;


    public GoogleProfile getGoogleProfile() {
        return googleProfile;
    }

    public void setGoogleProfile(GoogleSignInAccount acc) {
        GoogleProfile profile = new GoogleProfile();
        profile.importAccount(acc);
        Log.d("Me", profile.getName());
        this.googleProfile = profile;
    }

    public Profile getFbProfile() {
        return fb_profile;
    }

    public void setFbProfile(Profile fb_profile) {
        Log.d("Me", fb_profile.getName());
        this.fb_profile = fb_profile;
    }

}
