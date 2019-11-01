package com.github.lykmapipo.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.github.lykmapipo.common.provider.Provider;

import java.net.HttpRetryException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

/**
 * Helper utilities for day to day android development.
 *
 * @author lally elias<lallyelias87@gmail.com>
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

        /**
         * Check if throwable in net related exception
         *
         * @param t thrown exception
         * @return true if so else false
         */
        @NonNull
        public static synchronized Boolean isNetworkException(@NonNull Throwable t) {
            return (
                    t instanceof MalformedURLException ||
                            t instanceof ProtocolException ||
                            t instanceof SocketException ||
                            t instanceof UnknownHostException ||
                            t instanceof SocketTimeoutException ||
                            t instanceof HttpRetryException ||
                            t instanceof UnknownServiceException ||
                            t instanceof URISyntaxException
            );
        }
    }
}
