package com.dev.rexhuang.eyes.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dev.rexhuang.eyes.R;
import com.dev.rexhuang.eyes.common.HttpApi;
import com.dev.rexhuang.eyes.model.HomePicEntity;
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
                                return ;
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
        for (int i = 0 ; i < videoListSize ; i ++){
            if (!itemListEntities.get(i).getType().equals(videoType)){
                itemListEntities.remove(i);
                i--;
                videoListSize--;
            }
        }
        videoListAdapter = new VideoListAdapter(MainActivity.this, itemListEntities);
    }

    private void setListener() {
        setListItemOnclick();
    }

    private void setListItemOnclick() {
        mOnItemClickListener = new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(MainActivity.this,VideoDetailActivity.class);
                HomePicEntity.IssueListEntity.ItemListEntity.DataEntity dataEntity =
                        itemListEntities.get(position).getData();
                intent.putExtra("playUrl",dataEntity.getPlayUrl());
                intent.putExtra("title",dataEntity.getTitle());
                intent.putExtra("image",dataEntity.getCover().getFeed());
                startActivity(intent);
//                Toast.makeText(MainActivity.this," position : " + position,Toast.LENGTH_SHORT).show();
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
    }

    public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder> {
        //        define field
        private Context context;
        private int size;
        //        define data
        private List<HomePicEntity.IssueListEntity.ItemListEntity> itemListEntities;

        public VideoListAdapter(Context context, List<HomePicEntity.IssueListEntity.ItemListEntity> itemListEntities) {
            this.context = context;
            this.itemListEntities = itemListEntities;
            this.size = itemListEntities.size();
            initData();
        }


        private void initData() {

        }

        @NonNull
        @Override
        public VideoListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View videoItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.videolist_item, viewGroup, false);
            VideoListViewHolder videoListViewHolder = new VideoListViewHolder(videoItem);
            return videoListViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull VideoListViewHolder videoListViewHolder, int i) {
            Log.e(TAG, "i : " + i);
            HomePicEntity.IssueListEntity.ItemListEntity itemListEntity = itemListEntities.get(i);
            String feed;
            if (itemListEntity.getType().equals("video")) {
                feed = itemListEntity.getData().getCover().getFeed();
            } else {
                feed = itemListEntity.getData().getImage();
            }
            String userIcon= itemListEntity.getData().getAuthor().getIcon();
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
            Glide.with(context).load(feed).apply(RequestOptions.bitmapTransform(new RoundedCorners(1000))).into(videoListViewHolder.imageView);
            Glide.with(context).load(userIcon).into(videoListViewHolder.imageView_User);
            videoListViewHolder.tvSlogan.setText(slogan);
            videoListViewHolder.tvTitle.setText(title);
            videoListViewHolder.tvTime.setText(String.valueOf(category + stringTime));

        }

        @Override
        public int getItemCount() {
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
    }
}
