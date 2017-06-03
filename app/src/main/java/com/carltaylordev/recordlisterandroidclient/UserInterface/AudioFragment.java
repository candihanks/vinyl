package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.carltaylordev.recordlisterandroidclient.Media.MultiAudioRecorder;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.RecordSessionManager;


public class AudioFragment extends android.support.v4.app.Fragment implements RecordSessionManager.UpdateInterface, MultiAudioRecorder.Interface {

    private Button mRecordButton;
    private Button mPlayButton;
    private Button mDeleteButton;
    private NumberPicker mNumberPicker;

    private MultiAudioRecorder mRecorder;

    public static final int NUMBER_OF_AUDIO_TRACKS = 10;

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
        setupNumberPicker(rootView);
        setButtonStateForTrack(1);
        return rootView;
    }

    /**
     *  Setup
     */

    private void setupMultiAudioRecorder() {
        mRecorder = new MultiAudioRecorder(this, NUMBER_OF_AUDIO_TRACKS, 1);
    }

    private void setupButtons(View view) {
        mRecordButton = (Button) view.findViewById(R.id.record_button);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder.inUse()) {
                    mRecorder.stop();
                } else {
                    mRecorder.record(mNumberPicker.getValue());
                }
            }
        });

        mPlayButton = (Button) view.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder.inUse()) {
                    mRecorder.stop();
                } else {
                    mRecorder.play(mNumberPicker.getValue());
                }
            }
        });

        mDeleteButton = (Button) view.findViewById(R.id.delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder.inUse()) {
                    mRecorder.stop();
                }
                mRecorder.deleteFile(mNumberPicker.getValue());
                setButtonStateForTrack(mNumberPicker.getValue());
            }
        });
    }

    private void setupNumberPicker(View view) {
        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(NUMBER_OF_AUDIO_TRACKS);
        mNumberPicker.setWrapSelectorWheel(false);
        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mRecorder.stop();
                setButtonStateForTrack(newVal);
            }
        });
    }

    private void setButtonStateForTrack(int track) {
        boolean trackExists = mRecorder.audioFileExists(track);
        if (trackExists) {
            mPlayButton.setVisibility(View.VISIBLE);
            mDeleteButton.setVisibility(View.VISIBLE);
        } else {
            mPlayButton.setVisibility(View.INVISIBLE);
            mDeleteButton.setVisibility(View.INVISIBLE);
        }
        mRecordButton.setVisibility(View.VISIBLE);
    }

    /**
     *  RecordSessionManager Interface
     */

    @Override
    public void updateSession(RecordSessionManager manager) {
        manager.setAudio(mRecorder.getRecordedMap());
    }

    @Override
    public void updateUI(RecordSessionManager manager) {
        if (manager.getAudio() != null) {
            mRecorder.loadAudioMap(manager.getAudio());
        }
    }

    /**
     *  MultiAudioRecorder Interface
     */

    @Override
    public void didStartPlaying() {
        mPlayButton.setText("Stop");
        mRecordButton.setVisibility(View.INVISIBLE);
        mDeleteButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void didFinishPlaying() {
        mPlayButton.setText("Play");
        setButtonStateForTrack(mNumberPicker.getValue());
    }

    @Override
    public void didStartRecording() {
        mRecordButton.setText("Stop");
        mPlayButton.setVisibility(View.INVISIBLE);
        mDeleteButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void didFinishRecording() {
        mRecordButton.setText("Record");
        setButtonStateForTrack(mNumberPicker.getValue());
    }

    @Override
    public void didError(String message) {
        mRecorder.stop();
        setButtonStateForTrack(mNumberPicker.getValue());
        ListingActivity activity = (ListingActivity)getActivity();
        activity.showAlert("Error:", message);
    }
}
