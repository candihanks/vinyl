package com.carltaylordev.recordlisterandroidclient;

import android.content.Context;
import android.graphics.Bitmap;

import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.ImageItem;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by carl on 29/05/2017.
 */


public class RecordSessionManager {

    public interface Interface {
        void updateSession(RecordSessionManager sessionManager);
        void updateUI(RecordSessionManager sessionManager);
    }

    private ArrayList<ImageItem> mImageCacheList = new ArrayList<>();
    private RealmRecord mRealmRecord;
    private Realm mRealm;
    private Interface mUpdateInterface;
    private Context mContext;

    public RecordSessionManager(RealmRecord realmRecord, Realm realm, Interface updateInterface, Context context) {
        mRealmRecord = realmRecord;
        mRealm = realm;
        mUpdateInterface = updateInterface;
        mContext = context;

        if (mRealmRecord.getImages() != null) {
            for (RealmImage image : mRealmRecord.getImages()) {
                // get image from disc
//            mImageCacheList.add(new ImageItem())
            }
        }
    }

    /**
     * Associated Data
     */

    public void createTestData() {
        mRealmRecord.setArtist("Dave Clarke");
        mRealmRecord.setTitle("Red 3");
        mRealmRecord.setLabel("Deconstruction");
        mRealmRecord.setMediaCondition("Good Plus (G+)");
        mRealmRecord.setCoverCondition("Good Plus (G+)");
        mRealmRecord.setComments("Here is a great record");
        mRealmRecord.setPrice("9.99");

        RealmResults<EbayCategory>results = getAllCategories();
        mRealmRecord.setEbayCategory(results.first());

        mUpdateInterface.updateUI(this);
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

    public void addImageToCache(ImageItem imageItem) {
        mImageCacheList.add(imageItem);
    }

    public void removeImagesFromCache() {
        mImageCacheList = new ArrayList<>();
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
        mUpdateInterface.updateSession(this);
    }

    public void reloadCurrentRecord() {
        mUpdateInterface.updateUI(this);
    }

    /**
     * Validation
     */

    public BoolResponse recordIsValid() {
        mUpdateInterface.updateSession(this);
        // check all fields
        return new BoolResponse(true, "You need more stuff");
    }

    public BoolResponse canBuildListingTitle() {
        mUpdateInterface.updateSession(this);
        String title = mRealmRecord.getTitle();
        return new BoolResponse(!title.isEmpty(), "Add 'Title' to use this feature");
    }

    /**
     * Listing Title
     */

    public String buildListingTitle() {
        mUpdateInterface.updateSession(this);

        String artist =  mRealmRecord.getArtist();
        String title = mRealmRecord.getTitle();
        String label = mRealmRecord.getLabel();
        String category = mRealmRecord.getEbayCategory().toString();

        String titleString = "";

        if (!artist.isEmpty()) {
            titleString += cleanString(artist);
        }

        if (!title.isEmpty()) {
            titleString += " - ";
            titleString += cleanString(title);
        }

        if (!label.isEmpty()) {
            titleString += " - (";
            titleString += cleanString(label);
            titleString += ")";
        }

        if (!category.isEmpty()) {
            titleString += " - ";
            titleString += cleanString(category);
        }

        return titleString;
    }

    private String cleanString(String string) {
        String withoutUnwantedSpace = string.trim().replaceAll(" +", " ");
        return withoutUnwantedSpace;
    }

    /**
     * Save
     */

    public void save() {
        for (ImageItem imageItem : mImageCacheList) {
            if (imageItem.isPlaceHolder()) {
                continue;
            }
            File file = writeImage(imageItem.getImage());
            RealmImage realmImage = new RealmImage();
            realmImage.setTitle(imageItem.getTitle());
            realmImage.setPath(file.getAbsolutePath());
        }

        mUpdateInterface.updateSession(this);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(mRealmRecord);
        mRealm.commitTransaction();
    }

    private File writeImage(Bitmap imageBitmap) {
        FileManager fileManager = new FileManager(mContext);
        try {
            File file = fileManager.writeTempImageFile(imageBitmap);
            // // TODO: 31/05/2017 write perm file
            return file;
        } catch (Exception e) {
            Logger.logMessage("Exception writing Image File: " + e.toString());
            return null;
        }
    }
}
