package com.gca.red.redplace.objects;

import com.facebook.AccessToken;

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

    private String name;
    private AccessToken accessToken;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}
