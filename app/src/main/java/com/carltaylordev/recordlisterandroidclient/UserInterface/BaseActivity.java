package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.UserInterface.Settings.SettingsActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carl on 06/06/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    /**
     * Menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Helpers
     */

    public List<Fragment> getActiveFragments(List<WeakReference<Fragment>> fragments) {
        ArrayList<Fragment> active = new ArrayList<>();
        for(WeakReference<Fragment> weakReference : fragments) {
            Fragment fragment = weakReference.get();
            if(fragment != null) {
                if(fragment.isVisible()) {
                    active.add(fragment);
                }
            }
        }
        return active;
    }

    /**
     * Progress
     */

    public void showProgressDialog(String message) {
        mProgressDialog= new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    public void showHorizontalProgressDialog(String message, int max) {
        mProgressDialog= new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(max);
        mProgressDialog.show();
    }

    public void updateHorizontalProgressDialog(int update) {
        try {
            mProgressDialog.setProgress(update);
        } catch (NullPointerException e) {}
    }

    public void hideProgressDialog() {
        try {
            mProgressDialog.hide();
            mProgressDialog = null;
        } catch (NullPointerException e) {}
    }

    /**
     * User Notifications
     */

   public void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    public  void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
