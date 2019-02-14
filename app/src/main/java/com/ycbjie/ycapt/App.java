package com.ycbjie.ycapt;

import android.app.Application;

import com.ycbjie.api.router.ARouter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
    }
}
