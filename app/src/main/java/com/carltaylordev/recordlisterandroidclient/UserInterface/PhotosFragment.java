package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.RecordSessionManager;
import com.carltaylordev.recordlisterandroidclient.models.ImageItem;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import java.util.ArrayList;

/**
 * Created by carl on 30/05/2017.
 */

public class PhotosFragment extends android.support.v4.app.Fragment implements RecordSessionManager.Interface {

    private GridView mGridView;
    private GridViewAdapter mGridAdapter;

    /**
     * Constructors
     */

    public PhotosFragment() {
    }

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }

    /**
     *  Fragment LifeCycle
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photos_fragment, container, false);

        ListingActivity activity = (ListingActivity)getActivity();

        setupGridView(rootView, activity);

        return rootView;
    }

    /**
     *  Setup
     */

    void setupGridView(View view, ListingActivity activity) {
        ArrayList<ImageItem> images = new ArrayList<>();
        images.add(placeHolderImage());
        images.add(placeHolderImage());
        images.add(placeHolderImage());
        images.add(placeHolderImage());

        mGridView = (GridView) view.findViewById(R.id.photo_grid_view);
        mGridAdapter = new GridViewAdapter(activity, R.layout.photo_item_layout, images);
        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Logger.logMessage("CLICKED");

                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                // take picture


            }
        });
    }

    private ImageItem placeHolderImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.magic_wand);
        return new ImageItem(bitmap, "Tap To Add");
    }

    /**
     *  RecordSessionManager Interface
     */

    @Override
    public void updateRecord(RealmRecord realmRecord) {

    }

    @Override
    public void updateUI(RealmRecord realmRecord) {

    }
}


