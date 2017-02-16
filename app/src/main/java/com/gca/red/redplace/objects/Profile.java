package com.gca.red.redplace.objects;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by redhuang on 2017/2/4.
 */

public class Profile {
    private String name;
    private String photoUrl;
    private String email;

    public Profile() {

    }

    public void importGoogleAccount(GoogleSignInAccount acc) {
        name = acc.getDisplayName();
        photoUrl = acc.getPhotoUrl().toString();
        email = acc.getEmail();
    }

    public void importFacebookData(JSONObject data) throws JSONException {
        name = data.optString("last_name") + data.optString("first_name");
        photoUrl = data.getJSONObject("picture").optString("url");
        email = data.optString("email");
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getEmail() {
        return email;
    }
}
