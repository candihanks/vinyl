package com.carltaylordev.recordlisterandroidclient;

import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.Record;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by carl on 29/05/2017.
 */


public class RecordSessionManager {

    public interface Interface {
        void updateRecord(Record record);
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
        boolean valid = mRecord.getArtist() != null && mRecord.getArtist() != null;
        return new BoolResponse(valid, "Sort it out barry");
    }

    /**
     * Build Listing Title
     */

    public String buildListingTitle() {
        mUpdateInterface.updateRecord(mRecord);
        // todo: sort this so it concatenates relevant values
        return mRecord.getEbayCategory().toString() + " - " + mRecord.getTitle();
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
