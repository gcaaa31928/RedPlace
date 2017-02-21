package com.gca.red.redplace.utils;

import com.gca.red.redplace.objects.Friend;
import com.gca.red.redplace.objects.Me;
import com.gca.red.redplace.objects.Profile;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by red2 on 2017/2/21.
 */

public class MapSocketIOUtil {

    private Socket socket;
    private static MapSocketIOUtil ourInstance = new MapSocketIOUtil();

    public static MapSocketIOUtil getInstance() {
        return ourInstance;
    }

    private MapSocketIOUtil() {
    }

    public void instanceSocket(String url) {
        try {
            this.socket = IO.socket(url);
        } catch (Exception e) {
            Logger.e("socket url issue", e);
        }
    }

    public void subscribeAll() {
        if (socket != null) {
            try {
                for (Friend friend : Me.getInstance().getFriends()) {
                    JSONObject obj = new JSONObject();
                    obj.put("accessToken", Me.getInstance().getProfile().getAccessToken());
                    obj.put("friendId", friend.getUuid());
                    socket.emit("subscribe", obj);
                }
            } catch (Exception e) {
                Logger.e("socket url issue", e);
            }
        }

    }

    public void sendMyLocation() {
        try {
            JSONObject obj = new JSONObject();
            Profile profile = Me.getInstance().getProfile();
            obj.put("latitude", profile.getLatitude());
            obj.put("longitude", profile.getLongitude());
            obj.put("orientation", profile.getOrientation());
            obj.put("friendId", profile.getUuid());
            socket.emit("send location", obj);
        } catch (Exception e) {
            Logger.e("socket url issue", e);

        }
    }

    public void connect() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("connect socket server");
            }
        }).on("receive location", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject) args[0];
                try {
                    Friend friend = Me.getInstance().getFriendHashMap().get(obj.getString("friendId"));
                    friend.setLatitude(obj.getLong("latitude"));
                    friend.setLongitude(obj.getLong("longitude"));
                    friend.setOrientation(obj.getLong("orientation"));

                } catch (Exception e) {
                    Logger.e("receive location", e);
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("disconnect socket server");
            }
        });
        socket.connect();
    }
}
