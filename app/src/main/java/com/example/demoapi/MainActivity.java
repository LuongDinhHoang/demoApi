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
       // String url = "http://zing.vn"+mEdit;
        String url ="http://10.2.22.67:9090/craw_data/_search";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String savedata= data;
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, jsonObject,
//                new Response.Listener<JSONObject>() {//ket qua tra ve
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            mText.setText(response.toString());
//                            JSONArray jsonArray = response.getJSONArray("_source");
//                            mListView = new ArrayList<>();
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject employee = jsonArray.getJSONObject(i);
//                                String description = employee.getString("_index");
//                                String post_url = employee.getString("post_url");
//                                String title = employee.getString("title");
//                                String web_url = employee.getString("web_url");
//
//
//                                mText.append(description);
//                               // mListView.add(new Model(description,post_url,title,web_url));
//                            }
//                            //ve len adapter
//                            mAdapter =new ViewAdapter(mListView);
//                            mList.setAdapter(mAdapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }

  //      });
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    mText.setText(response.toString());

                //Log.i("VOLLEY", response);
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
                    //Log.v("Unsupported Encoding while trying to get the bytes", data);
                    return null;
                }
            }

        };
        mQueue.add(stringRequest);
        }
    }
