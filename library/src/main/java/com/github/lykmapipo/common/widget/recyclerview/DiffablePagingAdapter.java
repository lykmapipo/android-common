package com.github.lykmapipo.common.widget.recyclerview;

import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lykmapipo.common.data.Diffable;

/**
 * {@link RecyclerView RecyclerView.Adapter} base class for presenting {@link Diffable} paged
 * data from {@link androidx.paging.PagedList} in a {@link RecyclerView}
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @since 0.1.0
 */
public abstract class DiffablePagingAdapter<T, VH extends RecyclerView.ViewHolder>
        extends PagedListAdapter<Diffable, VH> {

    /**
     * Construct a new {@link DiffablePagingAdapter}
     */
    public DiffablePagingAdapter() {
        super(new DiffableCallback<T>());
    }

}
