package com.github.lykmapipo.common.sample;

import android.app.Application;

import com.github.lykmapipo.common.Common;

public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Common.of(this);
    }
}
