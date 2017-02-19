package com.gca.red.redplace.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Red on 2016/10/20.
 */

public class OkHttpUtil {
    private static OkHttpUtil mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpUtil() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.newBuilder().writeTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.newBuilder().readTimeout(10, TimeUnit.SECONDS);
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private synchronized static OkHttpUtil getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtil();
        }
        return mInstance;
    }


    private void getRequest(String url, final ResultCallback callback, List<Param> headers) {
        final Request.Builder builder = new Request.Builder();
        for (Param header : headers) {
            builder.addHeader(header.key, header.value);
        }
        final Request request = builder.url(url).build();
        deliveryResult(callback, request);
    }

    private void postRequest(String url, final ResultCallback callback, List<Param> params, List<Param> headers) {
        final Request request = buildPostRequest(url, params, headers);
        deliveryResult(callback, request);
    }

    private Request buildPostRequest(String url, List<Param> params, List<Param> headers) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        Request.Builder requestBuilder = new Request.Builder();
        for (Param header : headers) {
            requestBuilder.addHeader(header.key, header.value);
        }
        return requestBuilder.url(url).post(requestBody).build();
    }

    private void deliveryResult(final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callback, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String str = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessCallback(callback, str);
                    } else if (callback.mType == JsonObject.class) {
                        JsonParser parser = new JsonParser();
                        JsonObject object = parser.parse(str).getAsJsonObject();
                        sendSuccessCallback(callback, object);
                    } else if (callback.mType == JsonArray.class) {
                        JsonParser parser = new JsonParser();
                        JsonArray array = parser.parse(str).getAsJsonArray();
                        sendSuccessCallback(callback, array);
                    } else {
                        Object object = JsonUtil.deserialize(str, callback.mType);
                        sendSuccessCallback(callback, object);
                    }
                } catch (final Exception e) {
                    sendFailCallback(callback, e);
                }
            }
        });
    }

    private void sendSuccessCallback(final ResultCallback callback, final Object object) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(object);
                }
            }
        });
    }

    private void sendFailCallback(final ResultCallback callback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    public static void get(String url, ResultCallback callback, List<Param> headers) {
        getInstance().getRequest(url, callback, headers);
    }

    public static void get(String url, ResultCallback callback) {
        List<Param> headers = new ArrayList<>();
        getInstance().getRequest(url, callback, headers);
    }

    public static void post(String url, final ResultCallback callback, List<Param> params, List<Param> headers) {
        getInstance().postRequest(url, callback, params, headers);
    }

    public static void post(String url, final ResultCallback callback, List<Param> params) {
        List<Param> headers = new ArrayList<>();
        getInstance().postRequest(url, callback, params, headers);
    }

    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperClassTypeParameter(getClass());
        }

        static Type getSuperClassTypeParameter(Class<?> subClass) {
            Type superClass = subClass.getGenericSuperclass();
            if (superClass instanceof Class) {
                throw new RuntimeException("Missing type paramtere.");
            }
            ParameterizedType parameterized = (ParameterizedType) superClass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onSuccess(T response);

        public abstract void onFailure(Exception e);
    }

    public static class Param {
        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
