package com.carltaylordev.recordlisterandroidclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by carl on 30/05/2017.
 */

public class PhotosFragment extends android.support.v4.app.Fragment {

    /**
     * Constructors
     */

    public PhotosFragment() {
    }

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }

    /**
     *  Fragment LifeCycle
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photos_fragment, container, false);

        ListingActivity activity = (ListingActivity)getActivity();

        return rootView;
    }


}
