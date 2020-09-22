package com.example.android_project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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


import java.util.HashMap;
import java.util.Map;

public class ReplayDialog extends Dialog {
    private TextView textCon, textDate;
    private Button btnSha, btnDel;
    private String strCon, strDate;

    private View.OnClickListener clickSha, clickDel;

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
                new ListViewAdapter().delItem();
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

}
