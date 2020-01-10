package com.github.lykmapipo.common;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public class UserNoColor extends User {

    public UserNoColor(String name) {
        super(name);
    }

    /**
     * Background color for the avatar {@link Drawable} in hexadecimal.
     * <p>
     * If not provided random color will be generated.
     *
     * @return
     */
    @Nullable
    @Override
    public String getAvatarColor() {
        return null;
    }
}
