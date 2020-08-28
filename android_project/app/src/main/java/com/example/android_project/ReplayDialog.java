package com.example.android_project;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
                dismiss();
            }
        };

        clickDel = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("log.d", "Delete");
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

}
