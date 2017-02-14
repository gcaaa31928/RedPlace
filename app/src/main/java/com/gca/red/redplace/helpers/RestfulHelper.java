package com.gca.red.redplace.helpers;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by red on 2017/2/14.
 */

public class RestfulHelper {
    OkHttpClient client = new OkHttpClient();
    private static final String BACKEND_URL = "http://localhost:3000";
    private static final String USERS_URL = BACKEND_URL + "/users";


}
