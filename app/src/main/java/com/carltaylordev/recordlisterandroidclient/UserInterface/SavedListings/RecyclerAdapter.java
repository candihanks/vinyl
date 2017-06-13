package com.carltaylordev.recordlisterandroidclient.UserInterface.SavedListings;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by carl on 07/06/2017.
 */

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.RecordHolder> {

    /**
     * Holder
     */

    public static class RecordHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public interface Interface {
            void editRecord(String uuid);
        }

        private ImageView mImageView;
        private TextView mTitleTextView;
        private TextView mPriceTextView;
        private ImageView mUploadedImageView;
        private CheckBox mCheckBox;
        private RealmRecord mRecord;
        private Interface mInterface;

        public RecordHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.record_image);
            mTitleTextView = (TextView) view.findViewById(R.id.record_listing_title);
            mPriceTextView = (TextView) view.findViewById(R.id.record_price);
            mUploadedImageView = (ImageView) view.findViewById(R.id.needs_upload_image_view);
            mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecord.setChecked(mCheckBox.isChecked());
                }
            });
            view.setOnClickListener(this);
        }

        public void bindRecord(RealmRecord record, Interface adapterInterface) {
            mRecord = record;
            mInterface = adapterInterface;

            RealmImage image = mRecord.getImages().get(0);
            if (image.getThumb() == null) {
                image.rehydrate();
            }

            if (mRecord.hasBeenUploaded()) {
                 mUploadedImageView.setVisibility(View.INVISIBLE);
            } else {
                mUploadedImageView.setVisibility(View.VISIBLE);
            }

            mImageView.setImageBitmap(image.getThumb());
            mTitleTextView.setText(mRecord.getListingTitle());
            mPriceTextView.setText(mRecord.getPrice());
            mCheckBox.setChecked(false);
        }

        @Override
        public void onClick(View v) {
            mInterface.editRecord(mRecord.getUuid());
        }
    }

    /**
     * Adapter
     */

    private List<RealmRecord> mRecords;
    private RecordHolder.Interface mInterface;

    public RecyclerAdapter(List<RealmRecord> records, Activity activity) {
        mRecords = records;
        mInterface = (RecordHolder.Interface) activity;
    }

    public void updateRecords(List<RealmRecord>records) {
        mRecords = records;
        this.notifyDataSetChanged();
    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_listing_item_row_layout, parent, false);
        return new RecordHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        RealmRecord record = mRecords.get(position);
        holder.bindRecord(record, mInterface);
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public List<RealmRecord> getSelectedItems() {
        List<RealmRecord> items = new ArrayList<>();
        for (RealmRecord record : mRecords) {
            if (record.isChecked()) {
                items.add(record);
            }
        }
        return items;
    }
}
