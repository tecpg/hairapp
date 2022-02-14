package adapter;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.hairstyle.hairstyleapp.FirstScreen;
import com.hairstyle.hairstyleapp.R;
import com.hairstyle.hairstyleapp.Remove_Design;
import com.hairstyle.hairstyleapp.SaveImage;
import com.hairstyle.hairstyleapp.SplashActivity;
import com.joaquimley.faboptions.FabOptions;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Helper.AudienceNetworkInitializeHelper;
import Helper.SQLiteHandler;

import com.facebook.ads.*;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

/**
 * Created by PASCHAL GREEN on 11/30/2016.
 */
public class SlideshowDialogFragment2 extends DialogFragment implements View.OnClickListener {
    private String TAG = SlideshowDialogFragment2.class.getSimpleName();
    Log wtf;
    private ArrayList<Image2> images2;
    private ArrayList<Image4> images4;
    private GalleryAdapter4 mAdapter4;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView  pintext;
    private ImageView picShare, editBtn, pinit, adImg;
    private int selectedPosition = 0;
    DownloadManager downloadManager;
    SQLiteHandler db;
    StaggeredGridLayoutManager gagaredGridLayoutManager;
    RecyclerView recyclerView4;
    private AdView madView;
    //String SubEndPoint;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private com.facebook.ads.AdView adView;



    private final CharSequence[] items = {"Save Design", "Remove Design"};


    private static final int PERMISSION_REQUEST_CODE = 1;
    private InterstitialAd interstitialAd;

    public static SlideshowDialogFragment2 newInstance() {
        SlideshowDialogFragment2 f = new SlideshowDialogFragment2();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
//Permmision Status
        permissionStatus = getActivity().getSharedPreferences("permissionStaus",getActivity().MODE_PRIVATE);
//Scroll view

        final View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        ScrollView sv = v.findViewById(R.id.sclV2);
        sv.setFillViewport(true);
        sv.setVerticalScrollBarEnabled(false);
        viewPager = v.findViewById(R.id.viewpager);


        // pDialog = new ProgressDialog(this);
        images4 = new ArrayList<>();

        gagaredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);




        images2 = (ArrayList<Image2>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");
        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images2.size());
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        setCurrentItem(selectedPosition);

        final FabOptions fabOptions = v.findViewById(R.id.fab_id);
        fabOptions.setButtonsMenu(R.menu.fabmenu);
        fabOptions.setFabColor(R.color.truered);
        fabOptions.setOnClickListener(this);

         // If you call AudienceNetworkAds.buildInitSettings(Context).initialize()
        // in Application.onCreate() this call is not really necessary.
        // Otherwise call initialize() onCreate() of all Activities that contain ads or
        // from onCreate() of your Splash Activity.
        AudienceNetworkInitializeHelper.initialize(getActivity());
        //---------------------------Ad Banner Code----------------------------------------------


        //   com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this, "my ID", com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        adView = new AdView(getActivity(), "155871289992615_237328858513524", AdSize.BANNER_HEIGHT_50);


// Find the Ad Container
        LinearLayout adContainer = (LinearLayout) v.findViewById(R.id.banner_container);

// Add the ad view to your activity layout
        adContainer.addView(adView);
        //AdSettings.addTestDevice("cb10b985-eb21-4d8e-82b3-569763a772ba");

// Request an ad
        adView.loadAd();
        com.facebook.ads.AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
// Ad error callback
                Toast.makeText(
                    getActivity(),
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
        interstitialAd = new com.facebook.ads.InterstitialAd(getActivity(), "155871289992615_156184313294646");

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
                goToNext();
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


        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);

    }

    // page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private void displayMetaInfo(int position) {

        adapter.Image2 image = images2.get(position);



    }
//


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    // adapter
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);
            ImageView imageViewPreview = view.findViewById(R.id.image_preview);
            final Image2 image2 = images2.get(position);
            Glide.with(getActivity()).load(image2.getUrl())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.placeholder)
                    .into(imageViewPreview);
            container.addView(view);
