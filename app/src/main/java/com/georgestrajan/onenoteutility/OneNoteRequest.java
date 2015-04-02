package com.georgestrajan.onenoteutility;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;
import com.android.volley.Response;
import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by georgestrajan on 3/24/15.
 */
public class OneNoteRequest {

    private String mAccessToken;
    private Context context;

    public OneNoteRequest(String authToken, Context context) {
        this.mAccessToken = authToken;
        this.context = context;
    }

    public void SendGetRequest(String url, final OneNoteResponse.Listener<JSONObject> oneNoteResponse) {

        final String authToken = "Bearer " + this.mAccessToken;
        Map<String, String> jsonParams = new HashMap<String, String>();

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(jsonParams),

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        oneNoteResponse.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject( responseBody );

                        } catch ( JSONException jsEx ) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException ueEx){

                        }
                    }

                })
            {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", authToken);
                return headers;
            }
/*
            @Override
            protected Map<String, String> getParams() {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", authToken);

                return headers;
            }
*/
        };


        // Access the RequestQueue through your singleton class.
        AppSingleton.getInstance(this.context).addToRequestQueue(myRequest);

    }

}
