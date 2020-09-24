package com.example.android_project;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

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

public class SettingFragment extends Fragment implements View.OnClickListener{
    View view;

    String ipv4address = "common.stac-know.tk";
    String portnum = "5000";

    String postUrl = "https://" + ipv4address + ":" + portnum + "/";
    request request;

    SeekBar seekSound;
    static SeekBar seekPitch;
    static SeekBar seekRate;
    AudioManager audioManager;
    EditText editDeviceid;
    Button btnLogout, btnDeviceid;
    TextView textEmail;

    String deviceid;
    String vo;
    int nMax, nCurrentVolumn;
    boolean flag = true;

    String UID;
    String Email;
    Context mContext;

    static float pitch=0.5f;
    static float speachRate=0.5f;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnDeviceid = view.findViewById(R.id.btnDeviceid);
        editDeviceid = view.findViewById(R.id.editDeviceid);
        textEmail = view.findViewById(R.id.textEmail);
        seekSound = view.findViewById(R.id.seekSound);
        seekPitch = view.findViewById(R.id.seekPitch);
        seekRate = view.findViewById(R.id.seekRate);

        seekPitch.setProgress(50);
        seekRate.setProgress(50);
        seekPitch.setMax(100);
        seekRate.setMax(100);

        pitch = seekPitch.getProgress();
        speachRate = seekRate.getProgress();



        btnDeviceid.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        request=new request(postUrl);
        request.status=5;

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        nMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        nCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekSound.setMax(nMax);
        seekSound.setProgress(nCurrentVolumn);
        seekSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pitch = i;
                Log.d("RATE",String.valueOf(pitch));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speachRate = i;
                Log.d("RATE",String.valueOf(speachRate));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.mContext = getContext();
        SharedPreferences sf = mContext.getSharedPreferences("sFile",mContext.MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        UID = sf.getString("text","");
        Log.d("TAG", UID+" Setting");

        SharedPreferences eSf = mContext.getSharedPreferences("eFile",mContext.MODE_PRIVATE);
        Email = eSf.getString("text","");
        Log.d("testDeviceid", Email+" email");
        textEmail.setText(Email);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("로그아웃 하시겠습니까?")
                        .setMessage("로그아웃시 자동로그인 기능이 사라집니다.")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sf = mContext.getSharedPreferences("eFile",mContext.MODE_PRIVATE);
                                SharedPreferences.Editor eEditor = sf.edit();
                                eEditor.clear();
                                SharedPreferences sfp = mContext.getSharedPreferences("pFile",mContext.MODE_PRIVATE);
                                SharedPreferences.Editor pEditor = sfp.edit();
                                pEditor.clear();
                                SharedPreferences sfu = mContext.getSharedPreferences("sFile", mContext.MODE_PRIVATE);
                                SharedPreferences.Editor uEditor = sfu.edit();
                                uEditor.clear();
                                eEditor.commit();
                                pEditor.commit();
                                uEditor.commit();
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("아니요",null)
                .show();
                break;
            case R.id.btnDeviceid:
                deviceid = editDeviceid.getText().toString();
                try {
                    request.link(deviceid, UID);
                    Log.d("testDeviceid",deviceid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
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

        public void link(String deviceid,String uid) throws JSONException {
            if(flag){
                flag=false;
                JSONObject json=new JSONObject();
                json.put("uid",uid);
                json.put("device",deviceid);
                mediaType = MediaType.parse("application/json");
                requestBody = RequestBody.create(json.toString(), mediaType);
                System.out.println(Usernum);
                postRequest(requestBody,this.URL+"device");
                Log.d("testDeviceid", deviceid+" "+uid);
            }
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
                    getActivity().runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if(status==5){
                        try {
                            link(deviceid, UID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(status==4){
                        review(response.body().string());
                    }

                }
            });

        }

    }
    void review (String str){

    }

    static public float getPitch(){
        return pitch;
    }
    static public float getSpeachRate(){
        return speachRate;
    }
}