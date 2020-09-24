package com.example.android_project;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.android_project.SettingFragment;

public class TextMainFragment extends Fragment implements View.OnClickListener{
    View view;

    SettingFragment settingFragment;

    Button button;
    EditText editText;
    String data;
    String ipv4address = "common.stac-know.tk";
    String portnum = "5000";
    String postUrl = "https://" + ipv4address + ":" + portnum + "/";
    TextToSpeech tts;

    String Usernum;
    Context mContext;

    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_text_main, container, false);

        editText = view.findViewById(R.id.editText);
        button = view.findViewById(R.id.button);

        button.setOnClickListener(this);

        this.mContext = getContext();
        SharedPreferences sf = mContext.getSharedPreferences("sFile",mContext.MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        Usernum = sf.getString("text","");

        tts=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.KOREAN);
                }
                else{
                    Toast.makeText(getContext(), "speech 객체 초기화 중 에러발생", Toast.LENGTH_SHORT).show();
                }
            }
        },"com.google.android.tts");
        tts.setPitch(settingFragment.getPitch()/50);
        tts.setSpeechRate(settingFragment.getSpeachRate()/50);

        Log.d("RATE", String.valueOf(settingFragment.getPitch())+"pitch");
        Log.d("RATE", String.valueOf(settingFragment.getSpeachRate())+"rate");

        return view;
    }



    @Override
    public void onClick(View view) {
        data = editText.getText().toString();
        try {
            postRequest(data,postUrl+"topic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private RequestBody buildRequestBody(String msg) throws JSONException {
        postBodyString = msg;
        JSONObject json=new JSONObject();
        json.put("usernum",Usernum);
        json.put("topic",msg);
        mediaType = MediaType.parse("application/json");
        requestBody = RequestBody.create(json.toString(), mediaType);
        return requestBody;
    }

    void tts(final String str){
        tts.speak(str,TextToSpeech.QUEUE_FLUSH,null);
        Log.d("tts_true",str);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText(str);
            }
        });
    }

    private void postRequest(String message, String URL) throws JSONException {
        RequestBody requestBody = buildRequestBody(message);
        // OkHttpClient 객체 정의
        OkHttpClient okHttpClient = new OkHttpClient();
        // Request 객체 정의
        final Request request = new Request
                .Builder()
                .post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                /*VoiceMainFragment.this.runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("testasb",e.getMessage());
                        call.cancel();
                    }
                });*/

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                tts(response.body().string());
              /*getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        try {
                            //Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_LONG).show();
                            String str = response.body().string();
                            tts.speak(str,TextToSpeech.QUEUE_FLUSH, null);
                            textView.setText(str);
                            Log.d("test","5");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("testasb","뿅");
                        }
                        }
                });*/

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}