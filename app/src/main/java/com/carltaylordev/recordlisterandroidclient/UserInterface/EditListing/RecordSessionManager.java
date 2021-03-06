package com.carltaylordev.recordlisterandroidclient.UserInterface.EditListing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.Media.AudioTrack;
import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.RealmAudioClip;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by carl on 29/05/2017.
 */


public class RecordSessionManager {

    public interface UpdateSessionInterface {
        void updateSession(RecordSessionManager sessionManager);
    }

    public interface UpdateUiInterface {
        void updateUI(RecordSessionManager sessionManager);
    }

    public interface ErrorInterface {
        void showErrorMessage(String message);
    }

    private RealmList<RealmImage> mImages = new RealmList<>();
    private ArrayList<AudioTrack> mAudioTracks = new ArrayList<>();

    private RealmRecord mRealmRecord;
    private Realm mRealm;

    private UpdateSessionInterface mUpdateUpdateSessionInterface;
    private UpdateUiInterface mUpdateUiInterface;
    private ErrorInterface mErrorInterface;
    private Context mContext;

    public RecordSessionManager(RealmRecord record, Realm realm, Activity activity) {
        mRealmRecord = record;
        mRealm = realm;
        mUpdateUpdateSessionInterface = (UpdateSessionInterface) activity;
        mUpdateUiInterface = (UpdateUiInterface) activity;
        mErrorInterface = (ErrorInterface) activity;
        mContext = activity;

        loadAssociatedData();
    }

    private void loadAssociatedData() {
        loadAssociatedPictures();
        loadAssociatedAudioClips();
    }

    /**
     * Test Data
     */

