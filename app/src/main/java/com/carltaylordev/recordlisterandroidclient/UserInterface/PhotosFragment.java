package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.RecordSessionManager;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by carl on 30/05/2017.
 */

public class PhotosFragment extends android.support.v4.app.Fragment implements RecordSessionManager.UpdateInterface {

    static final int REQUEST_IMAGE_CAPTURE = 1001;

    private GridView mGridView;
    private GridViewAdapter mGridAdapter;

    private int mLastSelectedGridPosition;
    private String mLastCreatedTempFileLocation;

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
        ArrayList<RealmImage> images = activity.mRecordSessionManager.getImages();
        mGridView = (GridView) view.findViewById(R.id.photo_grid_view);
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.photo_item_layout, images);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int gridPosition, long id) {
                captureImage(gridPosition);
            }
        });
    }

    /**
     *  Image Capture
     */

    private void captureImage(int gridPosition) {
        ListingActivity activity = (ListingActivity) getActivity();
        mLastSelectedGridPosition = gridPosition;

        FileManager fileManager = new FileManager(activity);
        File tempFile = null;
        try {
            tempFile = fileManager.createTempFileOnDisc(".jpg");
        } catch (IOException e) {
            Logger.logMessage("Error creating file: " + e.toString());
        }

        if (tempFile != null) {
            mLastCreatedTempFileLocation = tempFile.getAbsolutePath();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } else {
                Logger.logMessage("Devise can not take pictures");
                activity.showToast("Your Devise Can Not Take Pictures");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            RealmImage image = new RealmImage("New Image", mLastCreatedTempFileLocation);

            ListingActivity activity = (ListingActivity) getActivity();
            RecordSessionManager manager = activity.mRecordSessionManager;
            manager.setImageAtIndex(image, mLastSelectedGridPosition);
        }
    }

    /**
     *  RecordSessionManager Interface
     */

    @Override
    public void updateSession(RecordSessionManager manager) {
//        manager.setImages(mGridAdapter.getItems());
    }

    @Override
    public void updateUI(RecordSessionManager manager) {
        ArrayList<RealmImage> copy = manager.getImages();
        mGridAdapter.clear();
        mGridAdapter.addAll(copy);
        mGridAdapter.notifyDataSetChanged();
    }
}


