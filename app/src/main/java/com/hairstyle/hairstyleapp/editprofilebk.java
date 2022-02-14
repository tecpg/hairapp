package com.hairstyle.hairstyleapp;

/**
 * Created by PASCHAL GREEN on 10/30/2016.
 * package com.yourdesignsapp.hairstyleapp;

 import android.com.yourdesignsapp.hairstyleapp.app.AlertDialog;
 import android.com.yourdesignsapp.hairstyleapp.app.ProgressDialog;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.graphics.drawable.BitmapDrawable;
 import android.net.Uri;
 import android.os.Bundle;
 import android.os.Environment;
 import android.provider.MediaStore;
 import android.provider.SyncStateContract;
 import android.support.v7.com.yourdesignsapp.hairstyleapp.app.AppCompatActivity;
 import android.support.v7.widget.Toolbar;
 import android.util.Log;
 import android.view.View;
 import android.widget.Button;

 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.google.android.gms.common.api.GoogleApiClient;

 import org.json.JSONObject;

 import java.io.File;
 import java.io.IOException;
 import java.util.HashMap;


 import Helper.SQLiteHandler;
 import okhttp3.Call;
 import okhttp3.Callback;
 import okhttp3.MediaType;
 import okhttp3.MultipartBody;
 import okhttp3.OkHttpClient;
 import okhttp3.Request;
 import okhttp3.RequestBody;
 import okhttp3.Response;


 public class Edit_Profile extends AppCompatActivity {
 ImageView displayrofile;
 static final int REQUEST_IMAGE_CAPTURE = 1;
 static final int GALLERY_REQUEST = 13;
 private final String TAG = this.getClass().getName();
 SQLiteHandler db;
 private Bitmap ImageToUpload;
 private ProgressDialog loading;
 Button userUpdateBtn;
 private File file;
 private String encoded_string, image_name;
 private Uri file_uri;
 ProgressDialog progressDialog;
 private EditText et_response;



 /**
 * ATTENTION: This was auto-generated to implement the App Indexing API.
 * See https://g.co/AppIndexing/AndroidStudio for more information.

private GoogleApiClient client;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        et_response = (EditText) findViewById(R.id.et_response);
        userUpdateBtn = (Button) findViewById(R.id.userUpdateBtn);
        userUpdateBtn.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        try {
        execMultipartPost();
        } catch (Exception e) {
        e.printStackTrace();
        }
        }
        });

        displayrofile = (ImageView) findViewById(R.id.displayProfile);
        displayrofile.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
final CharSequence[] editOptions = {"GALLERY", "CAMERA", "REMOVE PHOTO"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Profile.this);
        builder.setTitle("Select photo");
        builder.setItems(editOptions, new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
        switch (which) {
        case 0:
        Toast.makeText(getApplicationContext(), "Loading Gallery...", Toast.LENGTH_SHORT).show();
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (openGallery.resolveActivity(getPackageManager()) != null) ;
        startActivityForResult(openGallery, GALLERY_REQUEST);
        break;
        case 1:
        Toast.makeText(getApplicationContext(), "Loading Camera...", Toast.LENGTH_SHORT).show();

        Intent snap = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (snap.resolveActivity(getPackageManager()) != null) {
        File photoFile = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
        snap.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        snap.putExtra("crop", "true");
        snap.putExtra("aspectX", 0);
        snap.putExtra("aspectY", 0);
        snap.putExtra("outputX", 450);
        snap.putExtra("outputY", 600);
        snap.putExtra("return-data", true);
        startActivityForResult(snap, REQUEST_IMAGE_CAPTURE);
        }
        break;
        case 2:
        displayrofile.setImageBitmap(null);

        break;

        }
        }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        }
        });
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        TextView currentStatus = (TextView) findViewById(R.id.currentStatus);
        HashMap<String, String> user = db.getUserDetails();
        String status = user.get("status");
        currentStatus.setText(status);



        }


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
        displayrofile.setImageBitmap(decodeSampledBitmapFromFile(file.getAbsolutePath(), 600, 450));
        }
        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK && null != data) {
        Uri selectedImage = data.getData();
        displayrofile.setImageURI(selectedImage);


        }
        }
private void execMultipartPost() throws Exception {
// Uri uriFromPath = Uri.fromFile(new File(realPath));
        String contentType = file.toURL().openConnection().getContentType();
        Log.d(TAG, "file: " + file.getPath());
        Log.d(TAG, "contentType: " + contentType);
        RequestBody fileBody = RequestBody.create(MediaType.parse(contentType), file);
final String filename = "file_" + System.currentTimeMillis() / 1000L;
        RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("user_id", "1")
        .addFormDataPart("image", filename + ".jpg", fileBody)
        .build();
        Request request = new Request.Builder()
        .url(AppConfig.URL_UPLOAD)
        .post(requestBody)
        .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
@Override
public void onFailure(Call call, final IOException e) {
        runOnUiThread(new Runnable() {
@Override
public void run() {
        et_response.setText(e.getMessage());
        Toast.makeText(Edit_Profile.this, "nah", Toast.LENGTH_SHORT).show();
        }
        });
        }
@Override
public void onResponse(Call call, final Response response) throws IOException {
        runOnUiThread(new Runnable() {
@Override
public void run() {
        try {
        et_response.setText(response.body().string());
        } catch (IOException e) {
        e.printStackTrace();
        }
        }
        });
        }
        });
        }
public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
final int width = options.outWidth;
final int height = options.outHeight;

        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int intSampleSize = 1;
        if (height > reqHeight) {
        intSampleSize = Math.round((float) height / (float) reqHeight);}
        int expectWidth = width / intSampleSize;
        if (expectWidth > reqWidth) {
        intSampleSize = Math.round((float) width / (float) reqWidth);
        }
        //options.intSampleSize = intSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);

        }

        }

        */
