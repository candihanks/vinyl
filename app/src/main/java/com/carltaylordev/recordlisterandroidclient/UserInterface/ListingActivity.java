package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.RecordSessionManager;
import com.carltaylordev.recordlisterandroidclient.SetupSingleton;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ListingActivity extends AppCompatActivity implements RecordSessionManager.UpdateSessionInterface,
        RecordSessionManager.ErrorInterface, RecordSessionManager.UpdateUiInterface {

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
        setContentView(R.layout.listing_activity);

        SetupSingleton.setup(this);

        requestPermissions();

        mRecordSessionManager = new RecordSessionManager(new RealmRecord(), Realm.getDefaultInstance(), this);

        setupViewPager();
        setupSaveFab();
        setupTestFab();
    }

    @Override
    protected void onPause() {
        mRecordSessionManager.captureCurrentState();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mRecordSessionManager.refreshUi();
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
            showAlert("Oops", "You need to accept the Permissions");
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
     * Helpers
     */

    public List<Fragment> getActiveFragments() {
        ArrayList<Fragment> ret = new ArrayList<>();
        for(WeakReference<Fragment> ref : mFragList) {
            Fragment f = ref.get();
            if(f != null) {
                if(f.isVisible()) {
                    ret.add(f);
                }
            }
        }
        return ret;
    }


    /**
     * RecordSessionManager Interfaces
     */

    @Override
    public void updateSession(RecordSessionManager sessionManager) {
        List<Fragment> fragments = getActiveFragments();
        for (Fragment frag : fragments) {
            if (frag instanceof RecordSessionManager.UpdateSessionInterface) {
                ((RecordSessionManager.UpdateSessionInterface) frag).updateSession(sessionManager);
            } else {
                Logger.logMessage("Fragment does not implement 'updateSession' interface");
            }
        }
    }

    @Override
    public void updateUI(RecordSessionManager sessionManager) {
        List<Fragment> fragments = getActiveFragments();
        for (Fragment frag : fragments) {
            if (frag instanceof RecordSessionManager.UpdateUiInterface) {
                ((RecordSessionManager.UpdateUiInterface) frag).updateUI(sessionManager);
            } else {
                Logger.logMessage("Fragment does not implement 'updateUI' interface");
            }
        }
    }

    @Override
    public void showErrorMessage(String message) {
        showAlert("Error", message);
    }

    /**
     * User Notifications
     */

    void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
