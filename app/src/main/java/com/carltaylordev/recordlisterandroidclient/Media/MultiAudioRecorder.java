package com.carltaylordev.recordlisterandroidclient.Media;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import com.carltaylordev.recordlisterandroidclient.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED;

/**
 * Created by carl on 02/06/2017.
 */

public class MultiAudioRecorder {

    public interface Interface {
        void didStartPlaying();
        void didFinishPlaying();
        void didStartRecording();
        void didFinishRecording();
        void didError(String message);
    }

    private HashMap<Integer, String> mFilesMap = new HashMap<>();

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    private Interface mInterface;

    private boolean mInUse = false;

    public MultiAudioRecorder(Interface activity) {
        mInterface = activity;

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
            mInterface.didFinishRecording();
        }
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mInterface.didFinishPlaying();
        }

        mPlayer = null;
        mRecorder = null;
        mInUse = false;
    }

    private void startPlaying(String inputFile) {
        mPlayer = new MediaPlayer();
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mInterface.didError("Error Playing Audio");
                return false;
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mInterface.didFinishPlaying();
            }
        });
        try {
            mPlayer.setDataSource(inputFile);
            mPlayer.prepare();
            mPlayer.start();
            mInterface.didStartPlaying();
        } catch (IOException e) {
            mInterface.didError("Media Player Failed:" + e.toString());
        }
    }

    private void startRecording(String outputFile) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setAudioEncodingBitRate(16);
        mRecorder.setAudioSamplingRate(44);
        mRecorder.setMaxDuration(60 * 1000);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(outputFile);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                mInterface.didError("Error Recording");
            }
        });
        mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                switch (what) {
                    case MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                        mInterface.didFinishRecording();
                }
            }
        });
        try {
            mRecorder.prepare();
            mRecorder.start();
            mInterface.didStartRecording();
        } catch (IOException e) {
            mInterface.didError("Media Record Failed:" + e.toString());
        }
    }
}
