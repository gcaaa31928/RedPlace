package com.gca.red.redplace.objects;

import com.gca.red.redplace.utils.OkHttpUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by redhuang on 2017/2/3.
 */
public class Me {
    private static Me ourInstance = new Me();
    private static final String BACKEND_URL = "http://140.124.183.101:3000";
    private static final String USERS_URL = BACKEND_URL + "/users";
    private static final String LOGIN_URL = USERS_URL + "/login";

    public static Me getInstance() {
        return ourInstance;
    }

    private Me() {
    }

    private Profile profile = null;

    public void setGoogleProfile(GoogleSignInAccount acc) {
        this.profile = new Profile();
        this.profile.importGoogleAccount(acc);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setFbProfile(JSONObject data) throws JSONException {
        this.profile = new Profile();
        this.profile.importFacebookData(data);
    }


    public void login(final LoginResultCallback callback) {
        OkHttpUtil.ResultCallback<JsonObject> loginCallback = new OkHttpUtil.ResultCallback<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {
                String accessToken = response.get("accessToken").getAsString();
                Me.getInstance().profile.setAccessToken(accessToken);
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Exception e) {
                Logger.e("cannot connect to server");
            }
        };
        List<OkHttpUtil.Param> params = Arrays.asList(
                new OkHttpUtil.Param("name", profile.getName()),
                new OkHttpUtil.Param("photoUrl", profile.getPhotoUrl()),
                new OkHttpUtil.Param("email", profile.getEmail()));
        Logger.d(LOGIN_URL);
        OkHttpUtil.post(LOGIN_URL, loginCallback, params);
    }

    public static abstract class LoginResultCallback {

        public abstract void onSuccess(JsonObject response);

        public abstract void onFailure(Exception e);

    }

}
