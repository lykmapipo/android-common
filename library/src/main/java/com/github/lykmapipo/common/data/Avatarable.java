package com.github.lykmapipo.common.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A contract to be implemented by object that can provide as letter avatar
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.7.0
 */
public interface Avatarable {
    /**
     * Valid single letter {@link String}
     *
     * @return letter to use for avatar
     * @since 0.1.0
     */
    @NonNull
    String getLetter();

    /**
     * Background color for the avatar {@link android.graphics.drawable.Drawable} in hexadecimal.
     * <p>
     * If not provided random color will be generated.
     *
     * @return
     */
    @Nullable
    String getColor();
}
