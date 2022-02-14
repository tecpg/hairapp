package com.hairstyle.hairstyleapp.util;


import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.appopen.AppOpenAd;

import static androidx.lifecycle.Lifecycle.Event.ON_START;


public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String LOG_TAG = "AppOpenManager";
    private static final String AD_UNIT_ID = "ca-app-pub-1519698802326066/9270112755";
    private AppOpenAd appOpenAd = null;
    public static final String PREF_FULL_ACCESS= "FULL ACCESS";

    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private Activity currentActivity;
    private static boolean isShowingAd = false;
    private final Application myApplication;


    /**
     * Constructor
     */
    public AppOpenManager(Application myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

    }

    /**
     * Request an ad
     */

    public void fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAppOpenAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param error the error.
                     */
                    @Override
                    public void onAppOpenAdFailedToLoad(LoadAdError error) {
                        // Handle the error.
                        Log.d(LOG_TAG,"error in loading");
                        // Gets the domain from which the error came.
                        String errorDomain = error.getDomain();
                        // Gets the error code. See
                        // https://developers.google.com/android/reference/com/google/android/gms/ads/AdRequest#constant-summary
                        // for a list of possible codes.
                        int errorCode = error.getCode();
                        // Gets an error message.
                        // For example "Account not approved yet". See
                        // https://support.google.com/admob/answer/9905175 for explanations of
                        // common errors.
                        String errorMessage = error.getMessage();
                        // Gets additional response information about the request. See
                        // https://developers.google.com/admob/android/response-info for more
                        // information.
                        ResponseInfo responseInfo = error.getResponseInfo();
                        // Gets the cause of the error, if available.
                        AdError cause = error.getCause();
                        // All of this information is available via the error's toString() method.
                        Log.d("Ads", error.toString());
                    }

                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    /**
     * Shows the ad if one isn't already showing.
     */
    public void showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            appOpenAd.show(currentActivity, fullScreenContentCallback);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd();
        }
    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("D02FCD5BE190BCAA7310AC47A63FA0D5").build();
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable() {
        return appOpenAd != null;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;

    }
    /** LifecycleObserver methods */
    @OnLifecycleEvent(ON_START)
    public void onStart() {
//        if (getAdsStatus()) {
//            showAdIfAvailable();
//        }
        if (!getFullAccess())showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");
    }

    public boolean getFullAccess(){
        boolean fullAccess;
        SharedPreferences pref = currentActivity.getSharedPreferences(PREF_FULL_ACCESS, 0);
        fullAccess = pref.getBoolean("fullAccess", false);

        return fullAccess;

    }
}

