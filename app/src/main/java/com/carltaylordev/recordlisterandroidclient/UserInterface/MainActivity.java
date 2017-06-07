package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.UserInterface.EditListing.EditListingActivity;
import com.carltaylordev.recordlisterandroidclient.UserInterface.SavedListings.SavedListingsActivity;

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

        Button savedListingsButton = (Button)findViewById(R.id.saved_listings_button);
        savedListingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSavedListings();
            }
        });
    }

    /**
     * Intents
     */

    private void createNewListing() {
        Intent intent = new Intent(this, EditListingActivity.class);
        intent.putExtra(EditListingActivity.EXTRA_KEY_UUID, "");
        startActivity(intent);
    }

    private void viewSavedListings() {
        Intent intent = new Intent(this, SavedListingsActivity.class);
        startActivity(intent);
    }
}
