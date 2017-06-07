package com.carltaylordev.recordlisterandroidclient.UserInterface.SavedListings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import io.realm.RealmResults;

/**
 * Created by carl on 07/06/2017.
 */

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.RecordHolder> {

    /**
     * Holder
     */

    public static class RecordHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;
        private TextView mTitleTextView;
        private RealmRecord mRecord;

        public RecordHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.record_image);
            mTitleTextView = (TextView) view.findViewById(R.id.record_listing_title);
            view.setOnClickListener(this);
        }

        public void bindRecord(RealmRecord record) {
            mRecord = record;

            RealmImage image = mRecord.getImages().get(0);
            if (image.getThumb() == null) {
                image.rehydrate();
            }

            mImageView.setImageBitmap(image.getThumb());
            mTitleTextView.setText(mRecord.getListingTitle());
        }

        @Override
        public void onClick(View v) {
            Logger.logMessage("RecyclerViewClicked");
            // // TODO: 07/06/2017 launch edit listing activity with UUID
        }
    }

    /**
     * Adapter
     */

    private RealmResults<RealmRecord> mRecords;

    public RecyclerAdapter(RealmResults<RealmRecord> records) {
        mRecords = records;
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
        holder.bindRecord(record);
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }
}