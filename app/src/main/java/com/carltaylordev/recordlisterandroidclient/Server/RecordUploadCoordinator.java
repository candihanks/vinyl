package com.carltaylordev.recordlisterandroidclient.Server;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import io.realm.Realm;

/**
 * Created by carl on 09/06/2017.
 */

public class RecordUploadCoordinator {

    public static final int TIMEOUT_TIME_MILLIS = 30000;

    public interface Interface {
        void onUploadCountUpdate(int uploadCount);
        void onFinished(BoolResponse response);
    }

    private int mSucceed = 0;
    private int mFailed = 0;
    private int mStartSize;

    private boolean mInProgress;
    private Realm mRealm;
    private Context mContext;
    private String mBaseUrl;
    private String mToken;
    private List<RealmRecord> mRecords;
    private RequestQueue mQueue;
    private RecordUploadCoordinator.Interface mInterface;

    public RecordUploadCoordinator(String baseUrl,
                                   String token,
                                   List<RealmRecord> records,
                                   Realm realm,
                                   Context context,
                                   RecordUploadCoordinator.Interface Interface) {
        mRealm = realm;
        mContext = context;
        mRecords = records;
        mBaseUrl = baseUrl;
        mToken = token;
        mQueue = Volley.newRequestQueue(mContext);
        mInterface = Interface;
        mStartSize = records.size();
    }

    public boolean isInProgress() {
        return mInProgress;
    }

    /**
     * Volley Upload
     */

    private void jsonParseErrorTokenClearResponse() {
        mInterface.onFinished(new BoolResponse(false, "Server error: error parsing JSON response"));
    }

    public void tryNextUpload() {
        mInterface.onUploadCountUpdate(mFailed + mSucceed + 1);
       if (mRecords.size() > 0) {
           mInProgress = true;
           RealmRecord record = mRecords.get(0);
           mRecords.remove(0);
           upload(record);
       } else {
           mInProgress = false;
           if (mSucceed == mStartSize) {
               mInterface.onFinished(new BoolResponse(true, "Uploads Accepted"));
           } else {
               mInterface.onFinished(new BoolResponse(false, String.format("Number of Uploads Failed: %s (%s)", mFailed, mStartSize)));
           }
       }
    }

    private void upload(final RealmRecord record) {
        String url = mBaseUrl + "add_item";
        try {
            JSONObject jsonObject = record.toJSON();
            jsonObject.put("token", mToken);
            final String requestBody = jsonObject.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    updateRecordAsUploaded(record, true);
                    mSucceed ++;
                    tryNextUpload();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    updateRecordAsUploaded(record, false);
                    mInProgress = false;

                    if (error instanceof AuthFailureError) {
                        mInterface.onFinished(new BoolResponse(false, "Token no longer valid. Try clearing token and logging in again via settings"));

                    } else if (error instanceof ServerError) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            mInterface.onFinished(new BoolResponse(false, jsonObject.getString("message")));
                        } catch (Exception e) {
                            jsonParseErrorTokenClearResponse();
                        }
                    } else {
                        mFailed ++;
                        tryNextUpload();
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
                        if (response.statusCode == 202) {
                            return Response.success(response.toString(), HttpHeaderParser.parseCacheHeaders(response));
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

    /**
     * Realm Update
     */

    private void updateRecordAsUploaded(RealmRecord record, boolean success) {
        mRealm.beginTransaction();
        record.setUploaded(success);
        mRealm.copyToRealmOrUpdate(record);
        mRealm.commitTransaction();
    }
}
