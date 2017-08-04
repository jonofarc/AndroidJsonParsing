package com.example.jonathanmaldonado.w3d4_ex01;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName()+"_TAG";
    private static final String BASE_URL ="https://randomuser.me/api";

    OkHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient.Builder().build();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Request request = new Request.Builder().url(BASE_URL).build();

        // thys is a Synchoronous request
        // this needs a separate thread
       // Response response = client.newCall(request).execute();

        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                      /*
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: "+ response.body().string());
                        }else{
                            Log.d(TAG, "onResponse: Application Error");
                        }
                        */
                        String resp= response.body().string();
                        try {
                            JSONObject result = new JSONObject(resp);
                            JSONArray results= result.getJSONArray("results");
                           // results.get(0)//object
                          

                            String myString=results.get(0).toString();
                            Log.d(TAG, "onResponse: "+ myString);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }
        );
    }
}
