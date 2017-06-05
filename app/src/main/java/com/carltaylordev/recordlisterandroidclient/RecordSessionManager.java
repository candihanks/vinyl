package com.carltaylordev.recordlisterandroidclient;

import android.app.Activity;
import android.content.Context;

import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.RealmAudioClip;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

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
    private HashMap<Integer, String> mAudioMap = new HashMap<>();

    private RealmRecord mRealmRecord;
    private Realm mRealm;

    private UpdateSessionInterface mUpdateUpdateSessionInterface;
    private UpdateUiInterface mUpdateUiInterface;
    private ErrorInterface mErrorInterface;
    private Context mContext;

    public RecordSessionManager(RealmRecord realmRecord, Realm realm, Activity activity) {
        mRealmRecord = realmRecord;
        mRealm = realm;
        mUpdateUpdateSessionInterface = (UpdateSessionInterface) activity;
        mUpdateUiInterface = (UpdateUiInterface) activity;
        mErrorInterface = (ErrorInterface) activity;
        mContext = activity;

        loadAssociatedData();
    }

    /**
     * Test Data
     */

    public void createTestData() {
        RealmResults<RealmRecord> savedRecords = mRealm.where(RealmRecord.class).findAll();
        if (savedRecords.size() > 0) {
            // get last record
            mRealmRecord = mRealm.copyFromRealm(savedRecords.get(savedRecords.size() - 1));
            mRealmRecord.setListingTitle("Test Listing Do Not Buy");
            loadAssociatedData();
            mUpdateUiInterface.updateUI(this);
            return;
        }

        mRealmRecord.setArtist("Dave Clarke");
        mRealmRecord.setTitle("Red 3");
        mRealmRecord.setLabel("Deconstruction");
        mRealmRecord.setMediaCondition("Good Plus (G+)");
        mRealmRecord.setCoverCondition("Good Plus (G+)");
        mRealmRecord.setComments("Here is a great record");
        mRealmRecord.setListingTitle("Test Listing Do Not Buy");
        mRealmRecord.setPrice("9.99");

        RealmResults<EbayCategory>results = getAllCategories();
        mRealmRecord.setEbayCategory(results.first());

        mUpdateUiInterface.updateUI(this);
    }

    /**
     * Associated Data
     */

    private void loadAssociatedData() {
        loadAssociatedPictures();
//        loadAssociatedAudioClips();
    }

    private void loadAssociatedPictures() {
        mImages = mRealmRecord.getImages();
        if (mImages == null) {
            mImages = new RealmList<>();
        } else {
            RealmImage.rehydrateList(mImages);
        }
        mImages.add(RealmImage.placeHolderImage(mContext));
    }

    private void loadAssociatedAudioClips() {
        int counter = 0;
        try {
            for (RealmAudioClip audioClip : mRealmRecord.getAudioClips()) {
                mAudioMap.put(new Integer(counter), audioClip.getPath());
                counter ++;
            }
        } catch (NullPointerException e) {
            Logger.logMessage("No Sound Clips For Record");
        }
    }

    /**
     * Get
     */

    public static RealmResults<EbayCategory> getAllCategories() {
        return EbayCategory.getAll();
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
        list.add("Poor (P),");
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

    public Map<Integer, String> getAudio() {
        return mAudioMap;
    }

    public RealmRecord getRecord() {
        return mRealmRecord;
    }

    /**
     * Set
     */

    public void setImageAtIndex(RealmImage newImage, int index) {
        RealmImage currentImage = mImages.get(index);
        mImages.set(index, newImage);
        // If we replaced a placeholder image, we need to append a new one
        if (currentImage.isPlaceHolder()) {
            mImages.add(RealmImage.placeHolderImage(mContext));
        }
        reloadCurrentRecord();
    }

    public void setAudio(Map<Integer, String> audio) {
        mAudioMap = new HashMap<>();
        for (Map.Entry entry : audio.entrySet()) {
            mAudioMap.put((Integer)entry.getKey(), (String)entry.getValue());
        }
    }

    /**
     * State Management
     */

    public void captureCurrentState() {
        mUpdateUpdateSessionInterface.updateSession(this);
    }

    public void reloadCurrentRecord() {
        mUpdateUiInterface.updateUI(this);
    }

    /**
     * Validation
     */

    public BoolResponse recordIsValid() {
        mUpdateUpdateSessionInterface.updateSession(this);

        String message = "";
        boolean valid = true;

        if (mRealmRecord.getTitle().isEmpty()) {
            message += "Title\n";
            valid = false;
        }

        if (mRealmRecord.getListingTitle() == null || mRealmRecord.getListingTitle().isEmpty()) {
            message += "Listing title\n";
            valid = false;
        }

        if (mRealmRecord.getPrice().isEmpty()) {
            message += "Price\n";
            valid = false;
        }

        if (mImages.size() == 1) {
            RealmImage onlyImage = mImages.get(0);
            if (onlyImage.isPlaceHolder()) {
                message += "At least 1 Image\n";
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
        }

        if (!title.isEmpty()) {
            titleString += " - ";
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
     * Saving
     */

    public void save() {
        mUpdateUpdateSessionInterface.updateSession(this);
        FileManager fileManager = new FileManager(mContext);

        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(mRealmRecord);
        try {
            mRealmRecord.setImages(saveImages(fileManager));
            mRealm.copyToRealmOrUpdate(mRealmRecord);
        } catch (Exception e) {
            mRealm.cancelTransaction();
            mErrorInterface.showErrorMessage("Problem saving record: " + e.toString());
        }

        mRealm.commitTransaction();
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
            // Its a new image
            File imageFile = fileManager.writeJpegToDisc(image.getImage(),
                    FileManager.getRootPicturesPath(),
                    image.getUuid());
            image.setPath(imageFile.getAbsolutePath());

            mRealm.copyToRealmOrUpdate(image);
            imagesToAttach.add(image);
        }

        return imagesToAttach;
    }

    private void writeOutAndAttachAudioClips(FileManager fileManager, Realm realm, RealmRecord record) throws Exception {
        try {
            // Delete all existing clips
            RealmList<RealmAudioClip> existingClips = record.getAudioClips();
            for (RealmAudioClip existingClip : existingClips) {
                try {
                    existingClip.deleteFromRealm();
                } catch (Exception e) {
                    Logger.logMessage(e.toString());
                }
            }
            existingClips.clear();
        } catch (Exception e) {
            Logger.logMessage(e.toString());
        }

        // Add New
        RealmList<RealmAudioClip> newAudioClips = new RealmList<>();

        for (int i = 0; i < mAudioMap.size(); i ++) {
            String path = mAudioMap.get(new Integer(i));
            if (path == null || path.isEmpty()) {
                continue;
            }
            // Write to app storage
            File soundCLipFile = fileManager.copyAudioClipFromPathToDirectory(path,
                    FileManager.getRootAudioClipsPath(),
                    cleanStringForFileName(record.getListingTitle()));

            // Add to Realm
            RealmAudioClip realmAudioClip = realm.copyToRealm(new RealmAudioClip());
            realmAudioClip.setTitle(cleanStringOfUnwantedSpace(record.getListingTitle()) + "_" + Integer.toString(i));
            realmAudioClip.setPath(soundCLipFile.getPath());
            newAudioClips.add(realmAudioClip);

            // Delete original audio file path
            FileManager.deleteFileAtPath(path);
        }

        // Set New Clips
        record.setAudioClips(newAudioClips);
    }
}
