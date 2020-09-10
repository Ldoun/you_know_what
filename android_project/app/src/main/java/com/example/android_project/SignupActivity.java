package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    String ipv4address = "54.210.63.142";
    String portnum = "5000";
    String postUrl = "http://" + ipv4address + ":" + portnum + "/";

    EditText editEmail, editPW;
    Button btnSignup, btnCheck;
    TextView textId, textPW;
    
    String strEmail, strPW;
    boolean bEmail =false, bPW=false, bChecked=false;
    int checked=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editEmail = findViewById(R.id.editEMail);
        editPW = findViewById(R.id.editPW);
        btnSignup = findViewById(R.id.btnSignup);
        btnCheck = findViewById(R.id.btnCheck);
        textId = findViewById(R.id.textId);
        textPW = findViewById(R.id.textPW);

        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                if (editEmail.getText().length() > 0 && editPW.getText().length() > 0) {
                    strEmail = editEmail.getText().toString();
                    strPW = editPW.getText().toString();
                    if(checked == 0){
                        textId.setText("중복 체크를 해주세요");
                        textId.setTextColor(Color.RED);
                    }
                    if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", strPW)) {
                        textPW.setText("비밀번호 형식을 지켜주세요");
                        textPW.setTextColor(Color.RED);
                    }
                    if(Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", strPW)){
                        textPW.setText("비밀번호 형식이 맞습니다.");
                        textPW.setTextColor(Color.LTGRAY);
                        bPW = true;
                    }
                    if(!bEmail){
                        textId.setText("Email 형식이 아닙니다.");
                        textId.setTextColor(Color.RED);
                    }
                    if(bEmail && bChecked && bPW){
                        postRequests(strEmail, strPW,postUrl);
                    }
                }
                else {
                    Toast.makeText(this, "email나 Password는 비워둘 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCheck:
                if (editEmail.getText().length() > 0) {
                    strEmail = editEmail.getText().toString();
                    strPW = editPW.getText().toString();
                    if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                        textId.setText("Email 형식이 아닙니다.");
                        textId.setTextColor(Color.RED);
                    }
                    else {
                        textId.setTextColor(Color.LTGRAY);
                        textId.setText("Email 형식이 맞습니다.");
                        bEmail = true;
                        postRequestEmailCheck(strEmail, postUrl);
                    }
                }
                else {
                    Toast.makeText(this, "email나 Password는 비워둘 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void postRequests(String strEmail, String strPW, String URL){

    }

    private void postRequestEmailCheck (String strEmail, String URL){

        checked = 1;
        if(checked ==0){
            textId.setText("이미 존재하는 Email입니다.");
            textId.setTextColor(Color.RED);
        }
        else{
            textId.setTextColor(Color.LTGRAY);
            textId.setText("중복 확인 완료");
            bChecked = true;
            btnCheck.setEnabled(false);
        }
    }
}