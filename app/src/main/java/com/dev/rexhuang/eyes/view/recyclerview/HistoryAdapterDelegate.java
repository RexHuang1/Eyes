package com.dev.rexhuang.eyes.view.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dev.rexhuang.eyes.R;
import com.dev.rexhuang.eyes.model.HomePicEntity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * *  created by RexHuang
 * *  on 2019/4/12
 */
public class HistoryAdapterDelegate extends AdapterDelegate<HomePicEntity.IssueListEntity.ItemListEntity, HistoryAdapterDelegate.ViewHolder> {

    private static final  int VIDEO = -2;
    private static final int TEXT = -1;
    public int getRealViewType(HomePicEntity.IssueListEntity.ItemListEntity itemListEntity){
        if (itemListEntity.getType().equals("video")){
            return VIDEO;
        }
        return TEXT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.videolist_history_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, HomePicEntity.IssueListEntity.ItemListEntity itemListEntities) {
        ViewHolder videoListViewHolder = holder;
//        Log.e(TAG, "realPosition : " + realPosition);
        HomePicEntity.IssueListEntity.ItemListEntity itemListEntity = itemListEntities;//.get(position);
        String feed;
        if (itemListEntity.getType().equals("video")) {
            feed = itemListEntity.getData().getCover().getFeed();
        } else {
            feed = itemListEntity.getData().getImage();
        }
        String userIcon = itemListEntity.getData().getAuthor().getIcon();
        String title = itemListEntity.getData().getTitle();
        String slogan = itemListEntity.getData().getSlogan();
        String category = itemListEntity.getData().getCategory();
        category = "#" + category + "  /  ";
        int duration = itemListEntity.getData().getDuration();
        int last = duration % 60;
        String stringLast;
        if (last <= 9) {
            stringLast = "0" + last;
        } else {
            stringLast = last + "";
        }
        String durationString;
        int minit = duration / 60;
        if (minit < 10) {
            durationString = "0" + minit;
        } else {
            durationString = "" + minit;
        }
        String stringTime = durationString + "' " + stringLast + '"';
        Context context = holder.itemView.getContext();
        int radius = context.getResources().getDimensionPixelSize(R.dimen.corner_radius);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(radius));
        Glide.with(context).load(feed).apply(requestOptions).into(videoListViewHolder.imageView);
        requestOptions = requestOptions.transform(new CircleCrop());
        Glide.with(context).load(userIcon).apply(requestOptions).into(videoListViewHolder.imageView_User);
        videoListViewHolder.tvSlogan.setText(slogan);
        videoListViewHolder.tvTitle.setText(title);
        videoListViewHolder.tvTime.setText(String.valueOf(category + stringTime));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_User;
        ImageView imageView;
        TextView tvTitle;
        TextView tvTime;
        TextView tvSlogan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageView_User = itemView.findViewById(R.id.iv_user);
            tvSlogan = itemView.findViewById(R.id.tv_slogan);
        }
    }
}
