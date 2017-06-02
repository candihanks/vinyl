package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.carltaylordev.recordlisterandroidclient.Media.MultiAudioRecorder;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.RecordSessionManager;

import static android.view.View.GONE;


public class AudioFragment extends android.support.v4.app.Fragment implements RecordSessionManager.UpdateInterface, MultiAudioRecorder.Interface {

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
        mRecorder = new MultiAudioRecorder(this);
    }

    private void setupButtons(View view) {
        mRecordButton = (Button) view.findViewById(R.id.recordButton);
        mPlayButton = (Button) view.findViewById(R.id.playButton);

        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder.inUse()) {
                    mRecorder.stop();
                } else {
                    mRecorder.record(1);
                }
            }
        });

        //// TODO: 02/06/2017 separate stop button?

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder.inUse()) {
                    mRecorder.stop();
                } else {
                    mRecorder.play(1);
                }
            }
        });
    }

    /**
     *  RecordSessionManager Interface
     */

    @Override
    public void updateSession(RecordSessionManager manager) {
        // // TODO: 02/06/2017 gather tracks from recorder
    }

    @Override
    public void updateUI(RecordSessionManager manager) {
        // TODO: 02/06/2017 load tracks into recorder
    }

    /**
     *  MultiAudioRecorder Interface
     */

    @Override
    public void didStartPlaying() {
        mPlayButton.setText("Stop Playing");
        mRecordButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void didFinishPlaying() {
        mPlayButton.setText("Start Playing");
        mRecordButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void didStartRecording() {
        mRecordButton.setText("Stop Recording");
        mPlayButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void didFinishRecording() {
        mRecordButton.setText("Start Recording");
        mPlayButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void didError(String message) {
        ListingActivity activity = (ListingActivity)getActivity();
        activity.showAlert("Error:", message);
    }
}
