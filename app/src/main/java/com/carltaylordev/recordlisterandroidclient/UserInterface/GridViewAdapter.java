package com.carltaylordev.recordlisterandroidclient.UserInterface;

/**
 * Created by carl on 31/05/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.models.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter<ImageItem> {

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }

    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<>();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public ArrayList<ImageItem> getItems() {
        return data;
    }

    public void setItems(ArrayList imageItems) {
        data = imageItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        ImageItem item = data.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageBitmap(item.getImage());
        return row;
    }

}