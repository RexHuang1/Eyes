package com.dev.rexhuang.eyes.view.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

/**
 * *  created by RexHuang
 * *  on 2019/4/13
 */
public abstract class OnPortableScrollerListener extends OnScrollListener {

    /**
     * Used to mark whether scroll up.
     */
    private boolean mIsScrollUp = false;

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        // When not scroll
        if (newState == RecyclerView.SCROLL_STATE_IDLE && linearLayoutManager != null){
            int lastItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            int itemCount = linearLayoutManager.getItemCount();
            // Scroll to the last item, and it's scroll up.
            if (lastItemPosition == itemCount -1 && mIsScrollUp){
                // Load More
                onLoadMore();
            }
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mIsScrollUp = dy > 0;
    }

    /**
     * Callback method for scroll to end.
     */
    public abstract void onLoadMore();
}
