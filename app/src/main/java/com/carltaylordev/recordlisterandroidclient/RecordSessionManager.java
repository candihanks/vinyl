package com.carltaylordev.recordlisterandroidclient;

import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.Record;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import java.util.Map;

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

    public RealmResults<EbayCategory> getAllCategories() {
        return EbayCategory.getAll();
    }

    public void createTestData() {
        mRecord.setArtist("Dave     Clarke");
        mRecord.setTitle("Red 3");
        mRecord.setLabel("Deconstruction");

        RealmResults<EbayCategory>results = getAllCategories();
        mRecord.setEbayCategory(results.first());

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
        string.replace(" ", "");
        return string;
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
