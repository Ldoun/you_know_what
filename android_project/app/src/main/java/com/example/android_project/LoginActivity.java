package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editEMail, editPW;
    Button btnSignup;
    TextView textSignUp, textPW;
    String URL = "https://common.stac-know.tk:5000/";

    RequestBody requestBody;
    request request = new request(URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEMail = findViewById(R.id.editEMail);
        editPW = findViewById(R.id.editPW);
        btnSignup = findViewById(R.id.btnSignup);
        textSignUp = findViewById(R.id.textSignUp);
        textPW = findViewById(R.id.textPW);
        request.status = 2;


        btnSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    request.requests(editPW.getText().toString(),editEMail.getText().toString());
                    Log.d("LOGIN","click");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class); //회원가입 코드 넣기
                startActivity(intent);
            }
        });

        textPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class); //어디로 가는지 알아보자
                startActivity(intent);
            }
        });



    }
    void tts(String Msg){

    }

    void registerer(String msg){

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
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
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
        /*String result;
        String getter(){
            return result;
        }*/
        void requests(String msg) throws JSONException {
            JSONObject json=new JSONObject();
            json.put("email","1234@gmail.com");
            json.put("topic",msg);
            json.put("usernum",Usernum);
            mediaType = MediaType.parse("application/json");
            requestBody = RequestBody.create(json.toString(), mediaType);
            System.out.println(URL);
            postRequest(requestBody,this.URL+"topic");
            /*if(result!=null){
                tts.speak(result,TextToSpeech.QUEUE_FLUSH, null);
            }*/
        }

        public void requests(String id, String password, String email) throws JSONException {
            JSONObject json=new JSONObject();
            json.put("id",id);
            json.put("pwd",password);
            json.put("email",email);

            mediaType = MediaType.parse("application/json");
            requestBody = RequestBody.create(json.toString(), mediaType);
            postRequest(requestBody,this.URL+"register");
        }

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
                    if(status==0){
                        tts(response.body().string());
                    }
                    else if(status==1){
                        registerer(response.body().string());
                    }
                    else if(status==2){
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
                            Toast.makeText(LoginActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });

                }
            });

        }

    }
}