package com.example.you_know_what;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnMain, btnReview;
    FragmentManager fm;
    FragmentTransaction tran;
    FragmentMain frag1;
    FragmentReview frag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMain = findViewById(R.id.btnMain);
        btnReview = findViewById(R.id.btnReview);

        btnReview.setOnClickListener(this);
        btnMain.setOnClickListener(this);

        frag1 = new FragmentMain();
        frag2 = new FragmentReview();
        setFrag(0);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMain:
                setFrag(0);
                break;
            case R.id.btnReview:
                setFrag(1);
                break;
        }
    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();

        switch (n){
            case 0:
                tran.replace(R.id.main_frame, frag1);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.main_frame, frag2);
                tran.commit();
                break;
        }
    }
}