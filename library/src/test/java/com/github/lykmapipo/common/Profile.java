package com.github.lykmapipo.common;

import com.google.gson.annotations.Expose;

public class Profile {
    @Expose
    String name;

    public Profile(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        return name != null ? name.equals(profile.name) : profile.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
