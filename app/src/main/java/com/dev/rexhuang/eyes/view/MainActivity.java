package com.dev.rexhuang.eyes.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.rexhuang.eyes.R;
import com.dev.rexhuang.eyes.common.HttpApi;
import com.dev.rexhuang.eyes.model.HomePicEntity;
import com.dev.rexhuang.eyes.view.recyclerview.HorizontalSpace;
import com.dev.rexhuang.eyes.view.recyclerview.VideoListAdapter;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    //        define constant
    private static final String TAG = "MainActivity";
    private final String videoType = "video";
    private static final int UPDATE_UI = 100001;

    //    define field
    private boolean debug = false;
    private int videoListSize;
    private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private String[] weekdays = {"Sunday", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "Friday", "Saturday"};

    //    define view
    private RecyclerView videoList;
    private LinearLayoutManager linearLayoutManager;
    private ItemClickSupport mItemClickSupport;
    private ItemClickSupport.OnItemClickListener mOnItemClickListener;
    private ItemClickSupport.OnItemLongClickListener mOnItemLongClickListener;

    private RelativeLayout rl_title;
    private TextView text_date;

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
        videoListAdapter.setHeaderView(MainActivity.this, R.layout.video_header);
        videoListAdapter.setFooterView(MainActivity.this, R.layout.video_header);
    }

    private void setListener() {
        setLayoutListener();
        setListItemOnclick();
    }

    private void setLayoutListener() {
        rl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
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
                            itemListEntities.get(realPosition).getData();
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
        initLayout();
        initDate();
        initList();
    }

    private void initLayout() {
        rl_title = findViewById(R.id.rl_title);
    }

    private void initDate() {
        text_date = findViewById(R.id.text_date);
        Calendar calendar = Calendar.getInstance();
        int indexMonth = calendar.get(Calendar.MONTH);
        int indexWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        text_date.setText(weekdays[indexWeek].toUpperCase() + "," + months[indexMonth].toUpperCase() + " " + String.valueOf(day));
    }

    private void initList() {
        videoList = findViewById(R.id.videoList);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videoList.setLayoutManager(linearLayoutManager);
        mItemClickSupport = ItemClickSupport.addTo(videoList);
        videoList.addItemDecoration(new HorizontalSpace((int) getResources().getDimension(R.dimen.video_margin)));
    }


}
