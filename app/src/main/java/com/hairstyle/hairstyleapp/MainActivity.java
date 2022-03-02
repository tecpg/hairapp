package com.hairstyle.hairstyleapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.hairstyle.hairstyleapp.app.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import adapter.GalleryAdapter2;
import adapter.Image2;
import adapter.SlideshowDialogFragment2;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private String TAG = MainActivity.class.getSimpleName();

    private ArrayList<Image2> images;

    private GalleryAdapter2 mAdapter2;
     StaggeredGridLayoutManager gagaredGridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

//String SubEndPoint;
private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;

    private String vCode = BuildConfig.VERSION_NAME;
//Horinzontal CardView
RecyclerView recyclerView1;
    RecyclerView rLatest;
    ArrayList<String> Number;
    private ArrayList<Image2> images2;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout ;
    View ChildView ;
    int RecyclerViewItemPosition ;
TextView stitle,sub;
private String query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            query = bundle.getString("query");

        }



//Welcome Slider Code
//Swipe Refresh
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(MainActivity.this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_red_light));



//Permmision Status
        permissionStatus = getSharedPreferences("permissionStaus", MODE_PRIVATE);
        // getData();
        setTitle("");
        stitle = findViewById(R.id.styletitle);
        sub = findViewById(R.id.sub);
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,String.format(Locale.US, "fonts/%s","Oxanium-Bold.ttf"));
        Typeface typeface2 = Typeface.createFromAsset(am,String.format(Locale.US, "fonts/%s","Oxanium-Light.ttf"));
        stitle.setTypeface(typeface);
        sub.setTypeface(typeface2);

//Buttons



       // txtRegId = (TextView) findViewById(R.id.txt_reg_id);
       // txtMessage = (TextView) findViewById(R.id.txt_push_message);

//---------------------Horizontal CardView---------------------------------------



        checkConn();
        //ads code#
//-------------------------------------Ask for Permission Code----------------------------------------

    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //Show Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Allow Storage Permission");
            builder.setIcon(R.drawable.error);
            builder.setMessage("To save any image you need to allow storage permission");
            builder.setCancelable(false);
            builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
            //Previously Permission Request was cancelled with 'Dont Ask Again',
            // Redirect to Settings after showing Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Need Storage Permission");
            builder.setMessage("This app needs storage permission.");
            builder.setIcon(R.drawable.error);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    sentToSettings = true;
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
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
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
        }

        SharedPreferences.Editor editor = permissionStatus.edit();
        editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
        editor.commit();


    } else {
        //You already have the permission, just go ahead.
        proceedAfterPermission();
    }




//--------------------------------------------------//GALLERY CODE------------------------------------------------------
        rLatest = findViewById(R.id.Rlatest);
        rLatest.setHasFixedSize(false);
        // pDialog = new ProgressDialog(this);
        images2 = new ArrayList<>();

        gagaredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rLatest.setLayoutManager(gagaredGridLayoutManager);
        rLatest.setItemAnimator(new DefaultItemAnimator());
        mAdapter2 = new GalleryAdapter2(getApplicationContext(), images2);
        rLatest.setAdapter(mAdapter2);
        rLatest.addOnItemTouchListener(
                new  GalleryAdapter2.RecyclerTouchListener(
                        getApplicationContext(), rLatest, new GalleryAdapter2.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", images2);
                        bundle.putInt("position", position);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment2 slideshowDialogFragment2 = SlideshowDialogFragment2.newInstance();
                        slideshowDialogFragment2.setArguments(bundle);
                        slideshowDialogFragment2.show(ft, "slideshow");
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }

                )

        );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_share:
                Toast.makeText(MainActivity.this,"rate",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
      checkConn();
    }

//    String SubEndPoint = Number.get(RecyclerViewItemPosition);
    @TargetApi(Build.VERSION_CODES.M)

  /////////////////////--------------------------SECOND MAIN RECYCLERVIEW CODE-------------------------

    public void fetchImages2() {

        swipeRefreshLayout.setRefreshing(true);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!= null && networkInfo.isConnectedOrConnecting()){


            final String endpoint = "https://www.hairminion.com/files/tocci/"+query+".php/";

            JsonArrayRequest req = new JsonArrayRequest(endpoint,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            //pDialog.hide();
                            images2.clear();

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);

                                    adapter.Image2 image2 = new adapter.Image2();
                                    // JSONObject url = object.getJSONObject("url");
                                    image2.setUrl(object.getString("link"));
                                    image2.setAlbum_name(object.getString("album_name"));

                                    images2.add(image2);

                                } catch (JSONException e) {
                                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                                }
                            }
                            mAdapter2.notifyDataSetChanged();

                            // stopping swipe refresh
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
 // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);  }
            });

            req.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        }else {
            Toast.makeText(this,"Waiting for connection. Unable to fetch styles now",Toast.LENGTH_LONG).show();
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
        }}



    private void checkConn(){
    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    if (networkInfo!= null && networkInfo.isConnectedOrConnecting()){
       // MyToast.showToast(MainActivity.this,"Fetching " + Number.get(RecyclerViewItemPosition).toString() + " Styles...");
       // fetchImages();
      // images2.clear();
        fetchImages2();
    }else {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.error);
        builder.setMessage("Unable to fetch styles now, check network connection").setCancelable(false).setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              //  fetchImages();
                fetchImages2();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Couldn't refresh style feed");
        alertDialog.show();

   }

}
    public void mini(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }

//Permission Request Code
    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
       // Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Allow Storage Permission");
                    builder.setIcon(R.drawable.error);
                    builder.setMessage("To save any image you need to allow storage permission");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


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
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }


    @Override
    public void onDestroy() {
        //  Toast.makeText(getActivity(),"time destroyed"+ String.valueOf(getRemainingTime()), Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }

}

