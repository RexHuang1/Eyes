package com.dev.rexhuang.eyes.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    define field
    private static final String TAG = "MainActivity";
    private boolean debug = false;
    private static final int UPDATE_UI = 100001;

    //    define view
    private RecyclerView videoList;
    //    define data
    private HomePicEntity homePicEntity;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_UI:
                    videoList.setAdapter(new VideoListAdapter(MainActivity.this, homePicEntity.getIssueList().get(0).getItemList()));
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
                            Log.e(TAG,sb.toString());
                            bufferedReader.close();
                            homePicEntity = new Gson().fromJson(sb.toString(), HomePicEntity.class);
                            if (homePicEntity == null){
                                Log.e(TAG,"homePicEntity is a null object");
                            }
                            handler.sendEmptyMessage(UPDATE_UI);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void setListener() {

    }

    private void initView() {
        initList();
    }

    private void initList() {
        videoList = findViewById(R.id.videoList);
        videoList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder> {
        //        define field
        private Context context;
        private int length = 3;
        //        define data
        private List<String> titles = new ArrayList<String>();
        private List<Bitmap> images = new ArrayList<Bitmap>();
        private List<String> times = new ArrayList<String>();
        private List<HomePicEntity.IssueListEntity.ItemListEntity> itemListEntities;

        public VideoListAdapter(Context context, List<HomePicEntity.IssueListEntity.ItemListEntity> itemListEntities) {
            this.context = context;
            this.itemListEntities = itemListEntities;
//            initData();
        }

        private void initData() {
            for (int i = 0; i < length; i++) {
                titles.add("无所谓");
                images.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_background));
                times.add("动画  / " + i + "'" + Math.random());
            }
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
            Log.e(TAG,"i : " + i);
            HomePicEntity.IssueListEntity.ItemListEntity itemListEntity = itemListEntities.get(i);
            String feed;
            if (itemListEntity.getType().equals("video")) {
                feed = itemListEntity.getData().getCover().getFeed();
            } else {
                feed = itemListEntity.getData().getImage();
            }
            String title = itemListEntity.getData().getTitle();
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
            Glide.with(context).load(feed).into(videoListViewHolder.imageView);
            videoListViewHolder.tvTitle.setText(title);
            videoListViewHolder.tvTime.setText(String.valueOf(category + stringTime));

        }

        @Override
        public int getItemCount() {
            return itemListEntities.size();
        }

        public class VideoListViewHolder extends RecyclerView.ViewHolder {
            //            define view
            ImageView imageView;
            TextView tvTitle;
            TextView tvTime;

            public VideoListViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.iv);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvTime = itemView.findViewById(R.id.tv_time);
            }
        }
    }
}
