package com.captaincare.captan_care.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.captaincare.captan_care.R;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    private ArrayList<String> imgArrayList;
    private Context context;

    public ImageAdapter(Context context) {
        this.context = context;
        imgArrayList = new ArrayList<>();
    }

    public void addImage(String imgUri) {
        imgArrayList.add(imgUri);
    }

    @Override
    public int getCount() {
        return imgArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_image, container, false);
        ImageView imageView = view.findViewById(R.id.showImage);
        Glide.with(context).load(imgArrayList.get(position)).into(imageView);
        container.addView(imageView, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
