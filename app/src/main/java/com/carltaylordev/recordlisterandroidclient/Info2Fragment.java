package com.carltaylordev.recordlisterandroidclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;
import com.carltaylordev.recordlisterandroidclient.models.Record;

import org.w3c.dom.Text;

public class Info2Fragment extends android.support.v4.app.Fragment implements RecordSessionManager.Interface {

    Spinner mRecordConditionSpinner;
    Spinner mCoverConditionSpinner;
    EditText mCommentsEditText;

    /**
     *  Constructors
     */

    public Info2Fragment() {
    }

    public static Info2Fragment newInstance() {
        return new Info2Fragment();
    }

    /**
     *  Fragment Lifecycle
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info_2_fragment, container, false);

        ListingActivity activity = (ListingActivity)getActivity();

        setupEditTexts(rootView);
        setupSpinners(rootView, activity);

        return rootView;
    }

    /**
     *  Setup
     */

    private void setupEditTexts(View view) {
        mCommentsEditText = (EditText)view.findViewById(R.id.edit_text_comments);
    }

    void setupSpinners(View view, ListingActivity activity) {
        ArrayAdapter<String> recordConditionAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, activity.mRecordSessionManager.getRecordConditions());
        recordConditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mRecordConditionSpinner = (Spinner)view.findViewById(R.id.spinner_record_condition);
        mRecordConditionSpinner.setAdapter(recordConditionAdapter);

        ArrayAdapter<String> coverConditionAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, activity.mRecordSessionManager.getCoverConditions());
        recordConditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mCoverConditionSpinner = (Spinner)view.findViewById(R.id.spinner_cover_condition);
        mCoverConditionSpinner.setAdapter(coverConditionAdapter);
    }

    /**
     *  ListingCoordinator Interface
     */

    @Override
    public void updateRecord(Record record) {
        record.setComments(mCommentsEditText.getText().toString());
        record.setMediaCondition(mRecordConditionSpinner.toString());
        record.setCoverCondition(mCoverConditionSpinner.toString());
    }

    @Override
    public void updateUI(Record record) {
        mCommentsEditText.setText(record.getComments());

        ArrayAdapter recordConditionAdapter = (ArrayAdapter)mRecordConditionSpinner.getAdapter();
        ArrayAdapter coverConditionAdapter = (ArrayAdapter)mCoverConditionSpinner.getAdapter();

        mRecordConditionSpinner.setSelection(recordConditionAdapter.getPosition(record.getMediaCondition()));
        mCoverConditionSpinner.setSelection(coverConditionAdapter.getPosition(record.getCoverCondition()));
    }
}
