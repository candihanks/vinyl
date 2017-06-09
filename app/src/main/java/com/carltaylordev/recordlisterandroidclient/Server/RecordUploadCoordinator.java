package com.carltaylordev.recordlisterandroidclient.Server;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by carl on 09/06/2017.
 */

public class RecordUploadCoordinator {

    Realm mRealm;
    Context mContext;

    public RecordUploadCoordinator(Context context) {
        mRealm = Realm.getDefaultInstance();
        mContext = context;
    }

    public void uploadAll() {
        final RealmRecord record = mRealm.where(RealmRecord.class).equalTo(RealmRecord.UPLOADED, false).findFirst();
        JSONObject params = new JSONObject();
        try {
            params= record.toJSON();
        } catch (JSONException e) {}

        String url ="http://10.0.2.2:8000/list_item";
        RequestQueue queue = Volley.newRequestQueue(mContext);

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, future, future);
        queue.add(request);
        try {
            JSONObject response = future.get();
            Logger.logMessage("BOB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
