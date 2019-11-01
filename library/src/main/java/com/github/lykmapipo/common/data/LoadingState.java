package com.github.lykmapipo.common.data;

import androidx.annotation.NonNull;

/**
 * A generic class that holds data loading states.
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class LoadingState {

    public static final LoadingState INITIAL;
    public static final LoadingState REFRESHING;
    public static final LoadingState LOADING;
    public static final LoadingState SUCCESS;
    public static final LoadingState EMPTY;
    public static final LoadingState OFFLINE;
    public static final LoadingState FAILED;

    static {
        INITIAL = new LoadingState(Status.INITIAL, "Initial");
        REFRESHING = new LoadingState(Status.REFRESHING, "Refreshing");
        LOADING = new LoadingState(Status.LOADING, "Running");
        SUCCESS = new LoadingState(Status.SUCCESS, "Success");
        EMPTY = new LoadingState(Status.EMPTY, "Empty");
        OFFLINE = new LoadingState(Status.OFFLINE, "Offline");
        FAILED = new LoadingState(Status.FAILED, "Failed");
    }

    private final Status status;
    private final String message;
    private final Throwable throwable;

    public LoadingState(Status status, String message) {
        this.status = status;
        this.message = message;
        this.throwable = null;
    }

    public LoadingState(Status status, String message, Throwable t) {
        this.status = status;
        this.message = message;
        this.throwable = t;
    }

    public static LoadingState failed(String message) {
        return new LoadingState(Status.FAILED, message, null);
    }

    public static LoadingState failed(Throwable t) {
        return new LoadingState(Status.FAILED, t.getMessage(), t);
    }

    public static LoadingState failed(String message, Throwable t) {
        return new LoadingState(Status.FAILED, message, t);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoadingState that = (LoadingState) o;

        return status == that.status;
    }

    @Override
    public int hashCode() {
        return status != null ? status.hashCode() : 0;
    }

    @NonNull
    @Override
    public String toString() {
        return message;
    }

    public enum Status {
        INITIAL, // signal initial loading
        REFRESHING, // signal refreshing
        LOADING, //signal is loading next pages
        SUCCESS, // signal success loading
        EMPTY, // signal initial load succeed with empty results
        OFFLINE, // signal offline
        FAILED, //signal failed
    }
}
