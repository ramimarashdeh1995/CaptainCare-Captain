package com.captaincare.captan_care.ServerClass;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestHand {
    @SuppressLint("StaticFieldLeak")
    private static RequestHand mInstance;
    private RequestQueue mRequestQueue;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;

    private RequestHand(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized RequestHand getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestHand(context);
        }
        return mInstance;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(
                0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }
}
