package com.hairstyle.hairstyleapp;

/**
 * Created by PASCHAL GREEN on 9/30/2016.
 */
import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.hairstyle.hairstyleapp.util.AppOpenManager;
import com.hairstyle.hairstyleapp.util.LruBitmapCache;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private ImageLoader imageLoader;

    private static AppController mInstance;
    AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mInstance = this;
        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {}
                });
        appOpenManager = new AppOpenManager(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}