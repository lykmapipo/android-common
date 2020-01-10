package com.github.lykmapipo.common;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.lykmapipo.common.data.Avatarable;
import com.github.lykmapipo.common.data.Bundleable;
import com.github.lykmapipo.common.data.Dialable;
import com.github.lykmapipo.common.data.Diffable;
import com.google.gson.annotations.Expose;

public class User implements Bundleable, Diffable, Dialable, Avatarable {
    @Expose
    String name;

    @Expose
    String phoneNumber;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return name != null ? name.equals(user.name) : user.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @NonNull
    @Override
    public Bundle toBundle() {
        Bundle bundle = Common.Bundles.empty();
        bundle.putString("name", name);
        return bundle;
    }

    @NonNull
    @Override
    public String getObjectId() {
        return name;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Valid single letter {@link String}
     *
     * @return letter to use for avatar
     * @since 0.1.0
     */
    @NonNull
    @Override
    public String getAvatarLetter() {
        return String.valueOf(name.charAt(0));
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
        return "#271d45";
    }
}
