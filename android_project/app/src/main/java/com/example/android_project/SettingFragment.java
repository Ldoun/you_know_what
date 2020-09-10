package com.example.android_project;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import android.speech.tts.Voice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Locale;

public class SettingFragment extends Fragment {
    View view;
    
    Spinner spinner;
    AdapterSpinner adapterSpinner;
    SeekBar seekBar;
    AudioManager audioManager;

    String vo;
    int nMax, nCurrentVolumn;
    setVoice setVoice;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        
        spinner = view.findViewById(R.id.spinner);

        seekBar = view.findViewById(R.id.seekSound);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        nMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        nCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBar.setMax(nMax);
        seekBar.setProgress(nCurrentVolumn);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        ArrayList<String> voice = new ArrayList<>();
        voice.add("dlehdns");
        voice.add("wjdalswn");
        voice.add("qkrtjdfo");
        voice.add("dkstnqls");
        voice.add("cjswndgh");
        
        adapterSpinner = new AdapterSpinner(getContext(), voice);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getContext(), (String)spinner.getItemAtPosition(position)+"이 선택되었습니다.", Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        vo = "dlehdns";
                        break;
                    case 1:
                        vo = "wjdalswn";
                        break;
                    case 2:
                        vo = "qkrtjdfo";
                        break;
                    case 3:
                        vo = "dkstnqls";
                        break;
                    case 4:
                        vo = "cjswndgh";
                        break;
                }
                setVoice.setVoice(vo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof setVoice){
            setVoice = (setVoice) context;
        } else {
            throw new RuntimeException(context.toString()+" must implement setVoice");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setVoice = null;
    }

    public interface setVoice {
        void setVoice(String voiceName);
    }
}