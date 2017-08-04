package com.example.jonathanmaldonado.w3d4_ex01;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
        Request request = new Request.Builder().url("http://www.google.com").build();

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
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: "+ response.body().string());
                        }else{
                            Log.d(TAG, "onResponse: Application Error");
                        }
                    }
                }
        );
    }
}
