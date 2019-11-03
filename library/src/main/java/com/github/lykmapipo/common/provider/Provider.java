package com.github.lykmapipo.common.provider;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import java.util.Set;

/**
 * Provides ability to retrieve current application information.
 *
 * @since 0.1.0
 */
public abstract class Provider {
    /**
     * Returns application {@link Context}.
     * <p>
     * {@link Context#getApplicationContext()}
     */
    @NonNull
    public abstract Context getApplicationContext();

    /**
     * Returns application debug state from build config
     *
     * @return if build is debug, default to true
     */
    @NonNull
    public Boolean isDebug() {
        return true;
    }

    /**
     * Returns application ignored log levels
     *
     * @return if build is debug, default are {@link Log#VERBOSE},
     * {@link Log#DEBUG}, {@link Log#INFO}
     */
    public Set<Integer> ignoredLogLevels() {
        ArraySet<Integer> ignored = new ArraySet<Integer>();
        ignored.add(Log.VERBOSE);
        ignored.add(Log.DEBUG);
        ignored.add(Log.INFO);
        return ignored;
    }

}
