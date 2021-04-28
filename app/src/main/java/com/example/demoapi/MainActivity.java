package com.example.demoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView mText;
    private EditText mEdit;
    private Button mButton;
    private RequestQueue mQueue;
    public ArrayList<Model> mListView;
    public ViewAdapter mAdapter;

    ListView mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //anh xa
        mText = findViewById(R.id.textV);
        mEdit = findViewById(R.id.editT);
        mButton = findViewById(R.id.BttonS);
        mList = findViewById(R.id.ListV);

        mQueue= Volley.newRequestQueue(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "{\"size\":10,\"sort\":[{\"_score\":\"desc\"}],\"query\":{\"bool\":{\"must\":{\"multi_match\":{\"query\":\"("+mEdit.getText().toString()+") OR (cúm)\",\"fields\":[\"header\",\"description\"]}}}},\"_source\":[\"header\",\"description\",\"web_url\",\"post_url\"]}";
                jsonParse(data);
            }
        });
        }
    private void jsonParse(String data) {
        //GET du lieu Json tu 1 link
        String url ="http://10.2.22.67:9090/craw_data/_search";
        final String savedata= data;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    try {
                        //mText.setText(response.toString());
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject jsonObjectHits=jsonObject.getJSONObject("hits");
                        JSONArray jsonArrayHits=jsonObjectHits.getJSONArray("hits");
                        mListView = new ArrayList<>();
                        for (int i = 0; i < jsonArrayHits.length(); i++) {
                            JSONObject jsonObjectRound=jsonArrayHits.getJSONObject(i);
                            JSONObject jsonObjectSource=jsonObjectRound.getJSONObject("_source");
                            String description = jsonObjectSource.getString("description");
                            String header = jsonObjectSource.getString("header");

                            mListView.add(new Model(description,header));

                        }
                         //ve len adapter
                            mAdapter =new ViewAdapter(mListView);
                            mList.setAdapter(mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                //Log.v("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }

        };
        mQueue.add(stringRequest);
        }
    }
