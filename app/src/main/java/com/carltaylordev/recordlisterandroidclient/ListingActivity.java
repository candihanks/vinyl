package com.carltaylordev.recordlisterandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListingActivity extends AppCompatActivity {

    ArrayList<String> mStyleCatList = new ArrayList<>();

    Spinner mStyleCatSpinner;

    EditText mArtistEditText;
    EditText mTitleEditText;
    EditText mLabelEditText;
    EditText mListingTitleEditText;
    EditText mNotesEditText;

    // Activity Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_activity);

        setupEditTexts();
        setupSpinner();
        setupConcatButton();
    }

    // Setup
    void setupSpinner() {
        mStyleCatList.add("Motown");
        mStyleCatList.add("Northern Soul");
        mStyleCatList.add("Jazz");
        mStyleCatList.add("Funk");
        // TODO: replace with DB fetch
        mStyleCatSpinner = (Spinner)findViewById(R.id.listing_activity_spinner_cats);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mStyleCatList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStyleCatSpinner.setAdapter(spinnerArrayAdapter);
    }

    void setupEditTexts() {
        mArtistEditText = (EditText)findViewById(R.id.listing_activity_edit_text_artist);
        mTitleEditText = (EditText)findViewById(R.id.listing_activity_edit_text_title);
        mLabelEditText = (EditText)findViewById(R.id.listing_activity_edit_text_label);
        mListingTitleEditText = (EditText)findViewById(R.id.listing_activity_edit_text_listing_title);
        mNotesEditText = (EditText)findViewById(R.id.listing_activity_edit_text_notes);
    }

    void setupConcatButton() {
        ImageButton imageButton = (ImageButton)findViewById(R.id.listing_activity_image_button_concat);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleEditText.getText().toString();
                if (title.length() == 0) {
                    showToast("Please add a record title to use to this feature");
                } else {
                   mListingTitleEditText.setText(createListingTitle());
                }
            }
        });
    }

    // String
    String createListingTitle() {
        String aritst = mArtistEditText.getText().toString();
        String title = mTitleEditText.getText().toString();
        String label = mLabelEditText.getText().toString();
        String styleCat = mStyleCatSpinner.getSelectedItem().toString();
        // TODO: 06/05/17
        // get format
        // remove illegal chars

        //

        return styleCat;
    }

    // Alerts
    void showAlert(String title, String message) {

    }

    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
