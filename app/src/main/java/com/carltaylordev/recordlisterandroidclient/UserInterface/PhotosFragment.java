package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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

public class PhotosFragment extends android.support.v4.app.Fragment implements RecordSessionManager.UpdateUiInterface {

    static final int REQUEST_IMAGE_CAPTURE = 1001;

    private GridView mGridView;
    private PhotosGridViewAdapter mGridAdapter;

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

    void setupGridView(View view, final ListingActivity activity) {
        ArrayList<RealmImage> images = activity.mRecordSessionManager.getImages();
        mGridAdapter = new PhotosGridViewAdapter(getActivity(), R.layout.photo_item_layout, images);
        mGridView = (GridView) view.findViewById(R.id.photo_grid_view);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int gridPosition, long id) {
                captureImage(gridPosition);
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int gridPosition, long id) {
                RealmImage image = mGridAdapter.getItem(gridPosition);
                if (!image.isPlaceHolder()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Are you sure?")
                            .setTitle("Delete Image")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeImageAtIndex(gridPosition);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });
    }

    /**
     *  Image Capture / Deletion
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

    private void removeImageAtIndex(int index) {
        ListingActivity activity = (ListingActivity) getActivity();
        RecordSessionManager manager = activity.mRecordSessionManager;
        manager.removeImageAtIndex(index);
    }

    /**
     *  RecordSessionManager Interface
     */

    @Override
    public void updateUI(RecordSessionManager manager) {
        ArrayList<RealmImage> copy = manager.getImages();
        mGridAdapter.clear();
        mGridAdapter.addAll(copy);
        mGridAdapter.notifyDataSetChanged();
    }
}
