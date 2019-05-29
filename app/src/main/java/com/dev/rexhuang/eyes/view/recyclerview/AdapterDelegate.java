package com.dev.rexhuang.eyes.view.recyclerview;

import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * *  created by RexHuang
 * *  on 2019/4/12
 */
public abstract class AdapterDelegate<T,VH extends RecyclerView.ViewHolder> {

    public static final String DEFAULT_TAG = "";

    private String mTag = DEFAULT_TAG;

    private int[] viewType;

    public AdapterDelegate() {

    }

    public AdapterDelegate(String tag) {
        if (null == tag || tag.length() == 0) {
            throw new NullPointerException("The tag of" + this + "is null.");
        }
        this.setTag(tag);
    }

    public String getTag() {
        return this.mTag;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public boolean isForViewType(T item, int position) {
        return true;
    }

    public abstract VH onCreateViewHolder(ViewGroup parent);

    public abstract void onBindViewHolder(VH holder, int position, T item);

    public void onBindViewHolder(VH holder, int position, List<Object> payloads, T item) {

    }

    public void onViewRecycled(VH holder) {
    }

    public boolean onFailedToRecycleView(VH holder) {
        return false;
    }

    public void onViewAttachedToWindow(VH holder) {
    }

    public void onViewDetachedFromWindow(VH holder) {
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    }

    public int[] getViewType() {
        return viewType;
    }

    public void setViewType(int[] viewType) {
        this.viewType = viewType;
    }
}
