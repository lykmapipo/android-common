package com.github.lykmapipo.common.sample;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.github.lykmapipo.common.Common;

public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Common.of(new Common.Provider() {
            @NonNull
            @Override
            public Context getApplicationContext() {
                return SampleApp.this;
            }
        });

    }
}
