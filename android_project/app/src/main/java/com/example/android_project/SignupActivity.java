package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    String ipv4address = "common.stac-know.tk";
    String portnum = "5000";

    String postUrl = "https://" + ipv4address + ":" + portnum + "/";

    EditText editEmail, editPW;
    Button btnSignup, btnCheck;
    TextView textId, textPW;

    String strEmail, strPW;
    boolean bEmail =false, bPW=false, bChecked=false;
    int checked=0;
    request request;

    ConstraintLayout con1;
    LinearLayout con2;
    Animation animation;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editEmail = findViewById(R.id.editEMail);
        editPW = findViewById(R.id.editPW);
        btnSignup = findViewById(R.id.btnSignup);
        btnCheck = findViewById(R.id.btnCheck);
        textId = findViewById(R.id.textId);
        textPW = findViewById(R.id.textPW);
        con1 = findViewById(R.id.con1);
        con2 = findViewById(R.id.con2);
        img = findViewById(R.id.sign_suc);

        con1.setVisibility(View.VISIBLE);
        con2.setVisibility(View.INVISIBLE);

        btnSignup.setOnClickListener(this);
        btnCheck.setOnClickListener(this);

        request=new request(postUrl);
        request.status=5;

    }

    @Override
    public void onClick(View view) {
        strEmail = editEmail.getText().toString();
        strPW = editPW.getText().toString();
        switch (view.getId()) {
            case R.id.btnSignup:
                if (editEmail.getText().length() > 0 && editPW.getText().length() > 0) {
                    if(checked == 0){
                        textId.setText("중복 체크를 해주세요");
                        textId.setTextColor(Color.RED);
                    }
                    if(Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,12}$", strPW)){
                        textPW.setText("비밀번호 형식이 맞습니다.");
                        textPW.setTextColor(Color.LTGRAY);
                        bPW = true;
                    } else {
                        textPW.setText("비밀번호 형식이 아닙니다. 특수문자 포함 숫자");
                    }
                    if(!bEmail){
                        textId.setText("Email 형식이 아닙니다.");
                        textId.setTextColor(Color.RED);
                    }
                    if(bEmail && bChecked && bPW){
                        try {
                            request.requests(strPW, strEmail);
                            Toast.makeText(this, "통과", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    Toast.makeText(this, "email나 Password는 비워둘 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCheck:
                bEmail = true;
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    textId.setText("Email 형식이 아닙니다.");
                    textId.setTextColor(Color.RED);
                }
                else {
                    textId.setTextColor(Color.LTGRAY);
                    textId.setText("Email 형식이 맞습니다.");
                    bEmail = true;
                    try {
                        request.check(strEmail);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    checked =1;
                        /*if(checked ==0){
                            textId.setText("이미 존재하는 Email입니다.");
                            textId.setTextColor(Color.RED);
                        }
                        else{
                            textId.setTextColor(Color.LTGRAY);
                            textId.setText("중복 확인 완료");
                            bChecked = true;
                            btnCheck.setEnabled(false);
                        }*/
                }
                break;
        }
    }

    void tts(String str){

    }

    void email_check(String msg){
        if(msg.equals("fail")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textId.setText("이미 존재하는 아이디입니다.");
                    textId.setTextColor(Color.RED);
                }
            });
        }
        else if(msg.equals("success")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textId.setTextColor(Color.LTGRAY);
                    textId.setText("중복 확인 완료");
                    bChecked = true;
                    btnCheck.setEnabled(false);
                    request.status=1;
                    Log.d("testCh","성공임");
                }
            });
        }
    }
    void registerer(String str){
        // UID 저장
        SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("text",str);
        editor.commit();

        // email 저장
        SharedPreferences shaEmail = getSharedPreferences("eFile", MODE_PRIVATE);
        SharedPreferences.Editor eEditor = shaEmail.edit();
        eEditor.putString("text",editEmail.getText().toString());
        eEditor.commit();

        // PW 저장
        SharedPreferences shaPW = getSharedPreferences("pFile",MODE_PRIVATE);
        SharedPreferences.Editor pEditor = shaPW.edit();
        pEditor.putString("text", editPW.getText().toString());
        pEditor.commit();

        System.out.println(str+" register");
        Log.d("TAG",str);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                con1.setVisibility(View.GONE);
                con2.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sign_suc);
                img.setAnimation(animation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            }
        });

    }
    public class request{
        String Usernum;
        RequestBody requestBody;
        MediaType mediaType;
        request(String URL){
            this.URL=URL;
            System.out.println(URL);
        }
        int status;
        String URL;
        /*String result;
        String getter(){
            return result;
        }*/
        public void check(String email) throws JSONException {
            JSONObject json=new JSONObject();
            json.put("email",email);
            mediaType = MediaType.parse("application/json");
            requestBody = RequestBody.create(json.toString(), mediaType);
            postRequest(requestBody,this.URL+"email");
        }
        public void requests(String msg) throws JSONException {

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

        public void requests(String password, String email) throws JSONException {
            JSONObject json=new JSONObject();
            json.put("pwd",password);
            json.put("email",email);

            mediaType = MediaType.parse("application/json");
            requestBody = RequestBody.create(json.toString(), mediaType);
            postRequest(requestBody,this.URL+"register");
        }

        private void postRequest(RequestBody requestBody, String url) throws JSONException {
            //RequestBody requestBody = buildRequestBody(message);
            System.out.println(url);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request
                    .Builder()
                    .post(requestBody)
                    .url(url)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignupActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    if(status==0){
                        tts(response.body().string());
                    }
                    else if(status==1){
                        registerer(response.body().string());
                    }else if(status==4){
                        review(response.body().string());
                    }
                    else if(status==5){
                        email_check(response.body().string());
                        Log.d("testCh","check");
                    }

                }
            });

        }

    }

    void review (String str){

    }
}





