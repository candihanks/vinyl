package com.carltaylordev.recordlisterandroidclient;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;
import com.carltaylordev.recordlisterandroidclient.models.Record;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ListingActivity extends AppCompatActivity implements RecordSessionManager.Interface {

    public static final String MY_LOG_TAG = "MY_LOG_TAG";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    List<WeakReference<Fragment>> fragList = new ArrayList<>();

    public RecordSessionManager mRecordSessionManager;

    /**
     * Activity LifeCycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_activity);

        Realm realm = Realm.getDefaultInstance();
        mRecordSessionManager = new RecordSessionManager(new Record(),  realm, this);

        setupViewPager();
        setupSaveFab();
        setupTestFab();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecordSessionManager.captureCurrentState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecordSessionManager.reloadCurrentRecord();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachFragment (Fragment fragment) {
        fragList.add(new WeakReference(fragment));
        super.onAttachFragment(fragment);
    }

    /**
     * Setup
     */

    private void setupViewPager() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupSaveFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoolResponse response = mRecordSessionManager.recordIsValid();
                if (response.isTrue()) {
                    mRecordSessionManager.save();
                    showSnackBar(view, "Record Saved");
                } else {
                    showToast(response.getUserMessage());
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
        for(WeakReference<Fragment> ref : fragList) {
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
     * ListingCoordinator Interface
     */

    @Override
    public void updateRecord(Record record) {
        List<Fragment> fragments = getActiveFragments();
        for (Fragment frag : fragments) {
            if (frag instanceof RecordSessionManager.Interface) {
                ((RecordSessionManager.Interface) frag).updateRecord(record);
            } else {
                Log.d(MY_LOG_TAG, "Fragment does not implement 'updateRecord' interface");
            }
        }
    }

    @Override
    public void updateUI(Record record) {
        List<Fragment> fragments = getActiveFragments();
        for (Fragment frag : fragments) {
            if (frag instanceof RecordSessionManager.Interface) {
                ((RecordSessionManager.Interface) frag).updateUI(record);
            } else {
                Log.d(MY_LOG_TAG, "Fragment does not implement 'updateUI' interface");
            }
        }
    }

    /**
     * Alerts
     */

    void showAlert(String title, String message) {

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
                    return Info2Fragment.newInstance();
                case 3:
                    return Info2Fragment.newInstance();
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
