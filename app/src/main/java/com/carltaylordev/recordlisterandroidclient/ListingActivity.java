package com.carltaylordev.recordlisterandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ListingActivity extends AppCompatActivity {

    ArrayList<String> styleCatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_activity);

        setupSpinner();
    }

    // Setup
    void setupSpinner() {
        styleCatList.add("Motown");
        styleCatList.add("Northern Soul");
        styleCatList.add("Jazz");
        styleCatList.add("Funk");
        // TODO: replace with DB fetch

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, styleCatList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner)findViewById(R.id.listing_activity_spinner_cats);
        spinner.setAdapter(spinnerArrayAdapter);
    }
}
