package com.carltaylordev.recordlisterandroidclient;

import android.app.Activity;
import android.content.Context;

import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.ImageItem;
import com.carltaylordev.recordlisterandroidclient.models.RealmAudioClip;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import java.io.File;
import java.io.FileNotFoundException;
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

    public interface UpdateInterface {
        void updateSession(RecordSessionManager sessionManager);
        void updateUI(RecordSessionManager sessionManager);
    }

    public interface ErrorInterface {
        void showErrorMessage(String message);
    }

    private ArrayList<ImageItem> mImageCacheList = new ArrayList<>();
    private HashMap<Integer, String> mAudioMap = new HashMap<>();

    private RealmRecord mRealmRecord;
    private Realm mRealm;

    private UpdateInterface mUpdateUpdateInterface;
    private ErrorInterface mErrorInterface;
    private Context mContext;

    public RecordSessionManager(RealmRecord realmRecord, Realm realm, Activity activity) {
        mRealmRecord = realmRecord;
        mRealm = realm;
        mUpdateUpdateInterface = (UpdateInterface) activity;
        mErrorInterface = (ErrorInterface)activity;
        mContext = activity;

        loadAssociatedData();
    }

    /**
     * Test Data
     */

    public void createTestData() {
        RealmResults<RealmRecord> savedRecords = mRealm.where(RealmRecord.class).findAll();
        if (savedRecords.size() > 0) {
            mRealmRecord = mRealm.copyFromRealm(savedRecords.get(savedRecords.size() - 1));
            mRealmRecord.setListingTitle("Test Listing Do Not Buy");
            loadAssociatedData();
            mUpdateUpdateInterface.updateUI(this);
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

        mUpdateUpdateInterface.updateUI(this);
    }

    /**
     * Associated Data
     */

    private void loadAssociatedData() {
        loadAssociatedPictures();
        loadAssociatedAudioClips();
    }

    private void loadAssociatedPictures() {
        mImageCacheList.clear();
        try {
            for (RealmImage image : mRealmRecord.getImages()) {
                try {
                    mImageCacheList.add(image.convertToImageItem());
                } catch (FileNotFoundException e) {
                    mErrorInterface.showErrorMessage("Error loading images for record");
                    break;
                }
            }
        } catch (NullPointerException e) {
            Logger.logMessage("No Images For Record");
        }
        mImageCacheList.add(ImageItem.placeHolderImage(mContext));
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

    public ArrayList<ImageItem> getImages() {
        return mImageCacheList;
    }

    public Map<Integer, String> getAudio() {
        return mAudioMap;
    }

    public String getArtist() {
        return mRealmRecord.getArtist();
    }

    public String getTitle() {
        return mRealmRecord.getTitle();
    }

    public String getLabel() {
        return mRealmRecord.getLabel();
    }

    public String getMediaCondition() {
        return mRealmRecord.getMediaCondition();
    }

    public String getCoverCondition() {
        return mRealmRecord.getCoverCondition();
    }

    public String getComments() {
        return mRealmRecord.getComments();
    }

    public String getPrice() {
        return mRealmRecord.getPrice();
    }

    public String getListingTitle() {
        return mRealmRecord.getListingTitle();
    }

    /**
     * Set
     */

    public void setImageAtIndex(ImageItem imageItem, int index) {
        ImageItem selectedImage = mImageCacheList.get(index);
        mImageCacheList.set(index, imageItem);
        // If we replaced a placeholder image, we need to append a new one
        if (selectedImage.isPlaceHolder()) {
            mImageCacheList.add(ImageItem.placeHolderImage(mContext));
        }
        reloadCurrentRecord();
    }

    public void setImages(ArrayList<ImageItem> images) {
        mImageCacheList = images;
    }

    public void setAudio(Map<Integer, String> audio) {
        for (Map.Entry entry : audio.entrySet()) {
            mAudioMap.put((Integer)entry.getKey(), (String)entry.getValue());
        }
    }

    public void setArtist(String artist) {
        mRealmRecord.setArtist(artist);
    }

    public void setTitle(String title) {
        mRealmRecord.setTitle(title);
    }

    public void setLabel(String label) {
        mRealmRecord.setLabel(label);
    }

    public void setMediaCondition(String condition) {
        mRealmRecord.setMediaCondition(condition);
    }

    public void setCoverCondition(String condition) {
        mRealmRecord.setCoverCondition(condition);
    }

    public void setComments(String comments) {
        mRealmRecord.setComments(comments);
    }

    public void setPrice(String price) {
        mRealmRecord.setPrice(price);
    }

    public void setEbayCategory(EbayCategory ebayCategory) {
        mRealmRecord.setEbayCategory(ebayCategory);
    }

    /**
     * State Management
     */

    public void captureCurrentState() {
        mUpdateUpdateInterface.updateSession(this);
    }

    public void reloadCurrentRecord() {
        mUpdateUpdateInterface.updateUI(this);
    }

    /**
     * Validation
     */

    public BoolResponse recordIsValid() {
        mUpdateUpdateInterface.updateSession(this);

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

        if (mImageCacheList.size() == 1) {
            ImageItem onlyImage = mImageCacheList.get(0);
            if (onlyImage.isPlaceHolder()) {
                message += "At least 1 Image\n";
                valid = false;
            }
        }
        return new BoolResponse(valid, message);
    }

    public BoolResponse canBuildListingTitle() {
        mUpdateUpdateInterface.updateSession(this);
        String title = mRealmRecord.getTitle();
        return new BoolResponse(!title.isEmpty(), "Add 'Title' to use this feature");
    }

    /**
     * Listing Title
     */

    public String buildListingTitle() {
        mUpdateUpdateInterface.updateSession(this);

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
        mUpdateUpdateInterface.updateSession(this);
        final FileManager fileManager = new FileManager(mContext);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(mRealmRecord);
                writeOutAndAttachPictures(fileManager);
                if (mAudioMap != null) {
                    writeOutAndAttachAudioClips(fileManager);
                }
                realm.copyToRealmOrUpdate(mRealmRecord);
            }
        });
    }

    private void writeOutAndAttachPictures(FileManager fileManager) {
        RealmList<RealmImage> realmImages = new RealmList<>();

        int counter = 1;

        for (ImageItem imageItem : mImageCacheList) {
            if (imageItem.isPlaceHolder()) {
                continue;
            }
            try {
                // Write to app storage
                File imageFile = fileManager.writeJpegToDisc(imageItem.getImage(),
                        FileManager.getRootPicturesPath(),
                        cleanStringForFileName(mRealmRecord.getListingTitle()));

                // Add to Realm
                RealmImage realmImage = mRealm.copyToRealm(new RealmImage());
                realmImage.setTitle(cleanStringOfUnwantedSpace(mRealmRecord.getListingTitle()) + "_" + Integer.toString(counter));
                realmImage.setPath(imageFile.getPath());
                realmImages.add(realmImage);

                // Delete temp image
                FileManager.deleteFile(imageItem.getPath());

            } catch (Exception e) {
                mErrorInterface.showErrorMessage("Could not save record: " + e.toString());
            }

            counter ++;
        }

        mRealmRecord.setImages(realmImages);
    }

    private void writeOutAndAttachAudioClips(FileManager fileManager) {
        RealmList<RealmAudioClip> realmAudioClips = new RealmList<>();

        for (int i = 0; i < mAudioMap.size(); i ++) {
            String path = mAudioMap.get(new Integer(i));
            try {
                // Write to app storage
                File soundCLipFile = fileManager.copyAudioClipFromPathToDirectory(path,
                        FileManager.getRootAudioClipsPath(),
                        cleanStringForFileName(mRealmRecord.getListingTitle()));

                // Add to Realm
                RealmAudioClip realmAudioClip = mRealm.copyToRealm(new RealmAudioClip());
                realmAudioClip.setTitle(cleanStringOfUnwantedSpace(mRealmRecord.getListingTitle()) + "_" + Integer.toString(i));
                realmAudioClip.setPath(soundCLipFile.getPath());
                realmAudioClips.add(realmAudioClip);

                // Delete temp image
                FileManager.deleteFile(path);

            } catch (Exception e) {
                mErrorInterface.showErrorMessage("Could not save record: " + e.toString());
            }
        }

        mRealmRecord.setAudioClips(realmAudioClips);
    }
}
