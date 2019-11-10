package com.github.lykmapipo.common.widget.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.github.lykmapipo.common.data.Diffable;

/**
 * Callback that informs {@link androidx.paging.PagedListAdapter} and
 * {@link androidx.recyclerview.widget.ListAdapter} how to compute
 * list updates when using {@link androidx.recyclerview.widget.DiffUtil}
 * on a background thread.
 *
 * @since 0.1.0
 */
public class DiffableCallback<T extends Diffable> extends DiffUtil.ItemCallback<T> {
    /**
     * Called to decide whether two objects represent the same item.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the two items represent the same object or false if they are different.
     * @see androidx.recyclerview.widget.DiffUtil.Callback#areContentsTheSame(int, int)
     */
    @Override
    public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return oldItem.getObjectId().equals(newItem.getObjectId());
    }

    /**
     * Called to decide whether two items have the same data. This information is used to detect if
     * the contents of an item have changed.
     *
     * @param oldItem The item in the old list.
     * @param newItem The item in the new list.
     * @return True if the contents of the items are the same or false if they are different.
     * @see androidx.recyclerview.widget.DiffUtil.Callback#areContentsTheSame(int, int)
     */
    @Override
    public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return oldItem.equals(newItem);
    }
}
