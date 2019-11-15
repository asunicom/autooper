package com.madq.autooper;

import android.app.Application;
import android.content.Context;

/**
 * @author madeqiang
 * @version 1.0
 * @date 2019/11/15 15:56
 */
public class App extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
