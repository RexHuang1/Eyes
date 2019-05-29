package com.dev.rexhuang.eyes.view.recyclerview;

import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * *  created by RexHuang
 * *  on 2019/4/12
 */
public class AdapterDelegatesManager<VH extends RecyclerView.ViewHolder> {

    private SparseArray<String> mDataTypeWithTags = new SparseArray<>();
    private SparseArrayCompat<AdapterDelegate<Object, VH>> mDelegates = new SparseArrayCompat();
    private SparseArrayCompat<int[]> mDelegatesViewType = new SparseArrayCompat();
    protected AdapterDelegate mFallbackDelegate;

    public AdapterDelegatesManager addDelegate(AdapterDelegate<Object, VH> delegate, String tag) {
        Type superclass = delegate.getClass().getGenericSuperclass();
        try {
            Class<?> clazz = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
            String typeWithTag = getTypeWithTag(clazz, tag);

            int viewType = mDelegates.size();
            mDelegates.put(viewType, delegate);
            mDataTypeWithTags.put(viewType, typeWithTag);
            int[] delegateViewType = delegate.getViewType();
            mDelegatesViewType.put(viewType,delegateViewType);
        } catch (Exception e) {
            // Has no generics or generics not correct.
            throw new IllegalArgumentException(
                    String.format("Please set the correct generic parameters on %s.", delegate.getClass().getName()));
        }
        return this;
    }

    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterDelegate<Object, VH> delegate = getDelegate(viewType);
        int[] delegateViewType = delegate.getViewType();
        if (delegate == null) {
            throw new NullPointerException("No AdapterDelegate added for ViewType " + viewType);

        }
        VH vh = delegate.onCreateViewHolder(parent);
        if (vh == null) {
            throw new NullPointerException("ViewHolder returned from AdapterDelegate "
                    + delegate
                    + " for ViewType ="
                    + viewType
                    + " is null!");
        }
        return vh;
    }

    public void onBindViewHolder(VH holder, int position, Object item) {
        int viewType = holder.getItemViewType();
        AdapterDelegate<Object, VH> delegate = getDelegate(viewType);
        if (delegate == null) {
            throw new NullPointerException("No delegate found for item at position = "
                    + position
                    + " for viewType = "
                    + viewType);
        }
        delegate.onBindViewHolder(holder, position, item);
    }

    public void onBindViewHolder(VH holder, int position, List payloads, Object item) {
        int viewType = holder.getItemViewType();
        AdapterDelegate<Object, VH> delegate = getDelegate(viewType);
        if (delegate == null) {
            throw new NullPointerException("No delegate found for item at position = "
                    + position
                    + " for viewType = "
                    + viewType);
        }
        delegate.onBindViewHolder(holder, position, payloads, item);
    }

    public int getItemViewType(Object item, int position) {
        if (item == null) {
            throw new NullPointerException("Item data source is null.");
        }
        Class clazz = getTargetClass(item);
        String tag = getTargetTag(item);

        String typeWithTag = getTypeWithTag(clazz, tag);
        ArrayList<Integer> indexList = indexesOfValue(mDataTypeWithTags, typeWithTag);
        if (indexList.size() > 0) {
            for (Integer index : indexList) {
                AdapterDelegate<Object, VH> delegate = mDelegates.valueAt(index);
                if (null != delegate
                        && delegate.getTag().equals(tag)
                        && delegate.isForViewType(item, position)) {
                    return index;
                }
            }
        }

        if (mFallbackDelegate != null) {
            return mDelegates.size();
        }

        throw new NullPointerException("No AdapterDelegate added that matches position="
                + position + " item=" + item + " in data source.");
    }

    public void onViewRecycled(VH holder) {
        AdapterDelegate<Object, VH> delegate = getDelegate(holder.getItemViewType());
        if (delegate != null) {
            delegate.onViewRecycled(holder);
        }
    }

    public boolean onFailedToRecycleView(VH holder) {
        AdapterDelegate<Object, VH> delegate = getDelegate(holder.getItemViewType());
        if (delegate != null) {
            return delegate.onFailedToRecycleView(holder);
        }
        return false;
    }

    public void onViewAttachedToWindow(VH holder) {
        AdapterDelegate<Object, VH> delegate = getDelegate(holder.getItemViewType());
        if (delegate != null) {
            delegate.onViewDetachedFromWindow(holder);
        }
    }

    public void onViewDetachedFromWindow(VH holder) {
        AdapterDelegate<Object, VH> delegate = getDelegate(holder.getItemViewType());
        if (delegate != null) {
            delegate.onViewDetachedFromWindow(holder);
        }
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        for (int i = 0; i < mDelegates.size(); i++) {
            AdapterDelegate<Object, VH> delegate = mDelegates.get(mDelegates.keyAt(i));
            delegate.onAttachedToRecyclerView(recyclerView);
        }
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        for (int i = 0; i < mDelegates.size(); i++) {
            AdapterDelegate<Object, VH> delegate = mDelegates.get(mDelegates.keyAt(i));
            delegate.onDetachedFromRecyclerView(recyclerView);
        }
    }

    public AdapterDelegatesManager setFallbackDelegate(AdapterDelegate fallbackDelegate) {
        this.mFallbackDelegate = fallbackDelegate;
        return this;
    }

    @Nullable
    public AdapterDelegate getFallbackDelegate() {
        return mFallbackDelegate;
    }

    public AdapterDelegate<Object, VH> getDelegate(int viewType) {
        return mDelegates.get(viewType, mFallbackDelegate);
    }

    private String getTypeWithTag(Class clazz, String tag) {
        if (tag.length() == 0) {
            return clazz.getName();
        } else {
            return clazz.getName() + ":" + tag;
        }
    }

    private Class getTargetClass(Object data) {
        return data instanceof ItemData ? ((ItemData) data).getData().getClass() : data.getClass();
    }

    private String getTargetTag(Object data) {
        return data instanceof ItemData ? ((ItemData) data).getTag() : AdapterDelegate.DEFAULT_TAG;
    }

    private ArrayList<Integer> indexesOfValue(SparseArray<String> array, String value) {
        ArrayList<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            if (value.equals(array.valueAt(i))) {
                indexes.add(i);
            }
        }
        return indexes;
    }
}
