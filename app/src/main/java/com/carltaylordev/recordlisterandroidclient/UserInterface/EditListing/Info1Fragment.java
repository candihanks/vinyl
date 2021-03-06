package com.carltaylordev.recordlisterandroidclient.UserInterface.EditListing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;
import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

/**
 * Created by carl on 29/05/2017.
 */

public class Info1Fragment extends android.support.v4.app.Fragment implements RecordSessionManager.UpdateSessionInterface,
        RecordSessionManager.UpdateUiInterface {

    Spinner mStyleCatSpinner;

    EditText mArtistEditText;
    EditText mTitleEditText;
    EditText mLabelEditText;
    EditText mListingTitleEditText;

    /**
     * Constructors
     */

    public Info1Fragment() {
    }

    public static Info1Fragment newInstance() {
        return new Info1Fragment();
    }

    /**
     *  Fragment LifeCycle
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info_1_fragment, container, false);

        EditListingActivity activity = (EditListingActivity)getActivity();

        setupEditTexts(rootView);
        setupSpinner(rootView, activity);
        setupConcatButton(rootView);

        return rootView;
    }

    /**
     *  Setup
     */

    void setupSpinner(View view, EditListingActivity activity) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, activity.mRecordSessionManager.getCategoriesAsStrings());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mStyleCatSpinner = (Spinner)view.findViewById(R.id.spinner_ebay_cats);
        mStyleCatSpinner.setAdapter(spinnerArrayAdapter);
    }

    void setupEditTexts(View view) {
        mArtistEditText = (EditText)view.findViewById(R.id.edit_text_artist);
        mTitleEditText = (EditText)view.findViewById(R.id.edit_text_title);
        mLabelEditText = (EditText)view.findViewById(R.id.edit_text_label);
        mListingTitleEditText = (EditText)view.findViewById(R.id.edit_text_listing_title);
    }

    void setupConcatButton(View view) {
        ImageButton imageButton = (ImageButton)view.findViewById(R.id.image_button_concat);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditListingActivity activity = (EditListingActivity)getActivity();
                BoolResponse response = activity.mRecordSessionManager.canBuildListingTitle();
                if (response.isTrue()) {
                    mListingTitleEditText.setText(activity.mRecordSessionManager.buildListingTitle());
                } else {
                    activity.showToast(response.getUserMessage());
                }
            }
        });
    }

    /**
     *  RecordSessionManager Interfaces
     */

    @Override
    public void updateSession(RecordSessionManager manager) {
        RealmRecord record = manager.getRecord();
        record.setArtist(mArtistEditText.getText().toString());
        record.setTitle(mTitleEditText.getText().toString());
        record.setLabel(mLabelEditText.getText().toString());
        record.setListingTitle(mListingTitleEditText.getText().toString());
        manager.setEbayCatagoryMatchingName(mStyleCatSpinner.getSelectedItem().toString());
    }

    @Override
    public void updateUI(RecordSessionManager manager) {
        RealmRecord record = manager.getRecord();
        mArtistEditText.setText(record.getArtist());
        mTitleEditText.setText(record.getTitle());
        mLabelEditText.setText(record.getLabel());
        mListingTitleEditText.setText(record.getListingTitle());

        EbayCategory ebayCategory = record.getEbayCategory();
        ArrayAdapter styleCatAdapter = (ArrayAdapter)mStyleCatSpinner.getAdapter();
        mStyleCatSpinner.setSelection(styleCatAdapter.getPosition(ebayCategory.getName()));
    }
}
