package com.carltaylordev.recordlisterandroidclient.UserInterface.SavedListings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.UserInterface.BaseActivity;
import com.carltaylordev.recordlisterandroidclient.UserInterface.EditListing.EditListingActivity;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class SavedListingsActivity extends BaseActivity implements RecyclerAdapter.RecordHolder.Interface {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerAdapter mAdapter;
    private Realm mRealm;

    /**
     * Activity LifeCycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_listings_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRealm = Realm.getDefaultInstance();
        setupRecyclerView(getSavedRecords(mRealm));

        // TODO: 07/06/2017 auto update list on delete? Callback from Realm?
    }

    /**
     * Setup
     */

    void setupRecyclerView(RealmResults<RealmRecord> records) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new RecyclerAdapter(records, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    RealmResults<RealmRecord> getSavedRecords(Realm realm) {
        RealmResults<RealmRecord> records = realm.where(RealmRecord.class).findAll();
        return records;
    }

    /**
     * Interface
     */

    @Override
    public void editRecord(String uuid) {
        Intent intent = new Intent(this, EditListingActivity.class);
        intent.putExtra(EditListingActivity.EXTRA_KEY_UUID, uuid);
        startActivity(intent);
    }
}
