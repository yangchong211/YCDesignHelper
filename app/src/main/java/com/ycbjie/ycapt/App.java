package com.ycbjie.ycapt;

import android.app.Application;

import com.ycbjie.api.router.EasyRouter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EasyRouter.init(this);
    }
}
