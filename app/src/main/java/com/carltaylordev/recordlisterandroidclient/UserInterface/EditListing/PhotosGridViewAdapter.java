package com.carltaylordev.recordlisterandroidclient.UserInterface.EditListing;

/**
 * Created by carl on 31/05/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.models.RealmImage;

import java.util.ArrayList;

import io.realm.RealmList;

public class PhotosGridViewAdapter extends ArrayAdapter<RealmImage> {

    static class ViewHolder {
        ImageView image;
    }

    private Context context;
    private int layoutResourceId;
    private ArrayList<RealmImage> data;

    public PhotosGridViewAdapter(Context context, int layoutResourceId, ArrayList<RealmImage> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        RealmImage item = data.get(position);
        holder.image.setImageBitmap(item.getThumb());
        if (item.isPlaceHolder()) {
            holder.image.setBackgroundColor(ContextCompat.getColor(context, R.color.background_primary));
        }
        return row;
    }
}