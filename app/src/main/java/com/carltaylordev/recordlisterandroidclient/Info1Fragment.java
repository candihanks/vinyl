package com.carltaylordev.recordlisterandroidclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by carl on 29/05/2017.
 */

public class Info1Fragment extends android.support.v4.app.Fragment {

    Spinner mStyleCatSpinner;

    EditText mArtistEditText;
    EditText mTitleEditText;
    EditText mLabelEditText;
    EditText mListingTitleEditText;
    EditText mNotesEditText;

    private Realm mRealm;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public Info1Fragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Info1Fragment newInstance(int sectionNumber) {
        Info1Fragment fragment = new Info1Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info_1_fragment, container, false);

        mRealm = Realm.getDefaultInstance();

        setupEditTexts(rootView);
        setupSpinner(rootView);
        setupConcatButton(rootView);

        return rootView;
    }

    // - Setup
    void setupSpinner(View view) {
        RealmResults cats = EbayCategory.getAll();
        mStyleCatSpinner = (Spinner)view.findViewById(R.id.listing_activity_spinner_cats);
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cats);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mStyleCatSpinner.setAdapter(spinnerArrayAdapter);
    }

    void setupEditTexts(View view) {
        mArtistEditText = (EditText)view.findViewById(R.id.listing_activity_edit_text_artist);
        mTitleEditText = (EditText)view.findViewById(R.id.listing_activity_edit_text_title);
        mLabelEditText = (EditText)view.findViewById(R.id.listing_activity_edit_text_label);
        mListingTitleEditText = (EditText)view.findViewById(R.id.listing_activity_edit_text_listing_title);
        mNotesEditText = (EditText)view.findViewById(R.id.listing_activity_edit_text_notes);
    }

    void setupConcatButton(View view) {
        ImageButton imageButton = (ImageButton)view.findViewById(R.id.listing_activity_image_button_concat);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleEditText.getText().toString();
                if (title.length() == 0) {
                    Main2Activity activity = (Main2Activity)getActivity();
                   activity.showToast("Please add a record title to use to this feature");
                } else {
                    mListingTitleEditText.setText(createListingTitle());
                }
            }
        });
    }

    String createListingTitle() {
        String aritst = mArtistEditText.getText().toString();
        String title = mTitleEditText.getText().toString();
        String label = mLabelEditText.getText().toString();
        String styleCat = mStyleCatSpinner.getSelectedItem().toString();
        String ebayCategory = mStyleCatSpinner.getSelectedItem().toString();
        // remove illegal chars for eBay
        return styleCat;
    }


}
