package com.dev.rexhuang.eyes.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.rexhuang.eyes.MainApplication;
import com.dev.rexhuang.eyes.R;
import com.dev.rexhuang.eyes.common.HttpApi;
import com.dev.rexhuang.eyes.model.ResponseEntity;
import com.dev.rexhuang.eyes.model.User;
import com.dev.rexhuang.http.IHttpClient;
import com.dev.rexhuang.http.IRequest;
import com.dev.rexhuang.http.impl.CommonRequest;
import com.dev.rexhuang.http.impl.CommonResponse;
import com.dev.rexhuang.http.impl.OkHttpClientImpl;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    //define field
    private static final String TAG = "LoginActivity";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final int SHOW_MESSAGE = 1000000;
    private static final int FINISH_SELF = 1000001;
    //define view
    private EditText username_et;
    private EditText password_et;
    private Button login_bt;
    private Button sign_bt;

    //define data
    private HashMap<String, String> params = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();
    private String loginResponseMessage;
    private String signResponseMessage;
    private Gson gson;
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handler = new MyHandler(LoginActivity.this);
        headers.put("X-Bmob-Application-Id", MainApplication.getApplicationId());
        headers.put("X-Bmob-REST-API-Key", MainApplication.getRestApiKey());
        headers.put("Content-Type", "application/json");
        initView();
        OkHttpClient client = new OkHttpClient();
        OkHttpClientImpl okHttpClient = new OkHttpClientImpl(client);

        gson = new Gson();
        sign_bt.setOnClickListener(v -> {
            if (getText()) {
                User user = new User();
                user.setUsername(params.get(USERNAME));
                user.setPassword(params.get(PASSWORD));
                String userJson = gson.toJson(user);
                CommonRequest commonRequest = new CommonRequest.Builder()
                        .setMethod(IRequest.POST)
                        .setUrl(HttpApi.USER_SIGN)
                        .setHeaders(headers)
                        .setBody(userJson)
                        .build();
                okHttpClient.setCallback(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        signResponseMessage = "注册失败,请检查网络链接!";
                        Message message = Message.obtain();
                        message.what = SHOW_MESSAGE;
                        message.arg1 = 1;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        CommonResponse commonResponse = new CommonResponse.Builder()
                                .setResponseCode(response.code())
                                .setResponseData(response.body().string())
                                .build();
                        Log.d(TAG,String.valueOf(commonResponse.getResponseCode()));
                        if (commonResponse.getResponseCode() >= 200 && commonResponse.getResponseCode() < 300) {
                            signResponseMessage = "注册成功！";
                        } else if (commonResponse.getResponseCode() >= 400){
                            ResponseEntity responseEntity = gson.fromJson(commonResponse.getResponseData(),ResponseEntity.class);
                            signResponseMessage = responseEntity.getError();
                        }
                        Message message = Message.obtain();
                        message.what = SHOW_MESSAGE;
                        message.arg1 = 1;
                        handler.sendMessage(message);
                    }
                });
                okHttpClient.request(commonRequest, IHttpClient.asynchronous);
            }
        });
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getText()) {
                    CommonRequest commonRequest = new CommonRequest.Builder()
                            .setMethod(IRequest.GET)
                            .setBody(params)
                            .setUrl(HttpApi.USER_LOGIN)
                            .setHeaders(headers)
                            .build();
                    okHttpClient.setCallback(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            loginResponseMessage = "登录失败,请检查网络链接!";
                            Message message = Message.obtain();
                            message.what = SHOW_MESSAGE;
                            message.arg1 = 1;
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            CommonResponse commonResponse = new CommonResponse.Builder()
                                    .setResponseCode(response.code())
                                    .setResponseData(response.body().string())
                                    .build();
                            Log.d(TAG,String.valueOf(commonResponse.getResponseCode()));
                            Message message = Message.obtain();
                            message.what = SHOW_MESSAGE;
                            if (commonResponse.getResponseCode() >= 200 && commonResponse.getResponseCode() < 300) {
                                loginResponseMessage = commonResponse.getResponseData();
                                message.arg1 = 2;
                                message.arg2 = 2;
                                handler.sendMessage(message);
                            } else if (commonResponse.getResponseCode() >= 400) {
                                ResponseEntity entity = gson.fromJson(commonResponse.getResponseData(), ResponseEntity.class);
                                loginResponseMessage = entity.getError();
                                message.arg1 = 2;
                                message.arg2 = 1;
                                handler.sendMessage(message);
                            }
                        }
                    });
                    okHttpClient.request(commonRequest,IHttpClient.asynchronous);
                }
            }
        });
    }

    private void initView() {
        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);
        login_bt = findViewById(R.id.login_bt);
        sign_bt = findViewById(R.id.sign_bt);
    }

    private boolean getText() {
        String username = username_et.getText().toString();
        String password = password_et.getText().toString();
        if (username != null && password != null && username.length() != 0 && password.length() != 0) {
            params.put("username", username);
            params.put("password", password);
        } else {
            Toast.makeText(MainApplication.getAppContext(), "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class MyHandler extends Handler{
        WeakReference<LoginActivity> weakReference;
        MyHandler(LoginActivity activity){
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = weakReference.get();
            if (activity != null){
                switch (msg.what){
                    case SHOW_MESSAGE:
                        if (msg.arg1 == 1) {
                            Toast.makeText(MainApplication.getAppContext(), signResponseMessage, Toast.LENGTH_SHORT).show();
                        } else if (msg.arg1 == 2){
                            Toast.makeText(MainApplication.getAppContext(), loginResponseMessage, Toast.LENGTH_SHORT).show();
                            if (msg.arg2 == 2) {
                                MyHandler.this.sendEmptyMessage(FINISH_SELF);
                            }
                        }
                        break;
                    case FINISH_SELF:
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                        break;
                }
            }
        }
    }
}
