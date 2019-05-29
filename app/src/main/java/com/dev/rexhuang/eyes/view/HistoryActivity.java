package com.dev.rexhuang.eyes.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.dev.rexhuang.eyes.R;
import com.dev.rexhuang.eyes.common.HttpApi;
import com.dev.rexhuang.eyes.model.HomePicEntity;
import com.dev.rexhuang.eyes.view.recyclerview.DelegationAdapter;
import com.dev.rexhuang.eyes.view.recyclerview.HistoryAdapterDelegate;
import com.dev.rexhuang.eyes.view.recyclerview.OnPortableScrollerListener;
import com.dev.rexhuang.eyes.view.recyclerview.VerticalSpace;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryActivity extends AppCompatActivity {

    // define constant
    private static final String TAG = "HistoryActivity";
    private final String videoType = "video";
    private static final int INIT_UI = 100001;
    private static final int UPDATE_UI = 100002;

    // define field
    private int videoListSize;
    private boolean isLoading = false;
    private Handler handler;

    // define data
    private HomePicEntity homePicEntity;
    private List<HomePicEntity.IssueListEntity.ItemListEntity> itemListEntities;
    private String nextUrl;

    // define view
    private RecyclerView videoList;
    private LinearLayoutManager linearLayoutManager;
    private DelegationAdapter delegationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        setListener();
        initData();
    }

    private void initData() {
        handler = new UiHandler(HistoryActivity.this);
        new Thread(() -> {
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
                        handler.sendEmptyMessage(INIT_UI);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
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
        nextUrl = homePicEntity.getNextPageUrl();
        Log.e(TAG, "nextUrl ： " + nextUrl);
    }

    private void setListener() {
        setListItemOnclick();
        setVideoListener();
    }

    private void setListItemOnclick() {
        delegationAdapter.setOnItemClickListener(new DelegationAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(View itemView, int position) {

            }

            @Override
            public void onItemClick(View itemView,int position) {
                position -= delegationAdapter   .getHeaderCount();
                Intent intent = new Intent(HistoryActivity.this, VideoDetailActivity.class);
                HomePicEntity.IssueListEntity.ItemListEntity.DataEntity dataEntity =
                        itemListEntities.get(position).getData();
                intent.putExtra("playUrl", dataEntity.getPlayUrl());
                intent.putExtra("title", dataEntity.getTitle());
                intent.putExtra("image", dataEntity.getCover().getFeed());
                startActivity(intent);
            }
        });
    }

    private void setVideoListener() {
//        videoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                if (!isLoading) {
//                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1) {
//                        loadmore();
//                    }
//                }
//            }
//        });
        videoList.addOnScrollListener(new OnPortableScrollerListener() {
            @Override
            public void onLoadMore() {
                if (!isLoading){
                    isLoading = true;
                    loadMore();
                }
            }
        });

    }

    private void loadMore() {
        download(nextUrl);
    }

    private void download(String nextPageUrl) {
        new Thread(() -> {
            URL url = null;
            try {
                url = new URL(nextPageUrl);
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
                        HomePicEntity homePicEntity = new Gson().fromJson(sb.toString(), HomePicEntity.class);
                        if (homePicEntity == null) {
                            Log.e(TAG, "homePicEntity is a null object");
                            return;
                        }
                        updateData(homePicEntity);
                        handler.sendEmptyMessage(UPDATE_UI);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateData(HomePicEntity homePicEntity) {
        List<HomePicEntity.IssueListEntity.ItemListEntity> itemListEntities = homePicEntity.getIssueList().get(0).getItemList();
        int size = itemListEntities.size();
        for (int i = 0; i < size; i++) {
            if (!itemListEntities.get(i).getType().equals(videoType)) {
                itemListEntities.remove(i);
                i--;
                size--;
            }
        }
        nextUrl = homePicEntity.getNextPageUrl();
        Log.e(TAG, "nextUrl ： " + nextUrl);
        HistoryActivity.this.itemListEntities.addAll(itemListEntities);
    }

    private void initView() {
        initList();
    }

    @SuppressLint("WrongConstant")
    private void initList() {
        videoList = findViewById(R.id.videoList);
        linearLayoutManager = new LinearLayoutManager(HistoryActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        videoList.setLayoutManager(linearLayoutManager);
        delegationAdapter = new DelegationAdapter();
        delegationAdapter.addDelegate(new HistoryAdapterDelegate());
        videoList.setAdapter(delegationAdapter);
        videoList.addItemDecoration(new VerticalSpace((int) getResources().getDimension(R.dimen.video_margin)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private static class UiHandler extends Handler{
        private WeakReference<HistoryActivity> activityWeakReference;

        public UiHandler(HistoryActivity activity){
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HistoryActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case INIT_UI:
                    activity.delegationAdapter.setDataItems(activity.itemListEntities);
                    break;
                case UPDATE_UI:
                    activity.delegationAdapter.notifyDataSetChanged();
                    activity.isLoading = false;
                    break;
            }
        }
    }
}
