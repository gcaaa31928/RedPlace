package com.gca.red.redplace.objects;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by redhuang on 2017/2/4.
 */

public class Profile {
    private String name;
    private String photoUrl;
    private String email;
    private String accessToken;

    public Profile() {

    }

    public void importGoogleAccount(GoogleSignInAccount acc) {
        name = acc.getDisplayName();
        try {
            photoUrl = acc.getPhotoUrl().toString();
        }catch (Exception e) {
            photoUrl = "";
            Logger.e("dont get url", e);
        }
        email = acc.getEmail();
    }

    public void importFacebookData(JSONObject data) throws JSONException {
        name = data.optString("last_name") + data.optString("first_name");
        photoUrl = data.getJSONObject("picture").getJSONObject("data").optString("url");
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


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
