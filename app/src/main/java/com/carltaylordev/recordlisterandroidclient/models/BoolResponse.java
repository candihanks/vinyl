package com.carltaylordev.recordlisterandroidclient.models;

/**
 * Created by carl on 30/05/2017.
 */

public class BoolResponse {

    private boolean result;
    private String userMessage;

    public BoolResponse(boolean result, String userMessage) {
        this.result = result;
        this.userMessage = userMessage;
    }

    public boolean isTrue() {
        return result;
    }

    public String getUserMessage() {
        return userMessage;
    }
}