package com.github.lykmapipo.common.sample;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.github.lykmapipo.common.Common;
import com.github.lykmapipo.common.provider.Provider;

public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Common.of(new Provider() {
            @NonNull
            @Override
            public Context getApplicationContext() {
                return SampleApp.this;
            }

            @NonNull
            @Override
            public Boolean isDebug() {
                return BuildConfig.DEBUG;
            }
        });

    }
}
