package com.github.lykmapipo.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.github.lykmapipo.common.provider.Provider;

/**
 * Helper utilities for day to day android development.
 *
 * @author lally elias<lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class Common {
    // refs
    private static Provider appProvider;

    // no instances allowed
    private Common() {
        throw new AssertionError("No instances.");
    }

    /**
     * Initialize {@link Common} internals
     *
     * @param provider application provider
     * @since 0.1.0
     */
    public static synchronized void of(@NonNull Provider provider) {
        if (appProvider == null) {
            appProvider = provider;
        }
    }

    /**
     * Retrieve application {@link Context}
     *
     * @return current application context
     */
    @NonNull
    public static synchronized Context getApplicationContext() {
        return appProvider.getApplicationContext();
    }


    /**
     * Retrieve application build state from build config
     *
     * @return current application build state
     */
    @NonNull
    public static synchronized Boolean isDebug() {
        return appProvider.isDebug();
    }

    /**
     * Clean up and reset {@link Common} internals
     */
    public static synchronized void dispose() {
        appProvider = null;
    }

    /**
     * Network Utilities
     */
    public static class Network {
        /**
         * Check if there is internet or data connection on the device
         *
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean isConnected() {
            Context context = appProvider.getApplicationContext();
            ConnectivityManager connectivity =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
    }
}
