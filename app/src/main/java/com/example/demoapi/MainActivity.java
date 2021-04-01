package com.example.demoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView mText;
    private EditText mEdit;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = findViewById(R.id.textV);
        mEdit = findViewById(R.id.editT);
        mButton = findViewById(R.id.BttonS);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetURL().execute("http://"+mEdit.getText().toString().trim()) ;}
        });

    }
    class  GetURL extends AsyncTask<String,String,String>{
        OkHttpClient OkHttpClient = new  OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(15,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        @Override
        protected String doInBackground(String... strings) {
            Request.Builder builder=new Request.Builder();
            builder.url(strings[0]);
            Request request=builder.build();
            try {
                 Response  response= OkHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals(" "))
            {
                mText.append(s);
            }
            else {
            }
            super.onPostExecute(s);
        }
    }

}