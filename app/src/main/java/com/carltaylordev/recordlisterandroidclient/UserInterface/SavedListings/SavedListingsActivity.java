package com.carltaylordev.recordlisterandroidclient.UserInterface.SavedListings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.carltaylordev.recordlisterandroidclient.KeyValueStore;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.Server.RecordUploadCoordinator;
import com.carltaylordev.recordlisterandroidclient.UserInterface.BaseActivity;
import com.carltaylordev.recordlisterandroidclient.UserInterface.EditListing.EditListingActivity;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SavedListingsActivity extends BaseActivity implements RecyclerAdapter.RecordHolder.Interface, RecordUploadCoordinator.Interface {

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

        // TODO: 07/06/2017 auto onUploadCountUpdate list on delete? Callback from Realm?
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
            uploadSelectedRows();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Setup
     */

    void setupRecyclerView(List<RealmRecord> records) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new RecyclerAdapter(records, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    List<RealmRecord> getSavedRecords(Realm realm) {
        RealmResults<RealmRecord> records = realm.where(RealmRecord.class).findAll();
        List<RealmRecord> workingCopy = realm.copyFromRealm(records);
        return workingCopy;
    }

    /**
     * Data Operations
     */

    void uploadSelectedRows() {
        KeyValueStore keyValueStore = new KeyValueStore(this);
        String baseUrl = keyValueStore.getStringForKey(KeyValueStore.KEY_BASE_SERVER_URL);
        if (baseUrl.isEmpty()) {
            super.showAlert("Attention", "Please Set base url in Settings");
            return;
        }

        List<RealmRecord> records = mAdapter.getSelectedItems();
        if (records.size() > 0) {
            super.showHorizontalProgressDialog("Uploading to server", records.size());
            RecordUploadCoordinator coordinator = new RecordUploadCoordinator(baseUrl, records, mRealm, this, this);
            coordinator.tryNextUpload();
        } else {
            showToast("Select Records to Upload");
        }
    }

    /**
     * RecyclerView Interface
     */

    @Override
    public void editRecord(String uuid) {
        Intent intent = new Intent(this, EditListingActivity.class);
        intent.putExtra(EditListingActivity.EXTRA_KEY_UUID, uuid);
        startActivity(intent);
    }

    /**
     * Record Upload Coordinator Interface
     */

    @Override
    public void onUploadCountUpdate(int uploadCount) {
        super.updateHorizontalProgressDialog(uploadCount);
    }

    @Override
    public void onFinished(BoolResponse response) {
        super.hideProgressDialog();
        if (response.isTrue()) {
            super.showToast(response.getUserMessage());
        } else {
            super.showAlert("Info:", response.getUserMessage());
        }
    }
}
