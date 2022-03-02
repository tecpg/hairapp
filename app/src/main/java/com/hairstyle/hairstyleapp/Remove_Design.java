package com.hairstyle.hairstyleapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import adapter.MyToast;
import adapter.SlideshowDialogFragment2;


public class Remove_Design extends AppCompatActivity {
    private static final String TAG = Remove_Design.class.getSimpleName();

    private Button dBtn;
    private ProgressDialog pDialog;
    private ImageView rImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove__design);
setTitle("Remove Design");



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



       dBtn = findViewById(R.id.dBtn);

        Intent i = getIntent();
        // getting attached intent data
      final String  imgId = i.getStringExtra("img_path");
      final String  albumName = i.getStringExtra("user_album_name");

        // Login button Click Event
        dBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String img_path = imgId;
                String user_album_name = albumName;
MyToast.showToast(Remove_Design.this,user_album_name);
                // login user
                    remove(img_path,user_album_name);
            }

        });

        rImg = findViewById(R.id.rImg);

        rImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Glide
                            .with(Remove_Design.this)
                            .load(imgId)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.ic_profile)
                            .into(rImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {

            Glide
                    .with(Remove_Design.this)
                    .load(imgId)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_profile)
                    .into(rImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * function to verify login details in mysql db
     * */
    private void remove(final String img_path, final String user_album_name) {
        // Tag used to cancel the request
         String tag_string_req = "req_login";

        pDialog.setMessage("Removing Design ...");

        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REMOVE_DESIGN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Error Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        // Inserting row in users table
                      //  db.addUser(name, email, uid, created_at, status, phone, fbk_id, insta_id, image_path);
                       MyToast.showToast(getApplicationContext(),"Design removed successfully!");
                        Intent i = new Intent(Remove_Design.this,MainActivity.class);
                        startActivity(i);
try {
    // Launch main activity
    Intent intent = new Intent(Remove_Design.this,
            SlideshowDialogFragment2.class);
    startActivity(intent);
}catch (Exception e){
    e.printStackTrace();
}

                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        MyToast.showToast(getApplicationContext(),
                                errorMsg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    MyToast.showToast(getApplicationContext(), "Json error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                MyToast.showToast(getApplicationContext(),
                        error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("image_path", img_path);
                params.put("user_album_name",user_album_name);

                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(15000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public  void onPause(){

        super.onPause();
    }

    @Override
    public  void onResume(){
        super.onResume();

    }

    @Override
    public  void onDestroy(){

        super.onDestroy();
    }
}
