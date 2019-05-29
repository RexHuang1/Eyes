package com.dev.rexhuang.eyes.view;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.rexhuang.eyes.MainApplication;
import com.dev.rexhuang.eyes.R;
import com.dev.rexhuang.eyes.common.HttpApi;
import com.dev.rexhuang.eyes.model.User;
import com.dev.rexhuang.eyes.util.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    //define field
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    //define view
    private EditText username_et;
    private EditText password_et;
    private Button login_bt;
    private Button sign_bt;

    //define data
    private HashMap<String, String> params = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        OkHttpClient client = new OkHttpClient();
        sign_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getText()) {
                    User user = new User();
                    user.setUsername(params.get(USERNAME));
                    user.setPassword(params.get(PASSWORD));
                    String userJson = new Gson().toJson(user);
                    RequestBody userBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), userJson);
                    Headers headers = new Headers.Builder()
                            .add("X-Bmob-Application-Id", MainApplication.getApplicationId())
                            .add("X-Bmob-REST-API-Key", MainApplication.getRestApiKey())
                            .add("Content-Type", "application/json").build();
                    Request request = new Request.Builder()
                            .url(HttpApi.USER_SIGN)
                            .post(userBody)
                            .headers(headers)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainApplication.getAppContext(), "注册失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responeseMessage;
                            if (response.code() == 200) {
                                responeseMessage = "注册成功";
                            } else {
                                responeseMessage = response.body().string();
                            }
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainApplication.getAppContext(), responeseMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }
        });
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getText()) {
                    String url = Utils.attachHttpGetParams(HttpApi.USER_LOGIN, params);
                    Headers headers = new Headers.Builder()
                            .add("X-Bmob-Application-Id", MainApplication.getApplicationId())
                            .add("X-Bmob-REST-API-Key", MainApplication.getRestApiKey())
                            .add("Content-Type", "application/json").build();
                    Request request = new Request.Builder()
                            .url(url)
                            .headers(headers)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responeseMessage;
                            if (response.code() == 200) {
                                responeseMessage = response.body().string();
                            } else {
                                responeseMessage = response.body().string();
                            }
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainApplication.getAppContext(), responeseMessage, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
//                                    Log.d("rexHuang",response.stat)
                                }
                            });
                        }
                    });
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
}
