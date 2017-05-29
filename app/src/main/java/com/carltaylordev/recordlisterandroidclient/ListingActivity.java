package com.carltaylordev.recordlisterandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.Record;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListingActivity extends AppCompatActivity {

    Spinner mStyleCatSpinner;

    EditText mArtistEditText;
    EditText mTitleEditText;
    EditText mLabelEditText;
    EditText mListingTitleEditText;
    EditText mNotesEditText;

    private Realm mRealm;

    // - Activity Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_activity);

        mRealm = Realm.getDefaultInstance();

//        setupEditTexts();
//        setupSpinner();
//        setupConcatButton();
//        setUpSaveButton();
    }

//    // - Setup
//    void setupSpinner() {
//        RealmResults cats = EbayCategory.getAll();
//        mStyleCatSpinner = (Spinner)findViewById(R.id.listing_activity_spinner_cats);
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cats);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mStyleCatSpinner.setAdapter(spinnerArrayAdapter);
//    }
//
//    void setupEditTexts() {
//        mArtistEditText = (EditText)findViewById(R.id.listing_activity_edit_text_artist);
//        mTitleEditText = (EditText)findViewById(R.id.listing_activity_edit_text_title);
//        mLabelEditText = (EditText)findViewById(R.id.listing_activity_edit_text_label);
//        mListingTitleEditText = (EditText)findViewById(R.id.listing_activity_edit_text_listing_title);
//        mNotesEditText = (EditText)findViewById(R.id.listing_activity_edit_text_notes);
//    }
//
//    void setupConcatButton() {
//        ImageButton imageButton = (ImageButton)findViewById(R.id.listing_activity_image_button_concat);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String title = mTitleEditText.getText().toString();
//                if (title.length() == 0) {
//                    showToast("Please add a record title to use to this feature");
//                } else {
//                   mListingTitleEditText.setText(createListingTitle());
//                }
//            }
//        });
//    }
}
