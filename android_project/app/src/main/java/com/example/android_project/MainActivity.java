package com.example.android_project;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    TextMainFragment fragmentTextMain;
    ReplayFragment fragmentReplay;
    VoiceMainFragment fragmentVoiceMain;

    Button btnReplay, btn;
    ToggleButton btnToggle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnReplay = findViewById(R.id.btnReplay);
        btn = findViewById(R.id.btn);
        btnToggle = findViewById(R.id.btnToggle);

        btnReplay.setOnClickListener(this);
        btn.setOnClickListener(this);
        btnToggle.setOnClickListener(this);

        fragmentTextMain = new TextMainFragment();
        fragmentReplay = new ReplayFragment();
        fragmentVoiceMain = new VoiceMainFragment();

        setFrag(1);

    }

    private void setFrag(int n) {
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();

        switch (n){
            case 0:
                transaction.replace(R.id.fragmentLayout, fragmentReplay);
                transaction.commit();
                break;
            case 1:
                transaction.replace(R.id.fragmentLayout, fragmentTextMain);
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.fragmentLayout, fragmentVoiceMain);
                transaction.commit();
                break;
            /*case 3:
                transaction.replace(R.id.fragmentLayout, fragmentReplay);
                transaction.commit();
                break;*/

        }
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
            case R.id.btn:
                setFrag(3);
                break;
        }
    }
}