package com.example.android_project;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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

public class ReplayFragment extends Fragment {
    View view;
    String str;
    String[] array;

    String ipv4address = "common.stac-know.tk";
    String portnum = "5000";
    String postUrl = "https://" + ipv4address + ":" + portnum + "/";
    request request;

    String strEmail;

    private ListView listView;
    private ListViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_replay, container, false);

        adapter = new ListViewAdapter();

        listView= view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        request = new request(postUrl);
        request.status=4;
        try {
            request.revivew_requests(strEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
    void tts(String str){

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
        /*String result;
        String getter(){
            return result;
        }*/
        public void requests(String msg) throws JSONException {

            JSONObject json=new JSONObject();
            json.put("email","1234@gmail.com");
            json.put("topic",msg);
            json.put("usernum",Usernum);
            mediaType = MediaType.parse("application/json");
            requestBody = RequestBody.create(json.toString(), mediaType);
            System.out.println(URL);
            postRequest(requestBody,this.URL+"topic");
            /*if(result!=null){
                tts.speak(result,TextToSpeech.QUEUE_FLUSH, null);
            }*/
        }
        void revivew_requests(String id) throws JSONException {
            System.out.println("함");
            JSONObject json=new JSONObject();
            json.put("id","27");
            mediaType = MediaType.parse("application/json");
            requestBody = RequestBody.create(json.toString(), mediaType);
            postRequest(requestBody,this.URL+"review");
            /*if(result!=null){
                tts.speak(result, TextToSpeech.QUEUE_FLUSH, null);
            }*/
        }

        public void requests(String password, String email) throws JSONException {
            JSONObject json=new JSONObject();
            json.put("pwd",password);
            json.put("email",email);

            mediaType = MediaType.parse("application/json");
            requestBody = RequestBody.create(json.toString(), mediaType);
            postRequest(requestBody,this.URL+"register");
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
                            Toast.makeText(ReplayFragment.this.getContext(), "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    if(status==0){
                        tts(response.body().string());
                    }else if(status==4){
                        review(response.body().string());
                    }

                }
            });

        }

    }

    void review (String string){
        //array = string.split(":");

        str = "안수빈,안수빈,안녕,하이,메롱";
        array = str.split(",");

//출력
        for(int i=0;i<array.length;i++) {
            adapter.addItem(array[i]);
        }

        adapter.addItem("트위터의 드립 문화를 일컫는 유행어. 글자 그대로 아무 말이나 마구 하는 모양새를 일컫는 말로, 아무렇게나 말을 내뱉기 편한 트위터의 특성을 그대로 표현하고 있다." +
                "아래의 코너가 방송된 이후로 트위터 내에서는");
        adapter.addItem("동 생성'의 경우국어 인식 정확도는 최악이라서 이 기능을 사");
        adapter.addItem("귀신이 고칼로리가 무슨 뜻이야? 몬소리야 아니 귀신이 왜 고칼로리야 귀신이 왜 고칼로라리는건데 구시신이 고칼로리라잖아 뭐라는거지 그 았잖아 속답에서");
        adapter.addItem("좌, 우익은 새의 양 날개와 같아서 세상을 끌고 가는 두  축이라 시대 상황에 따라 득세를 넘겨주고 받을 수 있다. 그리고 시대가 바뀜에 따라 진보는 더 새로운 진보에게 밀려나기도 한다. 그리고 보수는 대중의 지지를 잃지 않으려고 사회적 책임을 다하는 한편  끊임없는 자기혁신을 하면서 생명력을 유지해가고 있다. 300년 역사를 자랑하는 영국의 경우 처럼." +
                "그래서 좌우 논쟁은 일단 접어두고, 이번 사건이 갖고 있는 문제의 본질을 한번 따져보자. 내 색깔부터 밝힌다. 나는 테스트 해보나 마나 중도 보수 우파다. 우파지만 때로는 사안별로 좌파의 목소리에도 귀를 기울이는 유연성을 갖고 있다. 노무현 대통령이 한참 욕 먹고 있을 때도 그의 말에 귀울였었고, 그를 싫어하던 아내의 손을 이끌고 봉하마을도 다녀왔다." +
                "본론으로 들어가서 이번 사건의 본질을 따지자면 나는 '기준과 선의 문제다' 라고 감히 단언한다. 군에서는 줄을 세우기 전에 기준부터 정한다. 그럼 기준되는 병사가 오른 손을 쳐들고 '기준!'하고 외치면 그에 따라 오와 열을 맞추어 정열을 하게 되는 것이다. 사회는 기준이 서야 질서가 유지되며 기준이 무너지면 질서가 무너지게 되어 있다. 기준은 기본이며 근본이며 원칙이며 윤리이고 법이기도 하다." +
                "줄자에는 한쪽에 cm가 표기되어 있고, 다른 쪽에는 inch가 표기되어 있는데 경우에 따라 cm로 재다가 inch로 잰다면 어떻게 될까? 형평의 원리에 어긋나기  때문에 난리가 날 것이다. 내가 볼 때 지금이 꼭 그렇다. 국회, 언론, 검찰이 내부자가 되어 이중잣대를 들이밀며 세상을 재단하고 있는 것이다. 국회의원이 앞장서면 언론은 선동하고 검찰이 행동대장 역할을 하고 있는 꼴이다. 평정심을 잃은 정도가 아니라 제정신이 아닌 것 같다.");

        adapter.notifyDataSetChanged();
    }
}