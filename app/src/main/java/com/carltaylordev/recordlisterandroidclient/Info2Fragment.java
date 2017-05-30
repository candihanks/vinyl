package com.carltaylordev.recordlisterandroidclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Info2Fragment extends android.support.v4.app.Fragment {

    public Info2Fragment() {
    }

    public static Info2Fragment newInstance() {
        return new Info2Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info_2_fragment, container, false);
        return rootView;
    }

}
