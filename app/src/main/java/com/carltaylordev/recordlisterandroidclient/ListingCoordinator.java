package com.carltaylordev.recordlisterandroidclient;

import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.Record;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by carl on 29/05/2017.
 */

public class ListingCoordinator {

    private Record mRecord;
    private Realm mRealm;

    public ListingCoordinator(Record record, Realm realm) {
        mRecord = record;
        mRealm = realm;
    }

    // - Validation
    public BoolResponse recordIsValid() {
        return new BoolResponse(false, "You need more stuff");
    }

    public BoolResponse canBuildListingTitle() {
        boolean valid = mRecord.getArtist() != null && mRecord.getArtist() != null;
        return new BoolResponse(valid, "Sort it out barry");
    }

    public String buildListingTitle() {
        // todo: sort this so it concatenates relevant values
        return mRecord.getEbayCategory().toString() + " - " + mRecord.getTitle();
    }

    public RealmResults<EbayCategory> getAllCategories() {
        return EbayCategory.getAll();
    }

    // - Saving
    public void save() {
//        EbayCategory ebayCategory = (EbayCategory)mStyleCatSpinner.getSelectedItem();
//        mRealm.beginTransaction();
//
//
//        record.setArtist(mArtistEditText.getText().toString());
//        record.setTitle(mTitleEditText.getText().toString());
//        record.setLabel(mLabelEditText.getText().toString());
//        record.setListingTitle(mListingTitleEditText.getText().toString());
//        record.setEbayCategory(ebayCategory);
//
//        mRealm.copyToRealm(record);
//        mRealm.commitTransaction();
    }
}
