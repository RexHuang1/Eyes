package com.dev.rexhuang.eyes.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dev.rexhuang.eyes.R;
import com.dev.rexhuang.eyes.common.HttpApi;
import com.dev.rexhuang.eyes.model.HomePicEntity;
import com.google.android.exoplayer2.C;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //        define constant
    private static final String TAG = "MainActivity";
    private final String videoType = "video";
    private static final int UPDATE_UI = 100001;

    //    define field
    private boolean debug = false;
    private int videoListSize;

    //    define view
    private RecyclerView videoList;
    private LinearLayoutManager linearLayoutManager;
    private ItemClickSupport mItemClickSupport;
    private ItemClickSupport.OnItemClickListener mOnItemClickListener;
    private ItemClickSupport.OnItemLongClickListener mOnItemLongClickListener;

    //    define data
    private HomePicEntity homePicEntity;
    private VideoListAdapter videoListAdapter;
    private List<HomePicEntity.IssueListEntity.ItemListEntity> itemListEntities;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_UI:
                    videoList.setAdapter(videoListAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
        initData();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(HttpApi.DAILY);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.connect();
                    int status = connection.getResponseCode();
                    switch (status) {
                        case 200:
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                sb.append(line);
                            }
                            Log.e(TAG, sb.toString());
                            bufferedReader.close();
                            homePicEntity = new Gson().fromJson(sb.toString(), HomePicEntity.class);
                            if (homePicEntity == null) {
                                Log.e(TAG, "homePicEntity is a null object");
                                return;
                            }
                            filterData();
                            handler.sendEmptyMessage(UPDATE_UI);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void filterData() {
        itemListEntities = homePicEntity.getIssueList().get(0).getItemList();
        videoListSize = itemListEntities.size();
        for (int i = 0; i < videoListSize; i++) {
            if (!itemListEntities.get(i).getType().equals(videoType)) {
                itemListEntities.remove(i);
                i--;
                videoListSize--;
            }
        }
        videoListAdapter = new VideoListAdapter(MainActivity.this, itemListEntities);
        videoListAdapter.setHeaderView(MainActivity.this,R.layout.video_header);
        videoListAdapter.setFooterView(MainActivity.this,R.layout.video_header);
    }

    private void setListener() {
        setListItemOnclick();
    }

    private void setListItemOnclick() {
        mOnItemClickListener = new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                VideoListAdapter videoListAdapter = (VideoListAdapter) recyclerView.getAdapter();
                if (videoListAdapter.getmHeaderView() != null) {
                    Intent intent = new Intent(MainActivity.this, VideoDetailActivity.class);
                    int realPosition = videoListAdapter.getRealPosition(recyclerView.findViewHolderForAdapterPosition(position));
                    HomePicEntity.IssueListEntity.ItemListEntity.DataEntity dataEntity =
                            itemListEntities.get(position).getData();
                    intent.putExtra("playUrl", dataEntity.getPlayUrl());
                    intent.putExtra("title", dataEntity.getTitle());
                    intent.putExtra("image", dataEntity.getCover().getFeed());
                    startActivity(intent);
//                Toast.makeText(MainActivity.this," position : " + position,Toast.LENGTH_SHORT).show();
                }
            }
        };
        mOnItemLongClickListener = new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                return false;
            }
        };
        mItemClickSupport.setmOnItemClickListener(mOnItemClickListener);
        mItemClickSupport.setmOnItemLongClickListener(mOnItemLongClickListener);
    }

    private void initView() {
        initList();
    }

    private void initList() {
        videoList = findViewById(R.id.videoList);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videoList.setLayoutManager(linearLayoutManager);
        mItemClickSupport = ItemClickSupport.addTo(videoList);
        videoList.addItemDecoration(new HorizontalSpace((int) getResources().getDimension(R.dimen.video_margin)));
    }

    public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        // define constant
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
                Log.e(TAG,"getItemViewType == TYPE_FOOTER");
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
                return new HeaderViewHolder(mHeaderView);
            } else if (mFooterView != null && viewType == TYPE_FOOTER) {
                return new FooterViewHolder(mFooterView);
            } else {
                View videoItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.videolist_item, viewGroup, false);
                VideoListViewHolder videoListViewHolder = new VideoListViewHolder(videoItem);
                return videoListViewHolder;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
            Log.e(TAG,"position : " + i);
            //如果是header footer view就直接返回,不需要绑定数据
            if (getItemViewType(i) == TYPE_HEADER) {
                return;
            }
            if (getItemViewType(i) == TYPE_FOOTER) {
                return;
            }
            int realPosition = getRealPosition(holder);
            VideoListViewHolder videoListViewHolder = (VideoListViewHolder) holder;
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
            if (mHeaderView != null){
                size ++;
            }

            if (mFooterView != null){
                size ++;
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

    public class HorizontalSpace extends RecyclerView.ItemDecoration {

        private final int horizontalSpaceWidth;

        public HorizontalSpace(int horizontalSpaceWidth){

            this.horizontalSpaceWidth = horizontalSpaceWidth;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            if (position != parent.getAdapter().getItemCount()-1){
                outRect.right = horizontalSpaceWidth;
            }
        }
    }
}
