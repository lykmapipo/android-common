package com.github.lykmapipo.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.github.florent37.runtimepermission.RuntimePermission;
import com.github.lykmapipo.common.data.Bundleable;
import com.github.lykmapipo.common.data.Dialable;
import com.github.lykmapipo.common.data.Locatable;
import com.github.lykmapipo.common.lifecycle.ConnectivityLiveData;
import com.github.lykmapipo.common.provider.Provider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static android.provider.Settings.ACTION_WIRELESS_SETTINGS;
import static com.github.florent37.runtimepermission.RuntimePermission.askPermission;

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
     * Retrieve application {@link Gson} instance
     *
     * @return valid gson instance
     * @since 0.1.0
     */
    @NonNull
    public static synchronized Gson gson() {
        return gson;
    }

    /**
     * Retrieve application {@link Context}
     *
     * @return current application context
     * @since 0.1.0
     */
    @NonNull
    public static synchronized Context applicationContext() {
        return appProvider.getApplicationContext();
    }

    /**
     * Retrieve application {@link PackageManager}
     *
     * @return current application package manager
     * @since 0.1.0
     */
    @NonNull
    public static synchronized PackageManager packageManager() {
        return applicationContext().getPackageManager();
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

        /**
         * Check if provided {@link String} values are empty
         *
         * @param strings values to check
         * @return if string is null or empty
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean areEmpty(@Nullable String... strings) {
            List<String> list = Value.listOf(strings);
            Boolean areEmpty = false;
            for (String string : list) {
                areEmpty = TextUtils.isEmpty(string);
                if (areEmpty) {
                    break;
                }
            }
            return areEmpty;
        }
    }

    /**
     * Date Utilities
     */
    public static class Dates {
        /**
         * Derive current device timezone
         *
         * @return device timezone
         * @since 0.1.0
         */
        @Nullable
        public static synchronized String timezone() {
            String timezone = "";
            try {
                TimeZone timeZone = TimeZone.getDefault();
                timezone = timeZone.getID();
            } catch (Exception e) {
                timezone = "";
            }

            return timezone;
        }

        /**
         * Parse a given date using given format
         *
         * @param date   valid date to format
         * @param format valid date format
         * @return formatted date
         * @since 0.1.0
         */
        @Nullable
        public static synchronized Date parse(@NonNull String date, @NonNull String format) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
                return formatter.parse(date);
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * Format a given date using given format
         *
         * @param date   valid date to format
         * @param format valid date format
         * @return formatted date
         * @since 0.1.0
         */
        @NonNull
        public static synchronized String format(@NonNull Date date, @NonNull String format) {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
            return formatter.format(date);
        }

        /**
         * Obtain tomorrow of today
         *
         * @return tomorrow date of today
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date tomorrow() {
            return tomorrowOf(today());
        }

        /**
         * Obtain tomorrow of given date
         *
         * @return tomorrow date of a given date
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date tomorrowOf(@NonNull Date date) {
            return after(date, 1);
        }

        /**
         * Obtain yesterday of today
         *
         * @return yesterday date of today
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date yesterday() {
            return yesterdayOf(today());
        }

        /**
         * Obtain yesterday of given date
         *
         * @return yesterday date of a given date
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date yesterdayOf(@NonNull Date date) {
            return before(date, 1);
        }

        /**
         * Verify if checked date is before today
         *
         * @param checked date to check
         * @return if checked date is before today
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean isBeforeToday(@NonNull Date checked) {
            return checked.before(today());
        }

        /**
         * Verify if checked date is after today
         *
         * @param checked date to check
         * @return if checked date is after today
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean isAfterToday(@NonNull Date checked) {
            return checked.after(today());
        }

        /**
         * Obtain current date without time
         *
         * @return today date with time cleared
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date today() {
            return clearTime(new Date());
        }

        /**
         * Obtain current date with time set to midnight
         *
         * @return today date with time set to midnight
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Date todayMidNight() {
            return midNightOf(new Date());
        }

        /**
         * Verify if checked date is before base date
         *
         * @param base    date to compared to
         * @param checked date to check
         * @return if checked date is before base date
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean isBefore(@NonNull Date base, @NonNull Date checked) {
            return checked.before(base);
        }

        /**
         * Verify if checked date is after base date
         *
         * @param base    date to compared to
         * @param checked date to check
         * @return if checked date is after base date
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean isAfter(@NonNull Date base, @NonNull Date checked) {
            return checked.after(base);
        }

        /**
         * Obtain a yesterday which if nth days from a specific date
         *
         * @param date valid date
         * @param days valid amount of days
         * @return date before given date
         * @since 0.1.0
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
         * @since 0.1.0
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

            int mils = calendar.getActualMinimum(Calendar.MILLISECOND);
            int secs = calendar.getActualMinimum(Calendar.SECOND);
            int mins = calendar.getActualMinimum(Calendar.MINUTE);
            int hrs = calendar.getActualMinimum(Calendar.HOUR_OF_DAY);

            calendar.set(Calendar.MILLISECOND, mils);
            calendar.set(Calendar.SECOND, secs);
            calendar.set(Calendar.MINUTE, mins);
            calendar.set(Calendar.HOUR_OF_DAY, hrs);

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

            int mils = calendar.getActualMaximum(Calendar.MILLISECOND);
            int secs = calendar.getActualMaximum(Calendar.SECOND);
            int mins = calendar.getActualMaximum(Calendar.MINUTE);
            int hrs = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);

            calendar.set(Calendar.MILLISECOND, mils);
            calendar.set(Calendar.SECOND, secs);
            calendar.set(Calendar.MINUTE, mins);
            calendar.set(Calendar.HOUR_OF_DAY, hrs);

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

        /**
         * Listen for network state changes
         *
         * @param owner    The LifecycleOwner which controls the observer
         * @param observer The observer that will receive the network status
         * @since 0.1.0
         */
        @MainThread
        @RequiresPermission(ACCESS_NETWORK_STATE)
        public static synchronized void observe(@NonNull LifecycleOwner owner, @NonNull Observer<Boolean> observer) {
            ConnectivityLiveData status = new ConnectivityLiveData(getConnectivityManager());
            status.observe(owner, observer);
        }
    }

    /**
     * Intent Utilities
     */
    public static class Intents {

        private static final String GOOGLE_MAP_PACKAGE = "com.google.android.apps.maps";

        /**
         * Launch turn-by-turn navigation to a given location
         *
         * @param location valid location
         * @return true if success
         * @since 0.1.0
         */
        public static synchronized Boolean navigateTo(@NonNull Locatable location) {
            Float latitude = location.getLatitude();
            Float longitude = location.getLongitude();
            String address = location.getAddress();

            if (latitude != null && longitude != null) {
                return navigateTo(latitude, longitude);
            }

            if (!Strings.isEmpty(address)) {
                return navigateTo(address);
            }

            return false;
        }

        /**
         * Launch turn-by-turn navigation to a given address
         *
         * @param address valid address
         * @return true if success
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean navigateTo(@NonNull String address) {
            String destination = "google.navigation:q=" + Uri.encode(address);
            Uri uri = Uri.parse(destination);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(GOOGLE_MAP_PACKAGE);
            return start(intent);
        }

        /**
         * Launch turn-by-turn navigation to a given point
         *
         * @param latitude  valid latitude
         * @param longitude valid longitude
         * @return true if success
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean navigateTo(@NonNull Float latitude, @NonNull Float longitude) {
            String destination = "google.navigation:q=" + latitude + "," + longitude;
            Uri uri = Uri.parse(destination);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(GOOGLE_MAP_PACKAGE);
            return start(intent);
        }

        /**
         * Request browser to view given url
         *
         * @param url valid url
         * @return true if success
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean browse(@NonNull String url) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            return start(intent);
        }

        /**
         * Request to dial to a given phone number
         *
         * @param dialable valid dialable
         * @return true if success
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean dial(@NonNull Dialable dialable) {
            return dial(dialable.getPhoneNumber());
        }

        /**
         * Request to dial to a given phone number
         *
         * @param phoneNumber valid phone number
         * @return true if success
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean dial(@NonNull String phoneNumber) {
            if (Strings.isEmpty(phoneNumber)) {
                return false;
            }

            String phone = phoneNumber.replace(" ", "");
            Uri uri = Uri.parse("tel:" + phone);

            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            return start(intent);
        }

        /**
         * Show settings to allow configuration of wireless controls such as Wi-Fi,
         * Bluetooth and Mobile networks
         *
         * @return true if started otherwise false
         * @since 0.1.0
         */
        public static synchronized Boolean openWirelessSettings() {
            Intent intent = new Intent(ACTION_WIRELESS_SETTINGS);
            return start(intent);
        }

        /**
         * Show screen of details about a particular application.
         *
         * @return true if started otherwise false
         * @since 0.1.0
         */
        public static synchronized Boolean openApplicationSettings() {
            String packageName = applicationContext().getPackageName();
            Uri uri = Uri.parse("package:" + packageName);

            Intent intent = new Intent(ACTION_APPLICATION_DETAILS_SETTINGS, uri);
            return start(intent);
        }

        /**
         * Start a given intent
         *
         * @param intent valid intent
         * @return true if started otherwise false
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean start(@NonNull Intent intent) {
            try {
                if (canHandle(intent)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    applicationContext().startActivity(intent);
                    return true;
                } else {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }

        /**
         * Verify if there is an app available to receive the intent
         *
         * @param intent valid intent
         * @return true if exist otherwise false
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Boolean canHandle(@NonNull Intent intent) {
            ComponentName component = intent.resolveActivity(packageManager());
            if (component != null) {
                return true;
            } else {
                return true;
            }
        }
    }

    /**
     * Bundle Utilities
     */
    public static class Bundles {
        public static final String PACKAGE = "package"; // package
        public static final String TIMEZONE = "timezone"; //timezone
        public static final String TIME = "time"; //current time
        public static final String MEDIUM = "medium"; //medium(or channel)
        public static final String VALUE_MEDIUM_ANDROID = "android";

        /**
         * Merge given bundleables to single bundle
         *
         * @param bundles valid bundleables
         * @return merged bundle
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Bundle from(@NonNull Bundleable... bundles) {
            Bundle results = new Bundle();
            for (Bundleable bundle : bundles) {
                try {
                    results.putAll(bundle.toBundle());
                } catch (Exception e) {/*ignore*/}
            }
            results.putAll(defaults());
            return results;
        }

        /**
         * Merge given bundles to single bundle
         *
         * @param bundles valid bundle
         * @return merged bundle
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Bundle from(@NonNull Bundle... bundles) {
            Bundle results = new Bundle();
            for (Bundle bundle : bundles) {
                try {
                    results.putAll(bundle);
                } catch (Exception e) {/*ignore*/}
            }
            results.putAll(defaults());
            return results;
        }

        /**
         * Create bundle with defaults values
         *
         * @return default bundle
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Bundle defaults() {
            Bundle params = new Bundle();

            params.putString(PACKAGE, applicationContext().getPackageName());
            params.putString(TIMEZONE, Dates.timezone()); //timezone
            params.putLong(TIME, new Date().getTime()); //time
            params.putString(MEDIUM, VALUE_MEDIUM_ANDROID);//medium

            return params;
        }

        /**
         * Initialize empty bundle
         *
         * @return empty bundle
         * @since 0.1.0
         */
        @NonNull
        public static synchronized Bundle empty() {
            return new Bundle();
        }
    }

    /**
     * Prompt Utilities
     */
    public static class Prompt {

        /**
         * Prompt for an action
         *
         * @param context      valid launch context
         * @param titleResId   prompt title
         * @param messageResId prompt message
         * @param listener     callback to invoke on accept or cancel
         * @since 0.1.0
         */
        public static synchronized void show(
                @NonNull Context context,
                @StringRes Integer titleResId,
                @StringRes Integer messageResId,
                @NonNull Prompt.OnClickListener listener
        ) {
            show(
                    context, null, titleResId,
                    messageResId, R.string.prompt_cancel_text,
                    R.string.prompt_accept_text, listener
            );
        }

        /**
         * Prompt for an action
         *
         * @param context      valid launch context
         * @param themeResId   applied dialog theme
         * @param titleResId   prompt title
         * @param messageResId prompt message
         * @param listener     callback to invoke on accept or cancel
         * @since 0.1.0
         */
        public static synchronized void show(
                @NonNull Context context,
                @StyleRes Integer themeResId,
                @StringRes Integer titleResId,
                @StringRes Integer messageResId,
                @NonNull Prompt.OnClickListener listener
        ) {
            show(
                    context, themeResId, titleResId,
                    messageResId, R.string.prompt_cancel_text,
                    R.string.prompt_accept_text, listener
            );
        }

        /**
         * Prompt for an action
         *
         * @param context         valid launch context
         * @param titleResId      prompt title
         * @param messageResId    prompt message
         * @param cancelTextResId cancel button label
         * @param acceptTextResId accept button label
         * @param listener        callback to invoke on accept or cancel
         * @since 0.1.0
         */
        public static synchronized void show(
                @NonNull Context context,
                @StringRes Integer titleResId,
                @StringRes Integer messageResId,
                @StringRes Integer cancelTextResId,
                @StringRes Integer acceptTextResId,
                @NonNull Prompt.OnClickListener listener
        ) {
            show(
                    context, null,
                    titleResId, messageResId,
                    cancelTextResId, acceptTextResId, listener
            );
        }

        /**
         * Prompt for an action
         *
         * @param context         valid launch context
         * @param themeResId      applied dialog theme
         * @param titleResId      prompt title
         * @param messageResId    prompt message
         * @param cancelTextResId cancel button label
         * @param acceptTextResId accept button label
         * @param listener        callback to invoke on accept or cancel
         * @since 0.1.0
         */
        public static synchronized void show(
                @NonNull Context context,
                @StyleRes Integer themeResId,
                @StringRes Integer titleResId,
                @StringRes Integer messageResId,
                @StringRes Integer cancelTextResId,
                @StringRes Integer acceptTextResId,
                @NonNull Prompt.OnClickListener listener
        ) {
            // prepare prompt
            MaterialAlertDialogBuilder prompt = (
                    themeResId != null ?
                            new MaterialAlertDialogBuilder(context, themeResId) :
                            new MaterialAlertDialogBuilder(context)
            );
            prompt.setTitle(titleResId);
            prompt.setMessage(messageResId);
            // handle cancel
            prompt.setNegativeButton(cancelTextResId, (dialog, i) -> {
                dialog.dismiss();
                listener.onClick(false);
            });
            // handle accept
            prompt.setPositiveButton(acceptTextResId, (dialog, i) -> {
                dialog.dismiss();
                listener.onClick(true);
            });
            // present prompt
            prompt.show();
        }

        /**
         * Prompt actions listener
         *
         * @since 0.1.0
         */
        public interface OnClickListener {
            /**
             * Called when action accepted or cancelled
             *
             * @since 0.1.0
             */
            @MainThread
            void onClick(Boolean accepted);
        }
    }

    /**
     * Permissions Utilities
     */
    public static class Permissions {

        /**
         * Request for permissions
         *
         * @param fragment    launching fragment
         * @param listener    permission granted listener
         * @param permissions requested permission
         * @since 0.1.0
         */
        @MainThread
        public static synchronized void request(
                @NonNull Fragment fragment,
                @NonNull OnGrantedListener listener,
                @Nullable String... permissions
        ) {
            request(fragment.requireActivity(), listener, permissions);
        }

        /**
         * Request for permissions
         *
         * @param activity    launching activity
         * @param listener    permission granted listener
         * @param permissions requested permission
         * @since 0.1.0
         */
        @MainThread
        public static synchronized void request(
                @NonNull FragmentActivity activity,
                @NonNull OnGrantedListener listener,
                @Nullable String... permissions
        ) {
            RuntimePermission request = askPermission(activity, permissions);

            // handle accepted
            request.onAccepted(result -> {
                listener.onResult(true);
            });

            // handle on denied
            request.onDenied(result -> {
                // prompt to allow permissions
                Prompt.show(activity, R.string.prompt_permissions_title,
                        R.string.prompt_permissions_message,
                        R.string.prompt_permissions_cancel_text,
                        R.string.prompt_permissions_accept_text, accepted -> {
                            if (accepted) {
                                result.askAgain();
                            } else {
                                listener.onResult(false);
                                result.goToSettings();
                            }
                        });
            });

            // handle forever denied
            request.onForeverDenied(result -> {
                listener.onResult(false);
                result.goToSettings();
            });

            // do request permissions
            request.ask();
        }

        /**
         * Permissions granted listener
         *
         * @since 0.1.0
         */
        public interface OnGrantedListener {
            @MainThread
            void onResult(Boolean granted);
        }
    }

    /**
     * Executors Utilities
     */
    public static class AppExecutors {
        // constants
        private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
        private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
        private static final long KEEP_ALIVE_TIME = 1L;

        // executors
        private static Executor schedule;
        private static Executor background;
        private static Executor diskIO;
        private static Executor networkIO;
        private static Executor mainThread;

        /**
         * Provide background operations executor that executes tasks in parallel.
         *
         * @return disk executor
         * @since 0.2.0
         */
        @NonNull
        public static synchronized Executor background() {
            if (background == null) {
                ThreadPoolExecutor executor = new ThreadPoolExecutor(
                        CORE_POOL_SIZE,
                        MAX_POOL_SIZE,
                        KEEP_ALIVE_TIME,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>()
                );
                executor.allowCoreThreadTimeOut(true);
                background = executor;
            }
            return background;
        }

        /**
         * Provide schedule operations executor
         *
         * @return disk executor
         * @since 0.2.0
         */
        @NonNull
        public static synchronized Executor schedule() {
            if (schedule == null) {
                schedule = Executors.newSingleThreadScheduledExecutor();
            }
            return schedule;
        }

        /**
         * Provide disk operations executor
         *
         * @return disk executor
         * @since 0.2.0
         */
        @NonNull
        public static synchronized Executor diskIO() {
            if (diskIO == null) {
                diskIO = Executors.newSingleThreadExecutor();
            }
            return diskIO;
        }

        /**
         * Provide network operations executor
         *
         * @return disk executor
         * @since 0.2.0
         */
        @NonNull
        public static synchronized Executor networkIO() {
            if (networkIO == null) {
                networkIO = Executors.newFixedThreadPool(3);
            }
            return networkIO;
        }

        /**
         * Provide main thread operations executor
         *
         * @return disk executor
         * @since 0.2.0
         */
        @NonNull
        public static synchronized Executor mainThread() {
            if (mainThread == null) {
                mainThread = new MainThreadExecutor();
            }
            return mainThread;
        }

        private static class MainThreadExecutor implements Executor {
            private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(@NonNull Runnable command) {
                mainThreadHandler.post(command);
            }
        }
    }
}