//Intent to Save Class




   return view;
        }



        private void remove() {
                int currentItem = viewPager.getCurrentItem();
            final Image2 image2 = images2.get(currentItem);

            // SqLite database handler
            db = new SQLiteHandler(getActivity());
            HashMap<String, String> user = db.getUserDetails();


            String picEmail = image2.getEmail();
            final String picID = image2.getUrl();

            final String email = user.get("email");

            final String album_table_name = image2.getAlbum_name();

            if (picEmail.equals(email)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Hey! " + album_table_name + " are you sure you want to Remove this Style?").setCancelable(false).setPositiveButton("Yea", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String imgpath = picID;
                        String user_album_name = album_table_name;
                        String user_email = email;
                        Intent i = new Intent(getActivity(), Remove_Design.class);
// sending data to new activity
                        i.putExtra("img_path", imgpath);
                        i.putExtra("user_album_name", user_album_name);
                        i.putExtra("user_email", user_email);
                        startActivity(i);


                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Remove style");
                alertDialog.show();

            } else {
                MyToast.showToast(getContext(), "Sorry, this design is not from your album");
            }

        }

        @Override
        public int getCount() {
            return images2.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }





    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        // Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
        int currentItem = viewPager.getCurrentItem();
        final Image2 image2 = images2.get(currentItem);

        if (!Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.DIRECTORY_DOWNLOADS);
            file.mkdirs();
        } else {
            File dir = new File(Environment.DIRECTORY_DOWNLOADS);
            dir.mkdirs();

        }

        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("hhh");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //  request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        MyToast.showToast(getContext(), "Saving design...");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, image2.getName() + "_naija fashion hive");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE).setAllowedOverRoaming(
                false).setTitle(image2.getName() + "_Naija Fashion Hive").setDescription("saving to gallery....");
        //request.setDestinationInExternalPublicDir("Ydsapp/","ydsapp.png");
        Long reference = downloadManager.enqueue(request);
        // downloadManager.enqueue(request);

    }
    public static void filterByPackageName(Context context,Intent intent, String prefix){
        List<ResolveInfo>matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches){
            if (info.activityInfo.packageName.toLowerCase().startsWith(prefix)){
                intent.setPackage(info.activityInfo.packageName);
                return;
            }
        }
    }
    public static String urlEncode(String s){
        try{
            return URLEncoder.encode(s,"UTF-8");
        }
        catch (UnsupportedEncodingException e){
            // Log.wft("","UTF-8 should always be supported", e);
            return "";
        }
    }



    public void rateApp(){
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent rt = new Intent(Intent.ACTION_VIEW, uri);
        //application
        rt.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            startActivity(rt);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/apps/details?id=" + getActivity().getPackageName())));
        }
    }
    public  void goToNext(){
        int currentItem = viewPager.getCurrentItem();
        final Image2 image2 = images2.get(currentItem);
        String imgText =  image2.getAlbum_name();
        Intent i = new Intent(getContext(), SaveImage.class);
// sending data to new activity
        i.putExtra("imgName", image2.getName());
        i.putExtra("imgUrl", image2.getUrl());
        i.putExtra("imgText", imgText);
        i.putExtra("imgEmail", image2.getEmail());
        startActivity(i);
       // getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_share:
                share();
                break;
            case R.id.action_save:
                if (interstitialAd == null
                        || !interstitialAd.isAdLoaded()
                        || interstitialAd.isAdInvalidated()) {
                    // Ad not ready to show.
                    goToNext();
                } else {
                    interstitialAd.show();
                }

        }
    }
    private void share() {
        int currentItem = viewPager.getCurrentItem();
        final Image2 image2 = images2.get(currentItem);
        String link = image2.getUrl();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String sharebody = "Hey! i just Discovered this hairstyle idea on Hairminion Check it out here... " + link;
        share.putExtra(Intent.EXTRA_TEXT, sharebody);
        startActivity(Intent.createChooser(share, "SHARE VIA"));
    }

}

