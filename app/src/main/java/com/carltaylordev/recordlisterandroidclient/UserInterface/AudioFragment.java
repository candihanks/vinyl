package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.RecordSessionManager;


public class AudioFragment extends android.support.v4.app.Fragment implements RecordSessionManager.UpdateInterface  {


    /**
     * Constructors
     */

    public AudioFragment() {}


    public static AudioFragment newInstance() {
        return new AudioFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.audio_fragment, container, false);

        return rootView;
    }

    /**
     *  RecordSessionManager Interface
     */

    @Override
    public void updateSession(RecordSessionManager manager) {

    }

    @Override
    public void updateUI(RecordSessionManager manager) {


    }
}
