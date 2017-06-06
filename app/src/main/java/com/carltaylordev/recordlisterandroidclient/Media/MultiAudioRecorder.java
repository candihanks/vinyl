package com.carltaylordev.recordlisterandroidclient.Media;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import com.carltaylordev.recordlisterandroidclient.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    private ArrayList<AudioTrack> mTracks = new ArrayList<>();

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    private Interface mInterface;

    private boolean mInUse = false;

    public MultiAudioRecorder(Interface activity, int numberOfTracks) {
        mInterface = activity;

        for (int i = 0; i < numberOfTracks; i++) {
            mTracks.add(AudioTrack.emptyTrack());
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
     *  Track Management
     */

    public Boolean audioFileExists(int index) {
        try {
            AudioTrack track = mTracks.get(index);
            track.getFilePath();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteTrack(Integer index) {
        AudioTrack track = mTracks.get(index);
        try {
            FileManager.deleteFileAtPath(track.getFilePath());
        } catch (NullPointerException e) {}
        track.setFilePath(null);
    }

    public ArrayList<AudioTrack> getTracks() {
        return mTracks;
    }

    public void setTracks(ArrayList<AudioTrack> tracks) {
        mTracks = tracks;
    }

    /**
     *  Audio Controls
     */

    public Boolean inUse() {
        return mInUse;
    }

    public void play(int index) {
        if (inUse()) {
            stop();
        }
        startPlaying(mTracks.get(index));
        mInUse = true;
    }

    public void record(int index) {
        if (inUse()) {
            stop();
        }
        AudioTrack track = mTracks.get(index);
        try {
            if (track.getFilePath() == null) {
                File tempFile = FileManager.createTempFileOnDisc(".aac");
                track.setFilePath(tempFile.getAbsolutePath());
            }
        } catch (Exception e) {
            Logger.logMessage("Problem creating temp file: " + e.toString());
        }
        startRecording(track);
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

    private void startPlaying(AudioTrack track) {
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
            mPlayer.setDataSource(track.getFilePath());
            mPlayer.prepare();
        } catch (IOException e) {
            mInUse = false;
            mInterface.didError("Media Player Failed:" + e.toString());
        }
    }

    private void startRecording(AudioTrack track) {
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

            mRecorder.setOutputFile(track.getFilePath());
            mRecorder.prepare();
            mRecorder.start();
            mInterface.didStartRecording();
        } catch (IOException e) {
            mInUse = false;
            mInterface.didError("Media Record Failed:" + e.toString());
        }
    }
}