    public void createTestData() {
        mRealmRecord.setArtist("Test Artist");
        mRealmRecord.setTitle("Test Title");
        mRealmRecord.setLabel("Test Label");
        mRealmRecord.setMediaCondition("Good Plus (G+)");
        mRealmRecord.setCoverCondition("Good Plus (G+)");
        mRealmRecord.setComments("Here is a great record");
        mRealmRecord.setListingTitle(String.format("Test Listing Do Not Buy (#%s)", FileManager.randomNumber()));
        mRealmRecord.setPrice("1.99");

        List<String> stringCats = getCategoriesAsStrings();
        EbayCategory category = mRealm.where(EbayCategory.class).equalTo("name", stringCats.get(0)).findFirst();
        mRealmRecord.setEbayCategory(category);

        mImages = new RealmList<>();
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.test_800);
            FileManager manager = new FileManager(mContext);
            File tempFile = manager.writeJpegToDisc(bitmap, FileManager.getRootTempPath(), cleanStringForFileName(mRealmRecord.getListingTitle()));
            RealmImage image = new RealmImage("test", tempFile.getAbsolutePath());
            mImages.add(image);
        } catch (Exception e) {}

        refreshUi();
    }

    /**
     * Get
     */

    public static List<String> getCategoriesAsStrings() {
        List<EbayCategory> sorted = EbayCategory.getAllSortedByFavourite();
        List<String> strings = new ArrayList<>();
        for (EbayCategory category : sorted) {
            strings.add(category.getName());
        }
        return strings;
    }

    public static ArrayList<String> getRecordConditions() {
        // todo: fetch these from the server? or at least make sure they match with server values
        ArrayList<String> list = new ArrayList<>();
        list.add("Mint (M)");
        list.add("Near Mint (NM or M-)");
        list.add("Very Good Plus (VG+)");
        list.add("Very Good (VG)");
        list.add("Good Plus (G+)");
        list.add("Good (G)");
        list.add("Fair (F)");
        list.add("Poor (P)");
        return list;
    }

    public static ArrayList<String> getCoverConditions() {
        ArrayList<String> list = getRecordConditions();
        list.add(0, "Generic");
        return list;
    }

    public ArrayList<RealmImage> getImages() {
        ArrayList<RealmImage> copy = new ArrayList<>(mImages);
        return copy;
    }

    public ArrayList<AudioTrack> getAudio() {
        return mAudioTracks;
    }

    public RealmRecord getRecord() {
        return mRealmRecord;
    }

    /**
     * Set
     */

    public void setEbayCatagoryMatchingName(String name) {
        EbayCategory category = mRealm.where(EbayCategory.class).equalTo("name", name).findFirst();
        mRealmRecord.setEbayCategory(category);
    }

    /**
     * Image Management
     */

    private void loadAssociatedPictures() {
        mImages = mRealmRecord.getImages();
        if (mImages == null) {
            mImages = new RealmList<>();
        } else {
            RealmImage.rehydrateList(mImages);
        }
        mImages.add(RealmImage.placeHolderImage(mContext));
    }


    public void setImageAtIndex(RealmImage newImage, int index) {
        RealmImage currentImage = mImages.get(index);
        mImages.set(index, newImage);
        // If we replaced a placeholder image, we need to append a new one
        if (currentImage.isPlaceHolder()) {
            mImages.add(RealmImage.placeHolderImage(mContext));
        }
        refreshUi();
    }

    public void removeImageAtIndex(int index) {
        RealmImage selectedImage = mImages.get(index);
        mImages.remove(index);
        try {
            FileManager.deleteFileAtPath(selectedImage.getPath());
        } catch (NullPointerException e) {
            Logger.logMessage("No pic at path: " + e.toString());
        }

        RealmImage managedImage = mRealm.where(RealmImage.class).equalTo("uuid", selectedImage.getUuid()).findFirst();
        if (managedImage != null) {
            mRealm.beginTransaction();
            managedImage.deleteFromRealm();
            mRealm.commitTransaction();
        }

        refreshUi();
    }

    /**
     * Audio Management
     */

    private void loadAssociatedAudioClips() {
        // Load 10 audio tracks from a combination of saved and blank
        try {
            mAudioTracks.clear();
            for (RealmAudioClip audioClip : mRealmRecord.getAudioClips()) {
                AudioTrack track = new AudioTrack(audioClip.getTitle(), audioClip.getPath(), audioClip.getUuid());
                mAudioTracks.add(track);
            }
            if (mAudioTracks.size() < 10) {
                for (int i = mAudioTracks.size(); i < 10; i++) {
                    mAudioTracks.add(AudioTrack.createEmptyTrack());
                }
            }
        } catch (NullPointerException e) {
            for (int i = 0; i < 10; i++) {
                mAudioTracks.add(AudioTrack.createEmptyTrack());
            }
        }
    }

    public void setAudio(ArrayList<AudioTrack> tracks) {
        mAudioTracks = tracks;
    }

    /**
     * State Management
     */

    public void captureCurrentState() {
        mUpdateUpdateSessionInterface.updateSession(this);
    }

    public void refreshUi() {
        mUpdateUiInterface.updateUI(this);
    }

    /**
     * Validation
     */

    public BoolResponse sessionIsValid() {
        mUpdateUpdateSessionInterface.updateSession(this);

        String message = "";
        boolean valid = true;

        if (mRealmRecord.getTitle().isEmpty()) {
            message += "* Title\n";
            valid = false;
        }

        if (mRealmRecord.getListingTitle() == null || mRealmRecord.getListingTitle().isEmpty()) {
            message += "* Listing Title\n";
            valid = false;
        }

        if (mRealmRecord.getPrice().isEmpty()) {
            message += "* Price\n";
            valid = false;
        }

        if (mImages.size() == 1) {
            RealmImage onlyImage = mImages.get(0);
            if (onlyImage.isPlaceHolder()) {
                message += "* At least 1 Image\n";
                valid = false;
            }
        }
        return new BoolResponse(valid, message);
    }

    public BoolResponse canBuildListingTitle() {
        mUpdateUpdateSessionInterface.updateSession(this);
        String title = mRealmRecord.getTitle();
        return new BoolResponse(!title.isEmpty(), "Add 'Title' to use this feature");
    }

    /**
     * Listing Title
     */

    public String buildListingTitle() {
        mUpdateUpdateSessionInterface.updateSession(this);

        String artist =  mRealmRecord.getArtist();
        String title = mRealmRecord.getTitle();
        String label = mRealmRecord.getLabel();
        String category = mRealmRecord.getEbayCategory().toString();

        String titleString = "";

        if (!artist.isEmpty()) {
            titleString += cleanStringOfUnwantedSpace(artist);
            titleString += " - ";
        }

        if (!title.isEmpty()) {
            titleString += cleanStringOfUnwantedSpace(title);
        }

        if (!label.isEmpty()) {
            titleString += " - (";
            titleString += cleanStringOfUnwantedSpace(label);
            titleString += ")";
        }

        if (!category.isEmpty()) {
            titleString += " - ";
            titleString += cleanStringOfUnwantedSpace(category);
        }

        return titleString;
    }

    /**
     * String Cleaning
     */

    private String cleanStringOfUnwantedSpace(String string) {
        return new String (string.trim().replaceAll(" +", " "));
    }

    private String cleanStringForFileName(String string) {
        return new String (string.replaceAll("\\W+", ""));
    }

    /**
     * Saving to Realm
     */

    public void save() {
        mUpdateUpdateSessionInterface.updateSession(this);
        FileManager fileManager = new FileManager(mContext);

        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(mRealmRecord);
        try {
            mRealmRecord.setImages(saveImages(fileManager));
            removeDeletedAudioTracksFromRealm();
            mRealmRecord.setAudioClips(saveAudioClips(fileManager));
            mRealm.copyToRealmOrUpdate(mRealmRecord);
            mRealm.commitTransaction();
        } catch (Exception e) {
            mRealm.cancelTransaction();
            mErrorInterface.showErrorMessage("Problem saving record: " + e.toString());
        }
    }

    private RealmList<RealmImage> saveImages(FileManager fileManager) throws Exception {
        int counter = 0;

        RealmList<RealmImage> imagesToAttach = new RealmList<>();

        for (RealmImage image : mImages) {
            if (image.isPlaceHolder()) {
                continue;
            }
            // Redo title as user may have changed it
            image.setTitle(cleanStringOfUnwantedSpace(mRealmRecord.getListingTitle()) + "_" + Integer.toString(counter));
            if (!image.isDirty()) {
                // Its not a new image
                imagesToAttach.add(image);
                mRealm.copyToRealmOrUpdate(image);
                continue;
            }
            // Its a new image, lets compress it and write to disc
            File imageFile = fileManager.writeJpegToDisc(image.getImage(),
                    FileManager.getRootPicturesPath(),
                    image.getUuid());
            image.setPath(imageFile.getAbsolutePath());
            image.freeUpMemory();

            mRealm.copyToRealmOrUpdate(image);
            imagesToAttach.add(image);

            counter ++;
        }

        return imagesToAttach;
    }

    private void removeDeletedAudioTracksFromRealm() {
        try {
            RealmList<RealmAudioClip> existingClips = mRealmRecord.getAudioClips();
            for (RealmAudioClip existingClip : existingClips) {
                boolean match = false;
                for (AudioTrack track : mAudioTracks) {
                    if (track.getUuid() != null && track.getUuid().equals(existingClip.getUuid())) {
                        // Our RealmAudioClip still exists in mAudioTracks, lets keep it.
                        match = true;
                        continue;
                    }
                }
                if (match == false) {
                    // Our RealmAudioClip is no longer in mAudioTracks, so lets delete it
                    existingClip.deleteFromRealm();
                }
            }
        } catch (Exception e) {
            Logger.logMessage(e.toString());
        }
    }

    private RealmList<RealmAudioClip> saveAudioClips(FileManager fileManager) throws Exception {
        RealmList<RealmAudioClip> audioClipsToAttach = new RealmList<>();
        int counter = 0;
        for (AudioTrack track : mAudioTracks) {
            if (track.getFilePath() == null) {
                // Track not recorded into
                continue;
            }

            String title = cleanStringOfUnwantedSpace(mRealmRecord.getListingTitle()) + "_" + Integer.toString(counter);

            RealmAudioClip existing = mRealm.where(RealmAudioClip.class).equalTo(RealmAudioClip.PRIMARY_KEY, track.getUuid()).findFirst();
            if (existing != null) {
                // Redo title as user may have changed it
                existing.setTitle(title);
                mRealm.copyToRealmOrUpdate(existing);
                audioClipsToAttach.add(existing);
                continue;
            }

            //** Brand new RealmAudioClip **//

            // Write to app storage
            File soundCLipFile = fileManager.copyFileFromPathToDirectory(track.getFilePath(),
                    FileManager.getRootAudioClipsPath(),
                    cleanStringForFileName(mRealmRecord.getListingTitle() + ".aac"));

            // Add to Realm
            RealmAudioClip realmAudioClip = mRealm.copyToRealm(new RealmAudioClip());
            realmAudioClip.setTitle(title);
            realmAudioClip.setPath(soundCLipFile.getPath());
            audioClipsToAttach.add(realmAudioClip);

            // Delete temp audio file path
            FileManager.deleteFileAtPath(track.getFilePath());
        }

        return audioClipsToAttach;
    }
}
