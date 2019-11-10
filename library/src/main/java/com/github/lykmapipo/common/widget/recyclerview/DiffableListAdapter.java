package com.github.lykmapipo.common.widget.recyclerview;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lykmapipo.common.data.Diffable;

/**
 * {@link RecyclerView.Adapter RecyclerView.Adapter} base class for presenting {@link Diffable}
 * list data in a {@link RecyclerView}, including computing diffs between lists
 * on a background thread.
 *
 * @param <T>  Type of the Lists this Adapter will receive.
 * @param <VH> A class that extends ViewHolder that will be used by the adapter.
 * @author lally elias <lallyelias87@gmail.com>
 * @since 0.1.0
 */
public abstract class DiffableListAdapter<T extends Diffable, VH extends RecyclerView.ViewHolder>
        extends ListAdapter<T, VH> {

    /**
     * Construct a new {@link DiffableListAdapter}
     */
    public DiffableListAdapter() {
        super(new DiffableCallback<T>());
    }

}
