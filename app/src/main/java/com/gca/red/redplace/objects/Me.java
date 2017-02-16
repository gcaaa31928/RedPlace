package com.gca.red.redplace.objects;

import android.util.Log;

import com.gca.red.redplace.utils.OkHttpUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by redhuang on 2017/2/3.
 */
public class Me {
    private static Me ourInstance = new Me();
    private static final String BACKEND_URL = "http://localhost:3000";
    private static final String USERS_URL = BACKEND_URL + "/users";
    private static final String LOGIN_URL = USERS_URL + "/login";
    private static final String TAG = "UserHelper";

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

    public void setFbProfile(JSONObject data) throws JSONException{
        this.profile = new Profile();
        this.profile.importFacebookData(data);
    }




    private void login() {
        OkHttpUtil.ResultCallback<JsonObject> loadUsersCallback = new OkHttpUtil.ResultCallback<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        List<OkHttpUtil.Param> params = Arrays.asList(
                new OkHttpUtil.Param("name", profile.getName()),
                new OkHttpUtil.Param("photoUrl", profile.getPhotoUrl()),
                new OkHttpUtil.Param("email", profile.getEmail()));
        OkHttpUtil.post(USERS_URL, loadUsersCallback, params);
    }

}
