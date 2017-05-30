package com.carltaylordev.recordlisterandroidclient;

import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.Record;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by carl on 29/05/2017.
 */


public class RecordSessionManager {

    public interface Interface {
        void updateRecord(Record record);
        void updateUI(Record record);
    }

    private Record mRecord;
    private Realm mRealm;
    private Interface mUpdateInterface;

    public RecordSessionManager(Record record, Realm realm, Interface updateInterface) {
        mRecord = record;
        mRealm = realm;
        mUpdateInterface = updateInterface;
    }

    /**
     * Associated Data
     */

    public void createTestData() {
        mRecord.setArtist("Dave Clarke");
        mRecord.setTitle("Red 3");
        mRecord.setLabel("Deconstruction");
        mRecord.setMediaCondition("Good Plus (G+)");
        mRecord.setCoverCondition("Good Plus (G+)");
        mRecord.setComments("Here is a great record");

        RealmResults<EbayCategory>results = getAllCategories();
        mRecord.setEbayCategory(results.first());

        mUpdateInterface.updateUI(mRecord);
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
        mUpdateInterface.updateRecord(mRecord);
    }

    public void reloadCurrentRecord() {
        mUpdateInterface.updateUI(mRecord);
    }

    /**
     * Validation
     */

    public BoolResponse recordIsValid() {
        mUpdateInterface.updateRecord(mRecord);
        // check all fields
        return new BoolResponse(true, "You need more stuff");
    }

    public BoolResponse canBuildListingTitle() {
        mUpdateInterface.updateRecord(mRecord);
        String title = mRecord.getTitle();
        return new BoolResponse(!title.isEmpty(), "Add 'Title' to use this feature");
    }

    /**
     * Listing Title
     */

    public String buildListingTitle() {
        mUpdateInterface.updateRecord(mRecord);

        String artist =  mRecord.getArtist();
        String title = mRecord.getTitle();
        String label = mRecord.getLabel();
        String category = mRecord.getEbayCategory().toString();

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
        mUpdateInterface.updateRecord(mRecord);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(mRecord);
        mRealm.commitTransaction();
    }
}
