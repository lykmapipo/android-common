package com.github.lykmapipo.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
     * @since 0.1.0
     */
    @NonNull
    public static synchronized Context getApplicationContext() {
        return appProvider.getApplicationContext();
    }


    /**
     * Retrieve application build state from build config
     *
     * @return current application build state
     * @since 0.1.0
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

        /**
         * Create a {@link java.util.Set} of given elements.
         *
         * @param elements set members
         * @return set from given elements
         * @since 0.1.0
         */
        @SafeVarargs
        @NonNull
        public static synchronized <T> Set<T> setOf(@NonNull T... elements) {
            ArraySet<T> set = new ArraySet<T>();
            Collections.addAll(set, elements);
            return set;
        }

        /**
         * Create a {@link java.util.List} of given elements.
         *
         * @param elements list members
         * @return set from given elements
         * @since 0.1.0
         */
        @SafeVarargs
        @NonNull
        public static synchronized <T> List<T> listOf(@NonNull T... elements) {
            ArrayList<T> list = new ArrayList<T>();
            Collections.addAll(list, elements);
            return list;
        }

        /**
         * Obtain a value or either
         *
         * @param value        value
         * @param defaultValue default value
         * @return either value or default value
         * @since 0.1.0
         */
        @NonNull
        public static synchronized <T> T valueOr(@Nullable T value, @NonNull T defaultValue) {
            return value == null ? defaultValue : value;
        }
    }

    /**
     * String Utilities
     */
    public static class Strings {
        /**
         * Join strings
         *
         * @param strings values to join
         * @return joined strings
         * @since 0.1.0
         */
        @NonNull
        public static synchronized String join(@NonNull String... strings) {
            List<String> parts = Value.listOf(strings);
            return join(parts, false);
        }

        /**
         * Join strings
         *
         * @param strings values to join
         * @return joined strings
         * @since 0.1.0
         */
        @NonNull
        public static synchronized String join(@Nullable List<String> strings) {
            return join(strings, false);
        }

        /**
         * Join strings
         *
         * @param strings values to join
         * @param unique  whether to ensure unique
         * @return joined strings
         * @since 0.1.0
         */
        @NonNull
        public static synchronized String join(@Nullable List<String> strings, @NonNull Boolean unique) {
            if (strings == null) {
                return "";
            }
            ArrayList<String> parts = new ArrayList<String>();
            for (String string : strings) {
                if (!isEmpty(string)) {
                    boolean shouldAdd = !unique || !parts.contains(string);
                    if (shouldAdd) {
                        parts.add(string);
                    }
                }
            }
            return TextUtils.join(",", parts);
        }

        /**
         * Obtain a {@link String} value or either
         *
         * @param value string value
         * @return either string or default value
         * @since 0.1.0
         */
        public static synchronized String valueOr(String value) {
            // return either
            return valueOr(value, "N/A");
        }

        /**
         * Obtain a {@link String} value or either
         *
         * @param value        string value
         * @param defaultValue default value
         * @return either string or default value
         * @since 0.1.0
         */
        @NonNull
        public static synchronized String valueOr(@Nullable String value, @NonNull String defaultValue) {
            String defaultVal = !isEmpty(defaultValue) ? defaultValue : "N/A";
            return !isEmpty(value) ? value : defaultVal;
        }

        /**
         * Check if provided {@link String} value is empty
         *
         * @param string value to check
         * @return if string is null or empty
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean isEmpty(@Nullable String string) {
            return TextUtils.isEmpty(string);
        }
    }

    /**
     * Date Utilities
     */
    public static class Dates {
        /**
         * Obtain current date without time
         *
         * @return date
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date today() {
            return clearTime(new Date());
        }

        /**
         * Obtain current date with time set to midnight
         *
         * @return date
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date todayMidNight() {
            return midNightOf(new Date());
        }

        /**
         * Obtain a yesterday which if nth days from a specific date
         *
         * @param date valid date
         * @param days valid amount of days
         * @return date before given date
         */
        @NonNull
        public static synchronized Date before(@NonNull Date date, @NonNull Integer days) {
            Integer length = days;
            if (length > 0) {
                length = 0 - length;
            }
            return addDays(date, length);
        }

        /**
         * Obtain a tomorrow which if nth days from a specific date
         *
         * @param date valid date
         * @param days valid amount of days
         * @return date after given date
         */
        @NonNull
        public static synchronized Date after(@NonNull Date date, @NonNull Integer days) {
            Integer length = days;
            if (length < 0) {
                length = 0 - length;
            }
            return addDays(date, length);
        }

        /**
         * Add days to a given date object and return a new date
         *
         * @param date valid date
         * @param days valid amount of days to be added
         * @return date with days added and time cleared
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date addDays(@NonNull Date date, @NonNull Integer days) {
            Calendar calendar = Calendar.getInstance();
            Date dt = clearTime(date);
            calendar.setTime(dt);

            calendar.add(Calendar.DATE, days);

            return calendar.getTime();
        }

        /**
         * Clear milliseconds, seconds, minutes and hours from a date
         *
         * @param date valid date
         * @return date with milliseconds, seconds, minutes and hours cleared
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date clearTime(@NonNull Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) date.clone());

            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);

            return calendar.getTime();
        }

        /**
         * Obtain mid night of a given date
         *
         * @param date valid date
         * @return date reset to its mid night time
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date midNightOf(@NonNull Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) date.clone());

            calendar.set(Calendar.MILLISECOND, 99);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.HOUR, 23);

            return calendar.getTime();
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
         * @since 0.1.0
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
         * @since 0.1.0
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
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean isOffline(@NonNull Throwable t) {
            return !isConnected() || isNetworkException(t);
        }
    }
}
