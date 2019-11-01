package com.github.lykmapipo.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.lykmapipo.common.provider.Provider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
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
    private static ConnectivityManager appConnectivity;
    private static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
            .serializeNulls()
            .create();

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
     * Value Utilities
     */
    public static class Value {
        /**
         * Convert a generic object value to a json string
         *
         * @param value the object for which Json representation is to
         *              be created setting for Gson.
         * @return json representation of {@code value}.
         * @since 0.1.0
         */
        @Nullable
        public static synchronized <T> String toJson(@NonNull T value) {
            try {
                return gson.toJson(value);
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * Convert a json string to generic object value.
         *
         * @param value valid json string value
         * @param type  valid type of the desired object
         * @return an object of type T from the string. Returns {@code null} if {@code value}
         * is null or if {@code value} is empty.
         * @since 0.1.0
         */
        @Nullable
        public static synchronized <T> T fromJson(@NonNull String value, @NonNull Class<T> type) {
            try {
                return gson.fromJson(value, type);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * Network Utilities
     */
    public static class Network {
        /**
         * Obtain application {@link ConnectivityManager}
         *
         * @return valid instance of connectivity manager
         */
        @NonNull
        public static synchronized ConnectivityManager getConnectivityManager() {
            if (appConnectivity == null) {
                Context context = appProvider.getApplicationContext();
                appConnectivity =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            }
            return appConnectivity;
        }

        /**
         * Check if there is internet or data connection on the device
         *
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean isConnected() {
            ConnectivityManager manager = getConnectivityManager();
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
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

        /**
         * Check if {@link Throwable} is due to device connection lost
         *
         * @return true if so else false
         * @version 0.1.0
         * @since 0.1.0
         */
        public static synchronized Boolean isOffline(@NonNull Throwable t) {
            return !isConnected() || isNetworkException(t);
        }
    }
}
