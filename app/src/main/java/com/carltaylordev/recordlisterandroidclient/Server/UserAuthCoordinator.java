package com.carltaylordev.recordlisterandroidclient.Server;

import android.app.Activity;
import android.content.Context;

import com.carltaylordev.recordlisterandroidclient.UserInterface.SavedListings.RecyclerAdapter;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

/**
 * Created by carl on 13/06/2017.
 */

public class UserAuthCoordinator {

    public interface Interface {
        void onFinished(BoolResponse response);
    }

    private String mBaseUrl;
    private Context mContext;
    private UserAuthCoordinator.Interface mInterface;


    public UserAuthCoordinator(String baseUrl, Activity activity, UserAuthCoordinator.Interface Interface) {
        mBaseUrl = baseUrl;
        mContext = activity;
        mInterface = Interface;
    }

    public void attemptLogin(String username, String password) {
        mInterface.onFinished(new BoolResponse(true, "Token received, you are now logged in"));
    }
}
