package com.carltaylordev.recordlisterandroidclient.UserInterface.EditListing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.carltaylordev.recordlisterandroidclient.Media.MultiAudioRecorder;
import com.carltaylordev.recordlisterandroidclient.R;


public class AudioFragment extends android.support.v4.app.Fragment implements RecordSessionManager.UpdateSessionInterface,
        RecordSessionManager.UpdateUiInterface, MultiAudioRecorder.Interface {

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
        setButtonStateForIndex(0);
        return rootView;
    }

    /**
     *  Setup
     */

    private void setupMultiAudioRecorder() {
        mRecorder = new MultiAudioRecorder(this, NUMBER_OF_AUDIO_TRACKS);
    }

    private void setupButtons(View view) {
        mRecordButton = (Button) view.findViewById(R.id.record_button);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder.inUse()) {
                    mRecorder.stop();
                } else {
                    mRecorder.record(numberPickerIndexOffset());
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
                    mRecorder.play(numberPickerIndexOffset());
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
                mRecorder.deleteTrack(numberPickerIndexOffset());
                setButtonStateForIndex(numberPickerIndexOffset());
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
                setButtonStateForIndex(numberPickerIndexOffset());
            }
        });
    }

    private void setButtonStateForIndex(int index) {
        boolean trackExists = mRecorder.audioFileExists(index);
        if (trackExists) {
            mPlayButton.setVisibility(View.VISIBLE);
            mDeleteButton.setVisibility(View.VISIBLE);
        } else {
            mPlayButton.setVisibility(View.INVISIBLE);
            mDeleteButton.setVisibility(View.INVISIBLE);
        }
        mRecordButton.setVisibility(View.VISIBLE);
    }

    private int numberPickerIndexOffset() {
        return mNumberPicker.getValue() - 1;
    }

    /**
     *  RecordSessionManager Interface
     */

    @Override
    public void updateSession(RecordSessionManager manager) {
        manager.setAudio(mRecorder.getTracks());
    }

    @Override
    public void updateUI(RecordSessionManager manager) {
        if (manager.getAudio() != null) {
            mRecorder.setTracks(manager.getAudio());
        }
        setButtonStateForIndex(numberPickerIndexOffset());
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
        setButtonStateForIndex(numberPickerIndexOffset());
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
        setButtonStateForIndex(numberPickerIndexOffset());
    }

    @Override
    public void didError(String message) {
        mRecorder.stop();
        setButtonStateForIndex(numberPickerIndexOffset());
        EditListingActivity activity = (EditListingActivity)getActivity();
        activity.showAlert("Error:", message);
    }
}
