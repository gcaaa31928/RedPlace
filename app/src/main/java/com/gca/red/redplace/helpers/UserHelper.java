package com.gca.red.redplace.helpers;

import android.util.Log;

import com.gca.red.redplace.utils.OkHttpUtil;
import com.google.gson.JsonObject;


/**
 * Created by red on 2017/2/14.
 */

public class UserHelper {
    private static final String BACKEND_URL = "http://localhost:3000";
    private static final String USERS_URL = BACKEND_URL + "/users";
    private static final String TAG = "UserHelper";

    private void loadUser() {
        OkHttpUtil.ResultCallback<JsonObject> loadUsersCallback = new OkHttpUtil.ResultCallback<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        OkHttpUtil.get(USERS_URL, loadUsersCallback);
    }

}
