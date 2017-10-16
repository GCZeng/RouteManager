package org.zgc.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Nick on 2017/10/2
 */
public class APP extends Application {
    public static Context sContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this.getApplicationContext();
    }
}
