package com.github.lykmapipo.common.data;

import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * A contract to be implemented by object that will be presented {@link android.os.Bundle}.
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public interface Bundleable {
    /**
     * Wrap value into {@link Bundle}
     *
     * @return bundle
     * @since 0.1.0
     */
    @NonNull
    Bundle toBundle();
}
