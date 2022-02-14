package com.hairstyle.hairstyleapp;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.io.File;
import java.util.ArrayList;

import Helper.AudienceNetworkInitializeHelper;
import Helper.SQLiteHandler;
import adapter.Image;
import adapter.MyToast;
import com.facebook.ads.*;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

public class SaveImage extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    private int selectedPosition = 0;
    DownloadManager downloadManager;

    private ArrayList<Image> images;
    private ViewPager viewPager;

    //String SubEndPoint;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;


    Button saveButton;
    Button removeButton;
    TextView ratetext;
    private com.facebook.ads.AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_image);
        setTitle("Save and Share");

        ScrollView scrollView = findViewById(R.id.sclV);
        scrollView.setVerticalScrollBarEnabled(false);
//Permmision Status
        permissionStatus = SaveImage.this.getSharedPreferences("permissionStaus", SaveImage.MODE_PRIVATE);

        //INTENT RECIEVER

        Intent i = getIntent();
        // getting attached intent data
        final String imgUrl = i.getStringExtra("imgUrl");
        String imgName = i.getStringExtra("imgName");

        ImageView imageViewPreview = findViewById(R.id.Simg);
        Glide.with(SaveImage.this).load(imgUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_loader)
                .into(imageViewPreview);

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
       // AdSettings.addTestDevice("cb10b985-eb21-4d8e-82b3-569763a772ba");

// Request an ad
        adView.loadAd();
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
// Ad error callback
                Toast.makeText(
                        SaveImage.this,
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

// Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());


        //Share Icons Code
        saveButton =  findViewById(R.id.saveBtn);
        removeButton = findViewById(R.id.removeBtn);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo!= null && networkInfo.isConnectedOrConnecting()) {
                    save();
                }else {
                    MyToast.showToast(SaveImage.this, "No active internet connection");
                }

            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();

            }
        });

    }



    private void save() {

        if (ActivityCompat.checkSelfPermission(SaveImage.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SaveImage.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveImage.this);
                builder.setTitle("Allow Storage Permission");
                builder.setMessage("To save any image you need to allow storage permission");
                builder.setCancelable(false);
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SaveImage.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveImage.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", SaveImage.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(SaveImage.this, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(SaveImage.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }



    private long proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        // Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
        Long reference;
        if (!Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.DIRECTORY_DOWNLOADS);
            file.mkdirs();
        } else {
            File dir = new File(Environment.DIRECTORY_DOWNLOADS);
            dir.mkdirs();

        }

        //INTENT RECIEVER

        Intent i = getIntent();
        // getting attached intent data
        String imgUrl = i.getStringExtra("imgUrl");
        String imgName ="Hairstyles from hairminion";

        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(imgUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
       // Toast.makeText(SaveImage.this,R.string.saving_image,Toast.LENGTH_SHORT).show();
        request.setDestinationInExternalPublicDir("/"+imgName,imgName+".jpeg");
        request.setMimeType("image/jpeg");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE).setAllowedOverRoaming(
                false).setTitle(imgName).setDescription("saving to gallery....");
        //request.setDestinationInExternalPublicDir("Ydsapp/","africanfashionhive.png");
        reference = downloadManager.enqueue(request);
        // downloadManager.enqueue(request);
           MyToast.showToast(getApplicationContext(),"Hairstyle saved!");


        return reference;
    }

    private void remove() {
        //INTENT RECIEVER

        Intent i = getIntent();
        // getting attached intent data
        String imgUrl = i.getStringExtra("imgUrl");
        String imgText = i.getStringExtra("imgText");

                    i = new Intent(SaveImage.this, Remove_Design.class);
                    i.putExtra("img_path", imgUrl);
                    i.putExtra("user_album_name", imgText);

                    startActivity(i);
 }



    public void rateApp() {
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

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

}


