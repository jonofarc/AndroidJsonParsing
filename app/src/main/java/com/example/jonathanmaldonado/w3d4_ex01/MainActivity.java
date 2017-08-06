package com.example.jonathanmaldonado.w3d4_ex01;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonathanmaldonado.w3d4_ex01.DataBase.DBHelper;
import com.example.jonathanmaldonado.w3d4_ex01.DataBase.FeedReaderContract;
import com.example.jonathanmaldonado.w3d4_ex01.randomUsers.RandomUser;
import com.example.jonathanmaldonado.w3d4_ex01.randomUsers.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    public static final String MAIN_ACTIVITY_EXTRA="com.example.jonathanmaldonado.w3d4_ex01.MAIN_ACTIVITY_EXTRA";
    private static final String BASE_URL ="https://randomuser.me/api";

    private DBHelper helper;
    private SQLiteDatabase database;

    OkHttpClient client;
    private List<Result> randomUserResults;
    private ImageView profilePictureIV;
    private EditText aliasET;
    private TextView fullNameTV;
    private TextView addressTV;
    private TextView emailTV;
    private TextView alertTV;
    private String pictureImage;
    private String originalFullName;
    private String originalAddress;
    private String originalEmail;
    private Bitmap ImageBitMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DBHelper(this);
        database = helper.getWritableDatabase();


        client = new OkHttpClient.Builder().build();
        randomUserResults = new ArrayList<>();
        aliasET= (EditText) findViewById(R.id.et_alias);
        fullNameTV= (TextView) findViewById(R.id.tv_fullName);
        addressTV= (TextView) findViewById(R.id.tv_address);
        emailTV= (TextView) findViewById(R.id.tv_email);
        profilePictureIV= (ImageView) findViewById(R.id.iv_profilePicture);
        alertTV= (TextView) findViewById(R.id.tv_alerts);

        getRandomUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // getRandomUser();
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
                                    pictureImage=url.toString();
                                    ImageBitMap=bmp;

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


                                    nameBuilder = new StringBuilder();
                                    nameBuilder.append(randomUserResults.get(currentRandomUserIndex).getName().getFirst().toString()+" "+randomUserResults.get(currentRandomUserIndex).getName().getLast().toString());


                                    //build the user address
                                    addressBuilder = new StringBuilder();
                                    addressBuilder.append(randomUserResults.get(currentRandomUserIndex).getLocation().getStreet()+" ");
                                    addressBuilder.append(randomUserResults.get(currentRandomUserIndex).getLocation().getCity()+" ");
                                    addressBuilder.append(randomUserResults.get(currentRandomUserIndex).getLocation().getState()+" ");
                                    addressBuilder.append(randomUserResults.get(currentRandomUserIndex).getLocation().getPostcode());






                                    originalFullName=nameBuilder.toString();
                                    originalAddress=addressBuilder.toString();
                                    originalEmail=randomUserResults.get(currentRandomUserIndex).getEmail().toString();

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

    private void saveRandomUser(){

        if(TextUtils.isEmpty(aliasET.getText().toString())){

            //Error Message
            //alertTV.setText("");
            alertTV.setText(R.string.lbl_field_no_blank);

        }else{


            byte[] imageBitsArray=getBytes(ImageBitMap);

            String alias= aliasET.getText().toString();


            ContentValues values= new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ALIAS,alias);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FULL_NAME,originalFullName);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ADDRESS,originalAddress);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL,originalEmail);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PICTURE_IMAGE,imageBitsArray);
            long recordId = database.insert(FeedReaderContract.FeedEntry.TABLE_NAME,null,values);
            if (recordId>0){
                // Log.d(TAG, "Record Saved");
                //show if save was succesfull
              //  saveNoteResult.setText("");
                alertTV.setText("User Saved: "+" \nAlias: "+ alias+" \nName: "+ originalFullName+ " \nAddress: "+ originalAddress+" \nEmail: "+ originalEmail+ " \npictureImage: "+ pictureImage);

            }


        }





    }


    public void saveUser(View view) {
        saveRandomUser();
    }

    public void searchUser(View view) {
        Intent intent = new Intent(MainActivity.this , SearchActivity.class);
        if(!TextUtils.isEmpty(aliasET.getText().toString())) {
            intent.putExtra(MAIN_ACTIVITY_EXTRA, aliasET.getText().toString());
        }

        startActivity(intent);
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
