package com.dev.rexhuang.eyes.view.recyclerview;

import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * *  created by RexHuang
 * *  on 2019/4/12
 */
public abstract class AbsDelegationAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected AdapterDelegatesManager<VH> mDelegatesManager;

    public AbsDelegationAdapter() {
        this(new AdapterDelegatesManager());
    }

    public AbsDelegationAdapter(AdapterDelegatesManager delegatesManager) {
        if (delegatesManager == null) {
            throw new NullPointerException("AdapterDelegatesManager is null.");
        }
        this.mDelegatesManager = delegatesManager;
    }

    public void addDelegate(AdapterDelegate delegate) {
        addDelegate(delegate, delegate.getTag());
    }

    private void addDelegate(AdapterDelegate delegate, String tag) {
        delegate.setTag(tag);
        mDelegatesManager.addDelegate(delegate, tag);
    }

    public void setFallbackDelegate(AdapterDelegate delegate) {
        mDelegatesManager.setFallbackDelegate(delegate);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mDelegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        mDelegatesManager.onBindViewHolder(holder, position, getItem(position));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, position);
        mDelegatesManager.onBindViewHolder(holder, position, payloads, getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mDelegatesManager.getItemViewType(getItem(position), position);
    }

    @Override
    public void onViewRecycled(@NonNull VH holder) {
        mDelegatesManager.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull VH holder) {
        return mDelegatesManager.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        mDelegatesManager.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        mDelegatesManager.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mDelegatesManager.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        mDelegatesManager.onDetachedFromRecyclerView(recyclerView);
    }


    protected abstract Object getItem(int position);
}
