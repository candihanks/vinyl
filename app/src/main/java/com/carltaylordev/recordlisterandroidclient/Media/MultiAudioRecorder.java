package com.carltaylordev.recordlisterandroidclient.Media;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import com.carltaylordev.recordlisterandroidclient.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

        for (int i = 0; i < numberOfTracks; i++) {
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
        return !mFilesMap.get(trackNumber.intValue() - 1).isEmpty();
    }

    public void deleteFile(Integer trackNumber) {
        FileManager.deleteFile(mFilesMap.get(trackNumber.intValue() - 1));
        mFilesMap.put(trackNumber.intValue() -1, "");
    }

    public void loadAudioMap(Map<Integer, String> audioMap) {
        for (int i = 0; i < audioMap.size(); i++) {
            mFilesMap.put(new Integer(i), audioMap.get(i));
        }
    }

    public Map<Integer, String> getRecordedMap() {
        HashMap<Integer, String> recorded = new HashMap();
        for (int i = 0; i < mFilesMap.size(); i++) {
            String path = mFilesMap.get(new Integer(i));
            if (path != null && !path.isEmpty()) {
                recorded.put(i, path);
            }
        }
        Map<Integer, String> immutableMap = Collections.unmodifiableMap(new LinkedHashMap<>(recorded));
        return immutableMap;
    }

    /**
     *  Audio Controls
     */

    public Boolean inUse() {
        return mInUse;
    }

    public void play(Integer trackNumber) {
        if (inUse()) {
            stop();
        }
        startPlaying(mFilesMap.get(trackNumber.intValue() - 1));
        mInUse = true;
    }

    public void record(Integer trackNumber) {
        if (inUse()) {
            stop();
        }
        Integer mapNumber = trackNumber.intValue() - 1;
        try {
            String path = mFilesMap.get(mapNumber);
            if (path.isEmpty()) {
                File tempFile = FileManager.createTempFileOnDisc(".aac");
                mFilesMap.put(mapNumber, tempFile.getAbsolutePath());
            }
        } catch (Exception e) {
            Logger.logMessage("Problem creating temp file: " + e.toString());
        }
        startRecording(mFilesMap.get(mapNumber));
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
