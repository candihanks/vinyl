package com.carltaylordev.recordlisterandroidclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by carl on 29/05/2017.
 */


public class RecordSessionManager {

    public interface Interface {
        void updateRecord(RealmRecord realmRecord);
        void updateUI(RealmRecord realmRecord);
    }

    private RealmRecord mRealmRecord;
    private Realm mRealm;
    private Interface mUpdateInterface;

    public RecordSessionManager(RealmRecord realmRecord, Realm realm, Interface updateInterface) {
        mRealmRecord = realmRecord;
        mRealm = realm;
        mUpdateInterface = updateInterface;
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

        mUpdateInterface.updateUI(mRealmRecord);
    }

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

    /**
     * State Management
     */

    public void captureCurrentState() {
        mUpdateInterface.updateRecord(mRealmRecord);
    }

    public void reloadCurrentRecord() {
        mUpdateInterface.updateUI(mRealmRecord);
    }

    /**
     * Validation
     */

    public BoolResponse recordIsValid() {
        mUpdateInterface.updateRecord(mRealmRecord);
        // check all fields
        return new BoolResponse(true, "You need more stuff");
    }

    public BoolResponse canBuildListingTitle() {
        mUpdateInterface.updateRecord(mRealmRecord);
        String title = mRealmRecord.getTitle();
        return new BoolResponse(!title.isEmpty(), "Add 'Title' to use this feature");
    }

    /**
     * Listing Title
     */

    public String buildListingTitle() {
        mUpdateInterface.updateRecord(mRealmRecord);

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
        mUpdateInterface.updateRecord(mRealmRecord);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(mRealmRecord);
        mRealm.commitTransaction();
    }
}
