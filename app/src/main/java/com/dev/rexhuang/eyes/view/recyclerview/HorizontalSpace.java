package com.dev.rexhuang.eyes.view.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * *  created by RexHuang
 * *  on 2019/4/12
 */
public class HorizontalSpace extends RecyclerView.ItemDecoration {

    private final int horizontalSpaceWidth;

    public HorizontalSpace(int horizontalSpaceWidth) {

        this.horizontalSpaceWidth = horizontalSpaceWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position != parent.getAdapter().getItemCount() - 1) {
            outRect.right = horizontalSpaceWidth;
        }
    }
}