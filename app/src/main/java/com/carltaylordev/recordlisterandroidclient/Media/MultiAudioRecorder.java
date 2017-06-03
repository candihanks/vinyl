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

    public MultiAudioRecorder(Interface activity, int numberOfTracks) {
        mInterface = activity;

        for (int i = 1; i < numberOfTracks + 1; i++) {
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
                File tempFile = FileManager.createTempFileOnDisc(".aac");
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
            try {
                mRecorder.stop();
            } catch (IllegalStateException e) {
                Logger.logMessage(e.toString());
            }
            mInterface.didFinishRecording();
        }
        if (mPlayer != null) {
            try {
                mPlayer.stop();
            } catch (IllegalStateException e) {
                Logger.logMessage(e.toString());
            }
            mInterface.didFinishPlaying();
        }
        mInUse = false;
    }

    private void startPlaying(String inputFile) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        } else {
            mPlayer.reset();
        }

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mInUse = false;
                mInterface.didError("Error Playing Audio");
                return false;
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mInUse = false;
                mInterface.didFinishPlaying();
            }
        });

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
                mInterface.didStartPlaying();
            }
        });

        try {
            mPlayer.setDataSource(inputFile);
            mPlayer.prepare();
        } catch (IOException e) {
            mInUse = false;
            mInterface.didError("Media Player Failed:" + e.toString());
        }
    }

    private void startRecording(String outputFile) {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        } else {
            mRecorder.reset();
        }

        try {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mRecorder.setAudioEncodingBitRate(44100);
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setMaxDuration(60 * 1000);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    mInUse = false;
                    mInterface.didError("Error Recording");
                }
            });
            mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    switch (what) {
                        case MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                            mInUse = false;
                            mInterface.didFinishRecording();
                    }
                }
            });

            mRecorder.setOutputFile(outputFile);
            mRecorder.prepare();
            mRecorder.start();
            mInterface.didStartRecording();
        } catch (IOException e) {
            mInUse = false;
            mInterface.didError("Media Record Failed:" + e.toString());
        }
    }
}
