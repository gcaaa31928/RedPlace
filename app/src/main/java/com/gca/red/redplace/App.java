package com.gca.red.redplace;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.orhanobut.hawk.Hawk;

/**
 * Created by redhuang on 2017/2/3.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());
        Hawk.init(getApplicationContext()).build();
    }

}
