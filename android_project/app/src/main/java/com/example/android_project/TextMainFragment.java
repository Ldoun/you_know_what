package com.example.android_project;

import android.app.Fragment;
import android.content.Intent;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TextMainFragment extends Fragment implements View.OnClickListener{
    View view;

    Button button;
    EditText editText;
    String data;
    String ipv4address = "54.210.63.142";
    String portnum = "5000";
    String postUrl = "http://" + ipv4address + ":" + portnum + "/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_text_main, container, false);

        editText = view.findViewById(R.id.editText);
        button = view.findViewById(R.id.button);

        button.setOnClickListener(this);

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
        json.put("email","1234@gmail.com");
        json.put("topic",msg);
        mediaType = MediaType.parse("application/json");
        requestBody = RequestBody.create(json.toString(), mediaType);
        return requestBody;
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
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("testasb",e.getMessage());
                        call.cancel();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        try {
                            //Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_LONG).show();
                            String str = response.body().string();
                            editText.setHint(str);
                            editText.setText("");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("testasb","뿅");
                        }
                    }
                });


            }
        });

    }
}