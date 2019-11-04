package com.github.lykmapipo.common.data;

import androidx.annotation.NonNull;

/**
 * A contract to be implemented by object that will be presented by
 * {@link androidx.paging.PagedListAdapter}
 * or {@link androidx.recyclerview.widget.ListAdapter}
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public interface Diffable<T> {
    /**
     * Obtain valid stable id for the diffable item
     *
     * @return object id
     * @since 0.1.0
     */
    @NonNull
    String getObjectId();

    /**
     * Check if current diffable is same as the given diffable
     *
     * @param other compared diffable
     * @return true or false
     * @since 0.1.0
     */
    @NonNull
    Boolean isSame(T other);
}
