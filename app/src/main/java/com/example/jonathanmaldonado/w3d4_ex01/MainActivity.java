package com.example.jonathanmaldonado.w3d4_ex01;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName()+"_TAG";
    private static final String BASE_URL ="https://randomuser.me/api";

    OkHttpClient client;
    private List<Result> randomUserResults;
    private ImageView profilePictureIV;
    private TextView fullNameTV;
    private TextView addressTV;
    private TextView emailTV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient.Builder().build();
        randomUserResults = new ArrayList<>();
        fullNameTV= (TextView) findViewById(R.id.tv_fullName);
        addressTV= (TextView) findViewById(R.id.tv_address);
        emailTV= (TextView) findViewById(R.id.tv_email);
        profilePictureIV= (ImageView) findViewById(R.id.iv_profilePicture);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getRandomUser();
    }

    public void getRandomUserBtn(View view) {
        getRandomUser();
    }

    public void getRandomUser() {


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

                        if(response.isSuccessful()){


                            randomUserResults.clear();
                            String resp= response.body().string();
                            try {


                                Gson gson =new GsonBuilder().create();
                                RandomUser randomUser = gson.fromJson(resp, RandomUser.class);

                                randomUserResults= randomUser.getResults();
                                // mainResultTV.setText(GResults.get(0).getName().toString());
                                for (int i = 0; i < randomUserResults.size(); i++) {
                                    final int currentRandomUserIndex=i;
                                    Log.d(TAG, "onResponse for: " + randomUserResults.get(currentRandomUserIndex).getName().getFirst().toString());


                                    StringBuilder nameBuilder = new StringBuilder();
                                    nameBuilder.append("Full Name: "+randomUserResults.get(currentRandomUserIndex).getName().getFirst().toString()+" "+randomUserResults.get(currentRandomUserIndex).getName().getLast().toString());
                                    final String fullName = nameBuilder.toString();

                                    //build the user address
                                    StringBuilder addressBuilder = new StringBuilder();
                                    addressBuilder.append("Address: ");
                                    addressBuilder.append(randomUserResults.get(currentRandomUserIndex).getLocation().getStreet()+" ");
                                    addressBuilder.append(randomUserResults.get(currentRandomUserIndex).getLocation().getCity()+" ");
                                    addressBuilder.append(randomUserResults.get(currentRandomUserIndex).getLocation().getState()+" ");
                                    addressBuilder.append(randomUserResults.get(currentRandomUserIndex).getLocation().getPostcode());
                                    final String fullAddress = addressBuilder.toString();



                                    StringBuilder emailBuilder = new StringBuilder();
                                    emailBuilder.append("Email: "+randomUserResults.get(currentRandomUserIndex).getEmail().toString());
                                    final String fullEmail = emailBuilder.toString();


                                    //retrieve the user picture
                                    URL url = new URL(randomUserResults.get(currentRandomUserIndex).getPicture().getLarge());
                                    final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());



                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            profilePictureIV.setImageBitmap(bmp);
                                            fullNameTV.setText( fullName);
                                            addressTV.setText( fullAddress);
                                            emailTV.setText(fullEmail);
                                        }
                                    });
                                } //end for GResults


                                // used to be able to modify view



                            }catch (JsonParseException e){
                                e.printStackTrace();
                            }



                            Log.d(TAG, "onResponse resp:  "+ resp);
                        }else{
                            Log.d(TAG, "onResponse: Application Error");
                        }





                    }
                }
        );



    }


}
