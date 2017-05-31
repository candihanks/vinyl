package com.carltaylordev.recordlisterandroidclient.UserInterface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.carltaylordev.recordlisterandroidclient.models.ImageItem;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;
import com.carltaylordev.recordlisterandroidclient.models.RealmRecord;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by carl on 30/05/2017.
 */

public class PhotosFragment extends android.support.v4.app.Fragment implements RecordSessionManager.Interface {

    static final int REQUEST_IMAGE_CAPTURE = 1001;

    private GridView mGridView;
    private GridViewAdapter mGridAdapter;

    private int mLastSelectedGridPosition;

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

        setupGridView(rootView);

        return rootView;
    }

    /**
     *  Setup
     */

    void setupGridView(View view) {
        ArrayList<ImageItem> images = new ArrayList<>();
        images.add(placeHolderImage());

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
     *  Image Capture / Writing
     */

    private void captureImage(int gridPosition) {
        mLastSelectedGridPosition = gridPosition; // // TODO: 31/05/2017 cant seem to pass this with the intent!

        ListingActivity activity = (ListingActivity) getActivity();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, ""); // TODO: 31/05/2017 DO I need to pass a url to get full image size?
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } else {
            Logger.logMessage("Devise can not take pictures");
            activity.showToast("Your Devise Can Not Take Pictures");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = intent.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            addImageToAdapter(imageBitmap, mLastSelectedGridPosition);
        }
    }

    private void writeImage(Bitmap imageBitmap) {
        FileManager fileManager = new FileManager(getActivity());
        try {
            File file = fileManager.writeImageFile(imageBitmap);
        } catch (Exception e) {
            Logger.logMessage("Exception writing Image File: " + e.toString());
        }
    }

    private ImageItem placeHolderImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.magic_wand);
        return new ImageItem(bitmap, "Tap To Add", true);
    }

    private void addImageToAdapter(Bitmap imageBitmap, int gridPosition) {
        ArrayList<ImageItem> images = mGridAdapter.getItems();
        ImageItem selectedImage = images.get(gridPosition);
        if (selectedImage.isPlaceHolder()) {
            images.add(placeHolderImage());
        }
        images.remove(gridPosition);
        images.add(gridPosition, new ImageItem(imageBitmap, "New Image", false));
        mGridView.invalidateViews();
    }

    /**
     *  Image Reading / Decoding
     */

    /**
     *  RecordSessionManager Interface
     */

    @Override
    public void updateRecord(RealmRecord realmRecord) {
//        realmRecord.getImages().removeAll();
//        RealmImage realmImage = new RealmImage();
//        realmImage.setTitle("barry");
//        realmImage.setPath(file.getAbsolutePath());
//        mRealmRecord.getImages().add(realmImage);
        //remove all, write all
    }

    @Override
    public void updateUI(RealmRecord realmRecord) {

    }
}


