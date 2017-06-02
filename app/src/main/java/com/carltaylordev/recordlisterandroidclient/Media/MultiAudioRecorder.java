package com.carltaylordev.recordlisterandroidclient.Media;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Button;

import com.carltaylordev.recordlisterandroidclient.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by carl on 02/06/2017.
 */

public class MultiAudioRecorder {

    private HashMap<Integer, String> mFilesMap = new HashMap<>();

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    private boolean mInUse = false;

    public MultiAudioRecorder() {

        for (int i = 0; i < 11; i++) {
            mFilesMap.put(new Integer(i), "");
        }
    }

    public void kill() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     *  File Management
     */

    public Boolean audioFileExists(Integer trackNumber) {
        return !mFilesMap.get(trackNumber).isEmpty();
    }

    public void removeFile(Integer trackNumber) {
        mFilesMap.put(trackNumber, "");
        // // TODO: 02/06/2017 instantly clean up temp file?
    }

    public HashMap<Integer, String> getRecorded() {
        // // TODO: 02/06/2017 only pass back with non blank strings?
        return mFilesMap;
    }


    /**
     *  Audio Controls
     */

    public Boolean inUse() {
        return mInUse;
    }

    public void play(Integer trackNumber) {
        stop();
        startPlaying(mFilesMap.get(trackNumber));
        mInUse = true;
    }

    public void record(Integer trackNumber) {
        stop();
        try {
            String path = mFilesMap.get(trackNumber);
            if (path.isEmpty()) {
                File tempFile = FileManager.createTempFileOnDisc(".3gp");
                mFilesMap.put(trackNumber, tempFile.getAbsolutePath());
            }
        } catch (Exception e) {
            Logger.logMessage("Problem creating temp file: " + e.toString());
        }
        startRecording(mFilesMap.get(trackNumber));
        mInUse = true;
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
        }
        if (mPlayer != null) {
            mPlayer.release();
        }

        mPlayer = null;
        mRecorder = null;
        mInUse = false;
    }

    private void startPlaying(String inputFile) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(inputFile);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Logger.logMessage("Media Player Failed:" + e.toString());
        }
    }

    private void startRecording(String outputFile) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(outputFile);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Logger.logMessage("Media Record Failed:" + e.toString());
        }

        mRecorder.start();
    }
}
