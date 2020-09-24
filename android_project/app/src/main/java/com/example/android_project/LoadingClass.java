package com.example.android_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadingClass extends Activity {

    String URL = "https://common.stac-know.tk:5000/";

    ImageView imageView;
    Context mContext;
    request request = new request(URL);
    String email,pw;

    boolean login=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lodingclass);

        request.status = 2;

        imageView = findViewById(R.id.imageView);

        Animation appear;
        appear = AnimationUtils.loadAnimation(this, R.anim.fadein);
        imageView.setAnimation(appear);

        this.mContext = getApplicationContext();
        SharedPreferences sf = mContext.getSharedPreferences("eFile",mContext.MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        email = sf.getString("text",null);

        SharedPreferences sfp = mContext.getSharedPreferences("pFile",mContext.MODE_PRIVATE);
        pw = sfp.getString("text",null);

        if(email != null && pw !=null){
            try {
                Toast.makeText(mContext, email+"로 자동 로그인 중", Toast.LENGTH_SHORT).show();
                request.requests(pw,email);
                login = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(login==false) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 3000);
    }

    void login(String str){
        Log.d("from_server",str);

        int num;
        try {
            num=Integer.parseInt(str);
        }catch (Exception e){
            System.out.println("로그인 실패");
            return ;
        }


        if(num==0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoadingClass.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("text", str);
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

    }

    public class request{

        request(String URL){
            this.URL=URL;
            System.out.println(URL);
        }

        MediaType mediaType;
        RequestBody requestBody;
        int Usernum;
        int status;


        String URL;

        public void requests(String password, String email) throws JSONException {
            JSONObject json = new JSONObject();
            json.put("pwd", password);
            json.put("email", email);
            mediaType = MediaType.parse("application/json");
            requestBody = RequestBody.create(json.toString(), mediaType);
            postRequest(requestBody,this.URL+"login");
            Log.d("LOGIN","requests");
        }
        private void postRequest(RequestBody requestBody, String url) throws JSONException {
            //RequestBody requestBody = buildRequestBody(message);
            Log.d("LOGIN","postRequest");
            System.out.println(url);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request
                    .Builder()
                    .post(requestBody)
                    .url(url)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    Log.d("sun","1");

                    if(status==2){
                        Log.d("sun","2");
                        login(response.body().string());
                        Log.d("LOGIN","login");
                    }
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_LONG).show();

                                tts.speak(response.body().string(),TextToSpeech.QUEUE_FLUSH, null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });*/
                }

                @Override
                public void onFailure(final Call call, final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoadingClass.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });

                }
            });

        }

    }
}
