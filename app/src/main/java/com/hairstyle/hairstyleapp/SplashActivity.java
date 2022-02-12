package com.hairstyle.hairstyleapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Locale;

import Helper.AudienceNetworkInitializeHelper;
import com.facebook.ads.*;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;


public class SplashActivity extends AppCompatActivity {
    InterstitialAd interstitialAd,interstitialAd2, interstitialAd3, interstitialAd4;
    private AdView madView;

    private static final String TAG = MainActivity.class.getSimpleName();
    private RelativeLayout lb1,lb2,lb3,lb4;
    private ScrollView mScroll;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mScroll = findViewById(R.id.scroll);
        mScroll.setVerticalScrollBarEnabled(false);

        // If you call AudienceNetworkAds.buildInitSettings(Context).initialize()
        // in Application.onCreate() this call is not really necessary.
        // Otherwise call initialize() onCreate() of all Activities that contain ads or
        // from onCreate() of your Splash Activity.
        AudienceNetworkInitializeHelper.initialize(this);

        //Button Font for Version 23 and above
        TextView title = findViewById(R.id.appNametext);
        TextView sub = findViewById(R.id.sub);
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "Oxanium-Bold.ttf"));
        Typeface typeface2 = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "Oxanium-Light.ttf"));
        title.setTypeface(typeface);
        sub.setTypeface(typeface2);




     //   com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this, "my ID", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
      adView = new AdView(this, "155871289992615_237328858513524", AdSize.BANNER_HEIGHT_50);

// Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

// Add the ad view to your activity layout
        adContainer.addView(adView);

// Request an ad
        adView.loadAd();

        interstitialAd = new com.facebook.ads.InterstitialAd(this, "155871289992615_237329165180160");

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
                goToNext("hair_image_query");
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

        interstitialAd2 = new com.facebook.ads.InterstitialAd(this, "155871289992615_237329165180160");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener2 = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                goToNext("braids_image_query");
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
        interstitialAd2.loadAd(
                interstitialAd2.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener2)
                        .build());

        interstitialAd3 = new com.facebook.ads.InterstitialAd(this, "155871289992615_237329165180160");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener3 = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                goToNext("natural_image_query");
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
        interstitialAd3.loadAd(
                interstitialAd3.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener3)
                        .build());
        interstitialAd4 = new com.facebook.ads.InterstitialAd(this, "155871289992615_237329165180160");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener4 = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                goToNext("color_image_query");
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
        interstitialAd4.loadAd(
                interstitialAd4.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener4)
                        .build());

        lb1 = findViewById(R.id.Lbg1);
        lb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd == null
                        || !interstitialAd.isAdLoaded()
                        || interstitialAd.isAdInvalidated()) {
                    goToNext("hair_image_query");
                }else {
                    interstitialAd.show();
                }
            }
        });
        lb2 = findViewById(R.id.Lbg2);
        lb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd2 == null
                        || !interstitialAd2.isAdLoaded()
                        || interstitialAd2.isAdInvalidated()) {
                    goToNext("braids_image_query");
                }else {
                    interstitialAd2.show();
                }
            }
        });
        lb3 = findViewById(R.id.Lbg3);
        lb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd3 == null
                        || !interstitialAd3.isAdLoaded()
                        || interstitialAd3.isAdInvalidated()) {
                    goToNext("natural_image_query");
                }else {
                    interstitialAd3.show();
                }
            }
        });
        lb4 = findViewById(R.id.Lbg4);
        lb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd4 == null
                        || !interstitialAd4.isAdLoaded()
                        || interstitialAd4.isAdInvalidated()) {
                    goToNext("color_image_query");
                }else {
                    interstitialAd4.show();
                }
            }
        });
        Button upBtn = findViewById(R.id.upBtn);
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, Upload_Design.class);
                startActivity(i);

            }
        });
        TextView insta = findViewById(R.id.insta);
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoeryLink = "https://www.instagram.com/hairminion";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(categoeryLink));
                startActivity(i);

            }
        });
        TextView fbk = findViewById(R.id.fbk);
        fbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoeryLink = "https://www.facebook.com/All-Women-Hairstyles-112875083815774";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(categoeryLink));
                startActivity(i);

            }
        });
        TextView rate = findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            rateApp();
            }
        });
        TextView pp = findViewById(R.id.pp);
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoeryLink = "https://hairminion.com/privacy-policy-2";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(categoeryLink));
                startActivity(i);

            }
        });

    }



        @Override
        public void onBackPressed() {
            super.onBackPressed();

    }


    public void mini(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }




    public void rateApp(){
        Uri uri = Uri.parse("market://details?id=" + getBaseContext().getPackageName());
        Intent rt = new Intent(Intent.ACTION_VIEW, uri);
        //application
        rt.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            startActivity(rt);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }
    public void goToNext(final String query){

        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("query",query);
        i.putExtras(bundle);
        startActivity(i);

    }



    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}

