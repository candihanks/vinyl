package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.carltaylordev.recordlisterandroidclient.Media.MultiAudioRecorder;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.RecordSessionManager;


public class AudioFragment extends android.support.v4.app.Fragment implements RecordSessionManager.UpdateInterface  {

    private Button mRecordButton;
    private Button mPlayButton;

    private MultiAudioRecorder mRecorder;

    /**
     * Constructors
     */

    public AudioFragment() {}


    public static AudioFragment newInstance() {
        return new AudioFragment();
    }

    /**
     *  Fragment LifeCycle
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.audio_fragment, container, false);
        setupMultiAudioRecorder();
        setupButtons(rootView);
        return rootView;
    }

    /**
     *  Setup
     */

    private void setupMultiAudioRecorder() {
        mRecorder = new MultiAudioRecorder();
    }

    private void setupButtons(View view) {
        mRecordButton = (Button) view.findViewById(R.id.recordButton);
        mPlayButton = (Button) view.findViewById(R.id.playButton);

        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder.inUse()) {
                    mRecorder.stop();
                    mRecordButton.setText("Start Recording");
                } else {
                    mRecorder.record(1);
                    mRecordButton.setText("Stop Recording");
                }
            }
        });

        //// TODO: 02/06/2017 separate stop button?

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder.inUse()) {
                    mRecorder.stop();
                    mPlayButton.setText("Start Playing");
                } else {
                    mRecorder.play(1);
                    mPlayButton.setText("Stop Playing");
                }
            }
        });
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
