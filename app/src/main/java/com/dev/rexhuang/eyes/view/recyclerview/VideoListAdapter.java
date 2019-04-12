package com.dev.rexhuang.eyes.view.recyclerview;

import android.content.Context;
import android.util.Log;
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
import com.dev.rexhuang.eyes.view.MainActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * *  created by RexHuang
 * *  on 2019/4/12
 */
public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // define constant
    private static final String TAG = "VideoListAdapter";
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;
    // define field
    private Context context;
    // define data
    private List<HomePicEntity.IssueListEntity.ItemListEntity> itemListEntities;
    // define view
    private View mHeaderView;
    private View mFooterView;

    public VideoListAdapter(Context context, List<HomePicEntity.IssueListEntity.ItemListEntity> itemListEntities) {
        this.context = context;
        this.itemListEntities = itemListEntities;
//            this.size = itemListEntities.size();
//            Log.e(TAG,"size 1 ： " + size);
        initData();
    }

    /**
     * Add header view
     *
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    /**
     * Add header view res
     *
     * @param context,layoutId
     */
    public void setHeaderView(Context context, int layoutId) {
        if (context == null && layoutId < 0) {
            return;
        }
        mHeaderView = LayoutInflater.from(context).inflate(layoutId, (ViewGroup) null);
        notifyItemInserted(0);
    }

    public View getmHeaderView() {
        return mHeaderView;
    }

    public View getmFooterView() {
        return mFooterView;
    }

    /**
     * Add footer view
     *
     * @param footerView
     */
    public void setFooterView(View footerView) {
        this.mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * Add footer view res
     *
     * @param context,layoutId
     */
    public void setFooterView(Context context, int layoutId) {
        if (context == null && layoutId < 0) {
            return;
        }
        this.mFooterView = LayoutInflater.from(context).inflate(layoutId, (ViewGroup) null);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        }

        if (mFooterView != null && position == getItemCount() - 1) {
            Log.e(TAG, "getItemViewType == TYPE_FOOTER");
            return TYPE_FOOTER;
        }

        return TYPE_NORMAL;
    }

    private void initData() {

    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new VideoListAdapter.HeaderViewHolder(mHeaderView);
        } else if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new VideoListAdapter.FooterViewHolder(mFooterView);
        } else {
            View videoItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.videolist_item, viewGroup, false);
            VideoListAdapter.VideoListViewHolder videoListViewHolder = new VideoListAdapter.VideoListViewHolder(videoItem);
            return videoListViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        Log.e(TAG, "position : " + i);
        //如果是header footer view就直接返回,不需要绑定数据
        if (getItemViewType(i) == TYPE_HEADER) {
            return;
        }
        if (getItemViewType(i) == TYPE_FOOTER) {
            return;
        }
        int realPosition = getRealPosition(holder);
        VideoListAdapter.VideoListViewHolder videoListViewHolder = (VideoListAdapter.VideoListViewHolder) holder;
        Log.e(TAG, "realPosition : " + realPosition);
        HomePicEntity.IssueListEntity.ItemListEntity itemListEntity = itemListEntities.get(realPosition);
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

    @Override
    public int getItemCount() {
        int size = itemListEntities.size();
        if (mHeaderView != null) {
            size++;
        }

        if (mFooterView != null) {
            size++;
        }
        return size;
    }

    public class VideoListViewHolder extends RecyclerView.ViewHolder {
        //            define view
        ImageView imageView_User;
        ImageView imageView;
        TextView tvTitle;
        TextView tvTime;
        TextView tvSlogan;

        public VideoListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageView_User = itemView.findViewById(R.id.iv_user);
            tvSlogan = itemView.findViewById(R.id.tv_slogan);
        }
    }

    /**
     * header view ViewHolder
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

    }

    /**
     * footer view ViewHolder
     */
    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}