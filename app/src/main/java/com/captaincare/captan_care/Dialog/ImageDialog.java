package com.captaincare.captan_care.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.Window;

import com.captaincare.captan_care.Adapters.ImageAdapter;
import com.captaincare.captan_care.R;

public class ImageDialog extends Dialog {

    private ViewPager viewPager;
    private ImageAdapter imageAdapter;
    private Context context;
    private final String offer_pic1;
    private final String offer_pic2;
    private final String offer_pic3;
    private final String offer_pic4;

    public ImageDialog(Context context, String offer_pic1, String offer_pic2, String offer_pic3, String offer_pic4) {
        super(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        this.context = context;
        this.offer_pic1 = offer_pic1;
        this.offer_pic2 = offer_pic2;
        this.offer_pic3 = offer_pic3;
        this.offer_pic4 = offer_pic4;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.dialog_images_offer);

        viewPager = findViewById(R.id.imageViewPager);
        imageAdapter = new ImageAdapter(context);
        imageAdapter.addImage(offer_pic1);
        imageAdapter.addImage(offer_pic2);
        imageAdapter.addImage(offer_pic3);
        imageAdapter.addImage(offer_pic4);
        viewPager.setAdapter(imageAdapter);

    }
}
