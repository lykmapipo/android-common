package com.github.lykmapipo.common;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.github.lykmapipo.common.data.Bundleable;
import com.google.gson.annotations.Expose;

public class User implements Bundleable {
    @Expose
    String name;

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
}
