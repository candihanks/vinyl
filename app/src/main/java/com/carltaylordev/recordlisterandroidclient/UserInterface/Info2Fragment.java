package com.carltaylordev.recordlisterandroidclient.UserInterface;

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
import com.carltaylordev.recordlisterandroidclient.RecordSessionManager;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

public class Info2Fragment extends android.support.v4.app.Fragment implements RecordSessionManager.Interface {

    Spinner mRecordConditionSpinner;
    Spinner mCoverConditionSpinner;
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
        mPriceEditText = (EditText)view.findViewById(R.id.edit_text_price);
        mPriceEditText.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 5, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = mPriceEditText.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }
                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });

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
     *  RecordSessionManager Interface
     */

    @Override
    public void updateRecord(RealmRecord realmRecord) {
        realmRecord.setComments(mCommentsEditText.getText().toString());
        realmRecord.setMediaCondition(mRecordConditionSpinner.toString());
        realmRecord.setCoverCondition(mCoverConditionSpinner.toString());
        realmRecord.setPrice(mPriceEditText.getText().toString());
    }

    @Override
    public void updateUI(RealmRecord realmRecord) {
        mCommentsEditText.setText(realmRecord.getComments());

        ArrayAdapter recordConditionAdapter = (ArrayAdapter)mRecordConditionSpinner.getAdapter();
        ArrayAdapter coverConditionAdapter = (ArrayAdapter)mCoverConditionSpinner.getAdapter();
        mRecordConditionSpinner.setSelection(recordConditionAdapter.getPosition(realmRecord.getMediaCondition()));
        mCoverConditionSpinner.setSelection(coverConditionAdapter.getPosition(realmRecord.getCoverCondition()));

        mPriceEditText.setText(realmRecord.getPrice());
    }
}
