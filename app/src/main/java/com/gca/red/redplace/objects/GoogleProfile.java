package com.gca.red.redplace.objects;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by redhuang on 2017/2/4.
 */

public class GoogleProfile {
    private String name;
    private Uri photoUrl;
    private String email;

    public GoogleProfile() {
    }

    public void importAccount(GoogleSignInAccount acc) {
        name = acc.getDisplayName();
        photoUrl = acc.getPhotoUrl();
        email = acc.getEmail();
    }

    public String getName() {
        return name;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public String getEmail() {
        return email;
    }
}
