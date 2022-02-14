package com.hairstyle.hairstyleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;


import Helper.AudienceNetworkInitializeHelper;

import com.facebook.ads.*;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

public class FirstScreen extends AppCompatActivity {

    private com.facebook.ads.AdView adView;
    private final String TAG = FirstScreen.class.getSimpleName();
    private InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);


        // If you call AudienceNetworkAds.buildInitSettings(Context).initialize()
        // in Application.onCreate() this call is not really necessary.
        // Otherwise call initialize() onCreate() of all Activities that contain ads or
        // from onCreate() of your Splash Activity.
        AudienceNetworkInitializeHelper.initialize(this);
        //---------------------------Ad Banner Code----------------------------------------------


        //   com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this, "my ID", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        adView = new AdView(this, "155871289992615_238327571746986", AdSize.RECTANGLE_HEIGHT_250);

// Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

// Add the ad view to your activity layout
        adContainer.addView(adView);
       AdSettings.addTestDevice("cb10b985-eb21-4d8e-82b3-569763a772ba");

// Request an ad
        adView.loadAd();
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
// Ad error callback
                Toast.makeText(
                        FirstScreen.this,
                        "Error: " + adError.getErrorMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
// Ad loaded callback
            }

            @Override
            public void onAdClicked(Ad ad) {
// Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
// Ad impression logged callback
            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
        interstitialAd = new InterstitialAd(this, "155871289992615_156184313294646");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
//                Intent i = new Intent(FirstScreen.this, SplashActivity.class);
//                startActivity(i);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());


        final Handler handler = new Handler();
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                // Check if interstitialAd has been loaded successfully
                if (interstitialAd == null
                        || !interstitialAd.isAdLoaded()
                        || interstitialAd.isAdInvalidated()) {
                    // Ad not ready to show.
                    Intent i = new Intent(FirstScreen.this, SplashActivity.class);
                    startActivity(i);
                } else {
                    interstitialAd.show();
                }
            }
        };
        handler.postDelayed(run, 5000);


    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}