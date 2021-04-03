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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
//        mText = findViewById(R.id.textV);
        mEdit = findViewById(R.id.editT);
        mButton = findViewById(R.id.BttonS);
        mList = findViewById(R.id.ListV);

        mQueue= Volley.newRequestQueue(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
        }
    private void jsonParse() {
        //GET du lieu Json tu 1 link
        String url = "http://172.36.68.21:9200/medicalsearch?key_search="+mEdit;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {//ket qua tra ve
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");
                            mListView = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);
                                String description = employee.getString("description");
                                String post_url = employee.getString("post_url");
                                String title = employee.getString("title");
                                String web_url = employee.getString("web_url");

                               // mText.append(firstName + ", " + age + ", " + mail +","+mail1+ "\n\n");
                                mListView.add(new Model(description,post_url,title,web_url));
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
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        }
    }
