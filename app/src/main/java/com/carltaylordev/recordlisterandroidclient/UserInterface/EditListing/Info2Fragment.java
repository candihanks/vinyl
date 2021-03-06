package com.carltaylordev.recordlisterandroidclient.UserInterface.EditListing;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import java.util.ArrayList;

public class Info2Fragment extends android.support.v4.app.Fragment implements RecordSessionManager.UpdateSessionInterface, RecordSessionManager.UpdateUiInterface {

    Spinner mRecordConditionSpinner;
    Spinner mCoverConditionSpinner;
    Spinner mNumberOfRecordsSpinner;
    EditText mCommentsEditText;
    EditText mPriceEditText;

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

        EditListingActivity activity = (EditListingActivity)getActivity();

        setupEditTexts(rootView);
        setupSpinners(rootView, activity);

        return rootView;
    }

    /**
     *  Setup
     */

    private void setupEditTexts(View view) {
        mCommentsEditText = (EditText)view.findViewById(R.id.edit_text_comments);
        mPriceEditText = (EditText)view.findViewById(R.id.edit_text_price);
        mPriceEditText.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 5, afterDecimal = 2;
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        if (mPriceEditText.getText().toString().equals(source.toString())) {
                            // the value has not changed
                            return super.filter(source, start, end, dest, dstart, dend);
                        }

                        String combinedOldAndNewValue = mPriceEditText.getText() + source.toString();
                        if (combinedOldAndNewValue.equals(".")) {
                            // create the 0 automatically for the user
                            return "0.";
                        }
                        else if (combinedOldAndNewValue.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (combinedOldAndNewValue.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            combinedOldAndNewValue = combinedOldAndNewValue.substring(combinedOldAndNewValue.indexOf(".") + 1);
                            if (combinedOldAndNewValue.length() > afterDecimal) {
                                // we are past 2 decimal places so return nothing
                                return "";
                            }
                        }
                        // Passes our checks so let it through
                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });

    }

    void setupSpinners(View view, EditListingActivity activity) {
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

        ArrayList<String> numbers = new ArrayList();
        for (int i = 1; i < 11; i ++) {
            numbers.add(Integer.toString(i));
        }
        ArrayAdapter<String> numberOfRecordsAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, numbers);
        mNumberOfRecordsSpinner = (Spinner)view.findViewById(R.id.spinner_number_of_records);
        mNumberOfRecordsSpinner.setAdapter(numberOfRecordsAdapter);
    }

    /**
     *  RecordSessionManager Interfaces
     */

    @Override
    public void updateSession(RecordSessionManager manager) {
        RealmRecord record = manager.getRecord();
        record.setComments(mCommentsEditText.getText().toString());
        record.setMediaCondition(mRecordConditionSpinner.getSelectedItem().toString());
        record.setCoverCondition(mCoverConditionSpinner.getSelectedItem().toString());
        record.setPrice(mPriceEditText.getText().toString());
        record.setNumberOfRecords(mNumberOfRecordsSpinner.getSelectedItem().toString());
    }

    @Override
    public void updateUI(RecordSessionManager manager) {
        RealmRecord record = manager.getRecord();
        mCommentsEditText.setText(record.getComments());
        mPriceEditText.setText(record.getPrice());

        ArrayAdapter recordConditionAdapter = (ArrayAdapter)mRecordConditionSpinner.getAdapter();
        mRecordConditionSpinner.setSelection(recordConditionAdapter.getPosition(record.getMediaCondition()));

        ArrayAdapter coverConditionAdapter = (ArrayAdapter)mCoverConditionSpinner.getAdapter();
        mCoverConditionSpinner.setSelection(coverConditionAdapter.getPosition(record.getCoverCondition()));

        ArrayAdapter numberOfRecordsAdapter = (ArrayAdapter)mNumberOfRecordsSpinner.getAdapter();
        mNumberOfRecordsSpinner.setSelection(numberOfRecordsAdapter.getPosition(record.getNumberOfRecords()));
    }
}
