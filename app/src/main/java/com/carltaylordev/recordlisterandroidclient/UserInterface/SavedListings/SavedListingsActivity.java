package com.carltaylordev.recordlisterandroidclient.UserInterface.SavedListings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.UserInterface.BaseActivity;
import com.carltaylordev.recordlisterandroidclient.UserInterface.EditListing.EditListingActivity;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import io.realm.Realm;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saved_listings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_upload_selected) {

            // upload on back thread
            return true;
        }

        return super.onOptionsItemSelected(item);
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
     * Data Operations
     */

    void getSelectedRows() {

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
