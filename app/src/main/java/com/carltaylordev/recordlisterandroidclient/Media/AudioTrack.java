package com.carltaylordev.recordlisterandroidclient.Media;

/**
 * Created by carl on 06/06/2017.
 */

public class AudioTrack {

        private String mTitle;
        private String mFilePath;
        private String mUuid;

        public AudioTrack(String title, String filePath, String uuid) {
            this.mTitle = title;
            this.mFilePath = filePath;
            this.mUuid = uuid;
        }

        public static AudioTrack createEmptyTrack() {
            return new AudioTrack("Empty", null, null);
        }

        public String getTitle() {
            return mTitle;
        }

        public String getFilePath() {
            return mFilePath;
        }

        public String getUuid() {
            return mUuid;
        }

        public void setFilePath(String filePath) {
            this.mFilePath = filePath;
        }
}
