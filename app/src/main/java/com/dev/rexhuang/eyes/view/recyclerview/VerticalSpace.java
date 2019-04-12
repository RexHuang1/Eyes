package com.dev.rexhuang.eyes.view.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * *  created by RexHuang
 * *  on 2019/4/12
 */
public class VerticalSpace extends RecyclerView.ItemDecoration {

    private int verticalSpace;

    public VerticalSpace(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = verticalSpace;
        }
    }
}
