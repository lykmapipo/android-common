package com.github.lykmapipo.common;

import android.content.Context;

import androidx.annotation.NonNull;

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
    public static synchronized Context getApplicationContext() {
        return appProvider.getApplicationContext();
    }

    /**
     * Clean up and reset {@link Common} internals
     */
    public static synchronized void dispose() {
        appProvider = null;
    }

    /**
     * Provides ability to retrieve current application information.
     *
     * @since 0.1.0
     */
    public interface Provider {
        /**
         * Returns the application {@link Context}.
         * <p>
         * {@link Context#getApplicationContext()}
         */
        @NonNull
        Context getApplicationContext();
    }
}
