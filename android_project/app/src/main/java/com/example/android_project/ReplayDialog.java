package com.example.android_project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.android_project.ListViewAdapter;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import com.example.android_project.ListViewAdapter;

public class ReplayDialog extends Dialog {
    private TextView textCon, textDate;
    private Button btnSha, btnDel;
    private String strCon, strDate;

    private View.OnClickListener clickSha, clickDel;

    String ipv4address = "common.stac-know.tk";
    String portnum = "5000";
    String postUrl = "https://" + ipv4address + ":" + portnum + "/";
    String UID;
    request request;

    ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.replay_dialog);

        textCon = findViewById(R.id.textCon);
        textDate = findViewById(R.id.textDate);
        btnSha = findViewById(R.id.btnSha);
        btnDel = findViewById(R.id.btnDel);

        textCon.setText(strCon);
        textDate.setText(strDate);

        listViewAdapter = new ListViewAdapter();

        request = new request(postUrl);

        SharedPreferences sf = getContext().getSharedPreferences("sFile",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        UID = sf.getString("text","");

        clickSha = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("log.d", "share");
                /*intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_TEXT, "그거알아:\n"+strCon);
                getContext().startActivity(intent);*/
                kakaolink();
                dismiss();
            }
        };

        clickDel = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("log.d", "Delete");

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("다시보기 지우기")
                        .setMessage("동일한 상식이 모두 제거됩니다.")
                        .setPositiveButton("삭제",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //요청 코드
                                        try {
                                            request.delete(listViewAdapter.getPosi(),UID);
                                            Log.d("TIP", listViewAdapter.getPosi()+"delete(TIP,UID)");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                .setNegativeButton("아니요",null)
                        .setIcon(R.drawable.attention)
                        .show();

                dismiss();
            }
        };

        btnSha.setOnClickListener(clickSha);
        btnDel.setOnClickListener(clickDel);

    }

    public ReplayDialog(Context context, String strCon, String strDate) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.strCon = strCon;
        this.strDate = strDate;

    }
    public void kakaolink() {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("그거알아? (YouKnowWhat)",
                        "https://i.ibb.co/nLMnRBt/image.png",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption(strCon)
                        .build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .setSocial(SocialObject.newBuilder()
                        .setLikeCount(1226)
                .build())
                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");


        KakaoLinkService.getInstance().sendDefault(getContext(), params, new ResponseCallback <KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {}

            @Override
            public void onSuccess(KakaoLinkResponse result) {
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
        String URL;

        public void delete(String tip,String uid) throws JSONException {
            System.out.println(tip);
            Log.d("TIP", tip);
            JSONObject json=new JSONObject();
            json.put("tip",tip);
            json.put("uid",uid);
            mediaType = MediaType.parse("application/json");
            requestBody = RequestBody.create(json.toString(), mediaType);
            postRequest(requestBody,this.URL+"delete");
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
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                }

                @Override
                public void onFailure(final Call call, final IOException e) {
                        }
                    });
        }

    }

}
