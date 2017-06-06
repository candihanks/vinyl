package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carl on 06/06/2017.
 */

public class BaseActivity extends AppCompatActivity {

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
}
