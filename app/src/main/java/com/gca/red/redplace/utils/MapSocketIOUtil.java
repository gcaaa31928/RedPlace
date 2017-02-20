package com.gca.red.redplace.utils;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by red2 on 2017/2/21.
 */

public class MapSocketIOUtil {

    private Socket socket;
    public MapSocketIOUtil(String url) throws URISyntaxException {
        this.socket = IO.socket(url);
    }

    public void connect() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        }).on("receive location", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });
        socket.connect();
    }
}
