package com.carltaylordev.recordlisterandroidclient.Server;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.carltaylordev.recordlisterandroidclient.KeyValueStore;
import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by carl on 13/06/2017.
 */

public class UserAuthCoordinator {

    public static final int TIMEOUT_TIME_MILLIS = 30000;

    public interface Interface {
        void onFinished(BoolResponse response);
    }

    private String mBaseUrl;
    private Context mContext;
    private UserAuthCoordinator.Interface mInterface;
    private boolean mInProgress = false;
    private RequestQueue mQueue;

    public UserAuthCoordinator(String baseUrl, Activity activity, UserAuthCoordinator.Interface Interface) {
        mBaseUrl = baseUrl;
        mContext = activity;
        mInterface = Interface;
        mQueue = Volley.newRequestQueue(mContext);
    }

    public void attemptAuthentication(String username, String password) {
        if (!mInProgress) {
            mInProgress = true;
            login(username, password);
        }
    }

    /**
     * Volley Login
     */

    private void login(String username, String password) {
        String url = mBaseUrl + "auth_user";
        try {
            final JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);
            final String requestBody = json.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    mInProgress = false;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        final KeyValueStore keyValueStore = new KeyValueStore(mContext);
                        keyValueStore.setStringForKey(KeyValueStore.KEY_SERVER_TOKEN, jsonObject.getString("token"));
                        mInterface.onFinished(new BoolResponse(true, "Token received, you are now logged in"));
                    } catch (JSONException e) {
                        jsonParseError();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mInProgress = false;
                    try {
                        String responseBody = new String( error.networkResponse.data, "utf-8" );
                        JSONObject jsonObject = new JSONObject( responseBody );
                        mInterface.onFinished(new BoolResponse(false, jsonObject.getString("message")));
                    } catch ( JSONException e ) {
                        jsonParseError();
                    } catch (UnsupportedEncodingException e){
                        jsonParseError();
                    }
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        Logger.logMessage(e.toString());
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response != null) {
                        String stringResponse = new String(response.data);
                        if (response.statusCode == 200) {
                            return Response.success(stringResponse, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    }
                    return Response.error(new VolleyError("Upload Failed"));
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_TIME_MILLIS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void jsonParseError() {
        mInterface.onFinished(new BoolResponse(false, "Unknown login error: error parsing JSON response"));
    }
}
