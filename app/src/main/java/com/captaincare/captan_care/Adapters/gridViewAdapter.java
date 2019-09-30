package com.captaincare.captan_care.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.captaincare.captan_care.R;

import java.util.ArrayList;

public class gridViewAdapter extends BaseAdapter {

    private Context ctx;
    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    ArrayList<Bitmap> mArrayUri;
    public gridViewAdapter(Context ctx, ArrayList<Bitmap> mArrayUri) {

        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
    }

    @Override
    public int getCount() {
        return mArrayUri.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        pos = position;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.custom_gridview_item, null);

        ivGallery = (ImageView) itemView.findViewById(R.id.ivGallery);

        ivGallery.setImageBitmap(mArrayUri.get(position));

        return itemView;
    }


}