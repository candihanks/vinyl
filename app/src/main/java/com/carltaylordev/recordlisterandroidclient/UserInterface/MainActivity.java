package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.carltaylordev.recordlisterandroidclient.R;

public class MainActivity extends BaseActivity {

    /**
     * Activity LifeCycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        setupButtons();
    }

    /**
     * Setup
     */

    private void setupButtons() {
        Button newListingButton = (Button)findViewById(R.id.new_listing_button);
        newListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewListing();
            }
        });
    }

    private void createNewListing() {
        Intent intent = new Intent(this, ListingActivity.class);
        intent.putExtra(ListingActivity.EXTRA_KEY_UUID, "");
        startActivity(intent);
    }
}
