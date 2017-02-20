package com.gca.red.redplace.objects;

import android.content.Context;

import com.gca.red.redplace.R;
import com.gca.red.redplace.utils.JsonUtil;
import com.gca.red.redplace.utils.OkHttpUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by redhuang on 2017/2/3.
 */
public class Me {
    private static Me ourInstance = new Me();


    private Context context;
    private String backendUrl;
    private List<Friend> friends;

    public static Me getInstance() {
        return ourInstance;
    }

    private Me() {
        friends = new ArrayList<>();
    }

    private Profile profile = null;

    public void setContext(Context context) {
        this.context = context;
        this.backendUrl = this.context.getString(R.string.server_address);
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(JsonArray friendJson) {
        for (int i = 0; i < friendJson.size(); i++) {
            JsonObject jsonObject = friendJson.get(i).getAsJsonObject();
            Friend friend = JsonUtil.deserialize(jsonObject, Friend.class);
            friends.add(friend);
        }
    }

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


    public void login(final ResultCallback callback) {
        String loginUrl = backendUrl + "/users/login";
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
                callback.onFailure(e);
            }
        };
        List<OkHttpUtil.Param> params = Arrays.asList(
                new OkHttpUtil.Param("name", profile.getName()),
                new OkHttpUtil.Param("photoUrl", profile.getPhotoUrl()),
                new OkHttpUtil.Param("email", profile.getEmail()));
        Logger.d(loginUrl);
        OkHttpUtil.post(loginUrl, loginCallback, params);
    }

    public void getFriends(final ResultCallback callback) {
        String loginUrl = backendUrl + "/users/my/friends";
        OkHttpUtil.ResultCallback<JsonArray> getFriendsCallback = new OkHttpUtil.ResultCallback<JsonArray>() {
            @Override
            public void onSuccess(JsonArray response) {
                Logger.d(response);
                setFriends(response);
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Exception e) {
                Logger.e("cannot connect to server");
                callback.onFailure(e);
            }
        };
        List<OkHttpUtil.Param> headers = Arrays.asList(new OkHttpUtil.Param("accessToken", profile.getAccessToken()));

        OkHttpUtil.get(loginUrl, getFriendsCallback, headers);
    }

    public void getFriendToken(final ResultCallback callback) {
        String loginUrl = backendUrl + "/users/my/friend_token";
        OkHttpUtil.ResultCallback<JsonObject> getFriendsCallback = new OkHttpUtil.ResultCallback<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Exception e) {
                Logger.e("cannot connect to server");
                callback.onFailure(e);
            }
        };
        List<OkHttpUtil.Param> headers = Arrays.asList(new OkHttpUtil.Param("accessToken", profile.getAccessToken()));
        List<OkHttpUtil.Param> params = new ArrayList<>();
        OkHttpUtil.post(loginUrl, getFriendsCallback, params, headers);
    }


    public void addFriend(final ResultCallback callback, String friendToken) {
        String loginUrl = backendUrl + "/users/my/friends/add";
        OkHttpUtil.ResultCallback<JsonObject> addFriendCallback = new OkHttpUtil.ResultCallback<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Exception e) {
                Logger.e("cannot connect to server");
                callback.onFailure(e);
            }
        };
        List<OkHttpUtil.Param> headers = Arrays.asList(new OkHttpUtil.Param("accessToken", profile.getAccessToken()));
        List<OkHttpUtil.Param> params = Arrays.asList(new OkHttpUtil.Param("friendToken", friendToken));
        OkHttpUtil.post(loginUrl, addFriendCallback, params, headers);
    }


    public static abstract class ResultCallback<T> {
        public abstract void onSuccess(T response);

        public abstract void onFailure(Exception e);
    }

}
