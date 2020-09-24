package com.example.android_project;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    TextMainFragment fragmentTextMain;
    ReplayFragment fragmentReplay;
    VoiceMainFragment fragmentVoiceMain;
    SettingFragment fragmentSetting;

    Button btnReplay, btnReport, btnSetting;
    ToggleButton btnToggle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnReplay = findViewById(R.id.btnReplay);
        btnReport = findViewById(R.id.btnReport);
        btnToggle = findViewById(R.id.btnToggle);
        btnSetting = findViewById(R.id.btn_setting);

        btnReplay.setOnClickListener(this);
        btnReport.setOnClickListener(this);
        btnToggle.setOnClickListener(this);
        btnSetting.setOnClickListener(this);

        fragmentTextMain = new TextMainFragment();
        fragmentReplay = new ReplayFragment();
        fragmentVoiceMain = new VoiceMainFragment();
        fragmentSetting = new SettingFragment();

        setFrag(2);


    }

    private void setFrag(int n) {
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();

        switch (n){
            case 0:
                transaction.replace(R.id.fragmentLayout, fragmentReplay);
                break;
            case 1:
                transaction.replace(R.id.fragmentLayout, fragmentTextMain);
                break;
            case 2:
                transaction.replace(R.id.fragmentLayout, fragmentVoiceMain);
                break;
            case 4:
                transaction.replace(R.id.fragmentLayout, fragmentSetting);
                break;
        }

        transaction.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReplay:
                setFrag(0);
                break;
            case R.id.btnToggle:
                if(btnToggle.isChecked()){
                    setFrag(1);
                    break;
                }else{
                    setFrag(2);
                    break;
                }
            case R.id.btnReport:
                ReportDialog dialog = new ReportDialog();
                dialog.show(fragmentManager, "fragment_dialog_test");

                break;
            case R.id.btn_setting:
                setFrag(4);
                break;
        }
    }
}