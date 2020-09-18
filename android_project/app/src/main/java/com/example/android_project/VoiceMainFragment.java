package com.example.android_project;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class VoiceMainFragment extends Fragment {
    View view;

    Intent intent;
    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView textView;
    final int PERMISSION = 1;
    String data;
    String ipv4address = "54.210.63.142";
    String portnum = "5000";
    String postUrl = "http://" + ipv4address + ":" + portnum + "/";
    String voiceName;
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;
    private Button connect;
    private TextToSpeech tts;
    String Usernum;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_voice_main, container, false);

        Bundle bundle = getArguments();
        if(bundle !=null){
            voiceName = getArguments().getString("voiceName");
            Log.d("testasb",voiceName+"입니당");
        }

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }


        Toast.makeText(getContext(), Usernum, Toast.LENGTH_SHORT).show();
        textView = view.findViewById(R.id.sttResult);
        sttBtn = view.findViewById(R.id.sttStart);

        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getActivity().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        sttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecognizer=SpeechRecognizer.createSpeechRecognizer(getActivity().getApplicationContext());
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent);
                Log.d("test","1");
            }
        });

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
        tts.setPitch(1.0f);
        tts.setSpeechRate(2.0f);
        /*Set<String> a = new HashSet<>();
        a.add("female");
        Voice voice = new Voice("en-us-x-sfg#female_2-local", new Locale("en","KOREA"), 400,200, true, a);
        tts.setVoice(voice);
        Log.d("testasb",tts.getVoices().toString());*/
        //tts.getDefaultEngine();
        return view;
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getActivity().getApplicationContext(),"음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
            Log.d("test","2");
        }

        @Override
        public void onBeginningOfSpeech() {}

        @Override
        public void onRmsChanged(float rmsdB) {}

        @Override
        public void onBufferReceived(byte[] buffer) {}

        @Override
        public void onEndOfSpeech() {}

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }

            Toast.makeText(getActivity().getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            data = String.join("", matches);
            /*for(int i = 0; i < matches.size() ; i++){
                textView.setText(matches.get(i));
                data.a
            }*/
            textView.setText(data);
            try {
                postRequest(data,postUrl+"topic");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("test","3");
        }

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    };


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
                            tts.speak(str,TextToSpeech.QUEUE_FLUSH, null);
                            textView.setText(str);
                            Log.d("test","5");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("testasb","뿅");
                        }
                        }
                });


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