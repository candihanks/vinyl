package com.carltaylordev.recordlisterandroidclient.UserInterface.EditListing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.UserInterface.BaseActivity;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class EditListingActivity extends BaseActivity implements RecordSessionManager.UpdateSessionInterface,
        RecordSessionManager.ErrorInterface, RecordSessionManager.UpdateUiInterface {

    public static final String EXTRA_KEY_UUID = "EXTRA_KEY_UUID";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    List<WeakReference<Fragment>> mFragList = new ArrayList<>();

    public RecordSessionManager mRecordSessionManager;

    private static final int REQUEST_PERMISSIONS = 200;
    private boolean permissionsAccepted = false;
    private String [] permissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Activity LifeCycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_listing_activity);

        requestPermissions();

        setupViewPager();
        setupSaveFab();
        setupTestFab();

        Intent intent = getIntent();
        final String uuid = intent.getStringExtra(EXTRA_KEY_UUID);
        if (!uuid.isEmpty()) {
            showProgressDialog("Loading Record");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                // allows progress dialog to get into run loop
                startSession(getRecordForUuid(uuid));
                hideProgressDialog();
            }
        }, 100);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        captureSessionState();
    }

    @Override
    protected void onResume() {
        refreshUi();
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSIONS:
                permissionsAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionsAccepted) {
            showAlert("Oops", "You need to accept the permissions else we can't record audio or take pics");
            requestPermissions();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachFragment (Fragment fragment) {
        mFragList.add(new WeakReference(fragment));
        super.onAttachFragment(fragment);
    }

    /**
     * Setup
     */

    private RealmRecord getRecordForUuid(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        RealmRecord record = realm.where(RealmRecord.class).equalTo(RealmRecord.PRIMARY_KEY, uuid).findFirst();
        if (record == null) {
            return new RealmRecord();
        } else {
            // Create working copy so we can manipulate outside write transactions
            RealmRecord workingCopy = realm.copyFromRealm(record);
            return workingCopy;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
    }

    private void setupViewPager() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupSaveFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoolResponse response = mRecordSessionManager.sessionIsValid();
                if (response.isTrue()) {
                    mRecordSessionManager.save();
                    showSnackBar(view, "Record Saved");
                } else {
                    showAlert("You need to add:", response.getUserMessage());
                }
            }
        });
    }

    private void setupTestFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.test_data_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordSessionManager.createTestData();
            }
        });
    }

    /**
     * Session Management
     */

    private void startSession(RealmRecord record) {
        if (mRecordSessionManager == null) {
            mRecordSessionManager = new RecordSessionManager(record, Realm.getDefaultInstance(), this);
        }
    }

    private void captureSessionState() {
        try {
            mRecordSessionManager.captureCurrentState();
        } catch (NullPointerException e) {}
    }

    private void refreshUi() {
        try {
            mRecordSessionManager.refreshUi();
        } catch (NullPointerException e) {}
    }

    /**
     * RecordSessionManager Interfaces
     */

    @Override
    public void updateSession(RecordSessionManager sessionManager) {
        List<Fragment> fragments = super.getActiveFragments(mFragList);
        for (Fragment frag : fragments) {
            if (frag instanceof RecordSessionManager.UpdateSessionInterface) {
                ((RecordSessionManager.UpdateSessionInterface) frag).updateSession(sessionManager);
            }
        }
    }

    @Override
    public void updateUI(RecordSessionManager sessionManager) {
        List<Fragment> fragments = super.getActiveFragments(mFragList);
        for (Fragment frag : fragments) {
            if (frag instanceof RecordSessionManager.UpdateUiInterface) {
                ((RecordSessionManager.UpdateUiInterface) frag).updateUI(sessionManager);
            }
        }
    }

    @Override
    public void showErrorMessage(String message) {
        super.showAlert("Error", message);
    }

    /**
     * PagerAdapter
     */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Info1Fragment.newInstance();
                case 1:
                    return Info2Fragment.newInstance();
                case 2:
                    return PhotosFragment.newInstance();
                case 3:
                    return AudioFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Info 1";
                case 1:
                    return "Info 2";
                case 2:
                    return "Photos";
                case 3:
                    return "Audio";
            }
            return null;
        }
    }
}
