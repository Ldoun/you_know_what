package com.example.android_project;

import android.app.Fragment;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
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

import static android.content.Context.MODE_PRIVATE;
import com.example.android_project.ListViewItem;

public class ReplayFragment extends Fragment{
    View view;
    String str;
    String[] array;

    String ipv4address = "common.stac-know.tk";
    String portnum = "5000";
    String postUrl = "https://" + ipv4address + ":" + portnum + "/";
    request request;

    String strEmail;

    String UID;
    int posi;

    private ListView listView;
    private ListViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_replay, container, false);

        adapter = new ListViewAdapter();

        //여기 login id num
        SharedPreferences sf = this.getActivity().getSharedPreferences("sFile",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        UID = sf.getString("text","");

        listView= view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                posi = position;
                //Log.d("testDel","선택됨"+posi);
                System.out.println("선택됨: "+posi);
                adapter.setPosi(adapterView.getItemAtPosition(posi).toString());
                Log.d("TIP", adapterView.getItemAtPosition(posi).toString()+" setPosi()");
            }
        });

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

    public int getPosi(){
        return posi;
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
            json.put("id",UID);
            System.out.println("user:"+UID);
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
        array = string.split(":");

        //str = "안수빈,안수빈,안녕,하이,메롱";
        //array = str.split(",");

//출력
        for (String s : array) {
            adapter.addItem(s);
        }  
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }
}