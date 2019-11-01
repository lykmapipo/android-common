package com.github.lykmapipo.common.provider;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Provides ability to retrieve current application information.
 *
 * @since 0.1.0
 */
public interface Provider {
    /**
     * Returns application {@link Context}.
     * <p>
     * {@link Context#getApplicationContext()}
     */
    @NonNull
    Context getApplicationContext();

    /**
     * Returns application debug state from build config
     *
     * @return if build is debug
     */
    @NonNull
    Boolean isDebug();
}
