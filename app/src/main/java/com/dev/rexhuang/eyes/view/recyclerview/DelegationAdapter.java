package com.dev.rexhuang.eyes.view.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.rexhuang.eyes.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * *  created by RexHuang
 * *  on 2019/4/12
 */
public class DelegationAdapter<VH extends RecyclerView.ViewHolder> extends AbsDelegationAdapter<VH> implements View.OnClickListener, View.OnLongClickListener {

    private boolean haveLoading = false;
    private OnItemClickListener onItemClickListener;
    private int TYPE_LOADING = -1;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setHaveLoading(boolean haveLoading) {
        this.haveLoading = haveLoading;
    }

    public boolean isHaveLoading() {
        return haveLoading;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);

        void onItemLongClick(View itemView, int position);
    }

    private List<Object> mDataItems = new ArrayList<>();
    private List<Object> mHeaderItems = new ArrayList<>();
    private List<Object> mFooterItems = new ArrayList<>();

    public DelegationAdapter() {
    }

    public DelegationAdapter(@NonNull AdapterDelegatesManager delegatesManager) {
        super(delegatesManager);
    }

    public void setHeaderItem(Object headerItem) {
        mHeaderItems.clear();
        mHeaderItems.add(headerItem);
        notifyDataSetChanged();
    }

    public void setHeaderItems(List headerItems) {
        this.mHeaderItems = headerItems;
        notifyDataSetChanged();
    }

    public void addHeaderItem(Object headerItem) {
        addHeaderItem(getHeaderCount(), headerItem);
    }

    public void addHeaderItem(int position, Object headerItem) {
        mHeaderItems.add(position, headerItem);
        notifyItemRangeInserted(position, 1);
    }

    public void addHeaderItems(List headerItems) {
        addHeaderItems(getHeaderCount(), headerItems);
    }

    private void addHeaderItems(int position, List headerItems) {
        mHeaderItems.addAll(position, headerItems);
        notifyItemRangeInserted(position, headerItems.size());
    }

    public void setFooterItem(Object footerItem) {
        mFooterItems.clear();
        mFooterItems.add(footerItem);
        notifyDataSetChanged();
    }

    public void setFooterItems(List footerItems) {
        mFooterItems = footerItems;
        notifyDataSetChanged();
    }

    public void addFooterItem(Object footerItem) {
        addFooterItem(getFooterCount(), footerItem);
    }

    public void addFooterItem(int position, Object footerItem) {
        mFooterItems.add(position, footerItem);
        notifyItemRangeInserted(getHeaderCount() + getDataCount() + position, 1);
    }

    public void addFooterItems(List footerItems) {
        addFooterItems(getFooterCount(), footerItems);
    }

    private void addFooterItems(int position, List footerItems) {
        mFooterItems.addAll(position, footerItems);
        notifyItemRangeInserted(getHeaderCount() + getDataCount() + position, footerItems.size());
    }

    public void setDataItems(List dataItems) {
        mDataItems = dataItems;
        notifyDataSetChanged();
    }

    public void addDataItem(Object item) {
        addDataItem(getDataCount(), item);
    }

    public void addDataItem(int position, Object item) {
        mDataItems.add(position, item);
        notifyItemRangeInserted(getHeaderCount() + position, 1);
    }

    public void addDataItems(List dataItems) {
        addDataItems(getDataCount(), dataItems);
    }

    public void addDataItems(int position, List dataItems) {
        mDataItems.addAll(position, dataItems);
        notifyItemRangeInserted(getHeaderCount() + position, dataItems.size());
    }

    public void moveDataItem(int fromPosition, int toPosition) {
        toPosition = fromPosition < toPosition ? toPosition - 1 : toPosition;
        mDataItems.add(toPosition, mDataItems.remove(fromPosition));
        notifyItemMoved(fromPosition, toPosition);
    }

    public void removeDataItem(Object dataItem) {
        int index = mDataItems.indexOf(dataItem);
        if (index != -1 && index <= getDataCount()) {
            removeDataItem(index);
        }
    }

    public void removeDataItem(int position) {
        removeDataItem(position, 1);
    }

    public void removeDataItem(int position, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            mDataItems.remove(position);
        }
        notifyItemRangeRemoved(getHeaderCount() + position, itemCount);
    }

    public List<Object> getDataItems() {
        return mDataItems;
    }

    public List<Object> getHeaderItems() {
        return mHeaderItems;
    }

    public List<Object> getFooterItems() {
        return mFooterItems;
    }

    @Override
    protected Object getItem(int position) {
        if (position < getHeaderCount()) {
            return mHeaderItems.get(position);
        }

        position -= getHeaderCount();

        if (position < getFooterCount()) {
            return mFooterItems.get(position);
        }
        return mDataItems.get(position);
    }

    @Override
    public int getItemCount() {
        if (isHaveLoading()){
            return getHeaderCount() + getDataCount() + getFooterCount() + 1;
        }
        return getHeaderCount() + getDataCount() + getFooterCount();
    }

    public int getDataCount() {
        return mDataItems.size();
    }

    public int getHeaderCount() {
        return mHeaderItems.size();
    }

    public int getFooterCount() {
        return mFooterItems.size();
    }

    public void clearData() {
        mDataItems.clear();
    }

    public void clearHeader() {
        mHeaderItems.clear();
    }

    public void clearFooter() {
        mFooterItems.clear();
    }

    public void clearAllData() {
        clearData();
        clearHeader();
        clearFooter();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (haveLoading && position == getItemCount() - 1 ){

        }
        holder.itemView.setTag(R.id.tag_clickable_adapter_delegate_holder, holder);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnLongClickListener(this);
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onClick(View v) {
        VH holder = (VH) v.getTag(R.id.tag_clickable_adapter_delegate_holder);
        if (holder != null) {
            int position = holder.getAdapterPosition();
            if (holder != null)
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v,position);
                }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position == getItemCount() - 1 && isHaveLoading() == true){
//            return TYPE_LOADING;
//        }
//        return super.getItemViewType(position);
//    }
//
//    @NonNull
//    @Override
//    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == TYPE_LOADING){
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_loading,parent,false);
//            return (VH) new LoadingViewHolder(itemView);
//        }
//        return super.onCreateViewHolder(parent, viewType);
//    }
//
//    public void startLoading(){
//
//    }
//
//    public void shutdownLoading(){
//
//    }
//
//    private class LoadingViewHolder extends RecyclerView.ViewHolder {
//
//        public LoadingViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
}
