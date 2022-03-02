package com.hairstyle.hairstyleapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Helper.ImageUtils;
import Helper.RealPathUtil;
import Helper.SQLiteHandler;
import adapter.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Upload_Design extends AppCompatActivity {

    Button upload_button;
    private static final int REQUEST_CAMERA = 16001;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_GALLERY = 10702;
    private final String TAG = this.getClass().getName();
    ImageView Upload_Demo;
    private final CharSequence[] items = {"Take Photo", "From Gallery"};
    private ProgressDialog pDialog;
    private Uri selectedImageUri;
    private Bitmap mBitmap;
    SQLiteHandler db;
    ScrollView scrollView;
    //ads

    private EditText albumNameText;
    private ArrayList<String> title;

    private JSONArray result;
    String SubEndpoint;
    private String err;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__design);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // hide scrollbar
        setTitle("Upload hairstyles");

        // Toast.makeText(UploadDesign.this,catToServer,Toast.LENGTH_LONG).show();
        //ads code


        pDialog = new ProgressDialog(this);
        title = new ArrayList<>();

        albumNameText = findViewById(R.id.enterAlbumName);


        scrollView = findViewById(R.id.scrollV1);
        scrollView.setVerticalScrollBarEnabled(false);




        Upload_Demo = findViewById(R.id.Upload_Demo1);
        Upload_Demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooserDialog();
            }
        });

        upload_button = findViewById(R.id.upload_button1);

        upload_button.setEnabled(false);


albumNameText.addTextChangedListener(TEW);

        if (albumNameText.getText().toString().trim().length()> 0){
            upload_button.setEnabled(true);


        }else {
            upload_button.setEnabled(false);

        }
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    execMultipartPost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
    private final TextWatcher TEW = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() <= 1) {
              albumNameText.setError("Album name should be more than 4 letters");
                upload_button.setEnabled(false);
            } else if (s.length() >= 2) {
                upload_button.setEnabled(true);

            }
            if (s.length() == 25) {
                albumNameText.setError(" maximium letter reached!!!");
                upload_button.setEnabled(true);

            }
        }};

    private void getSpinnerTitle(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                title.add(json.getString(AppConfig.TAG_USERNAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner


    }


    private void openFileChooserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Picture");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        initCameraPermission();
                        break;
                    case 1:
                        initGalleryIntent();
                        break;
                    default:
                }
            }
        });
        builder.show();
    }

  //  Uri uri = Uri.parse("android.resource://com.yourdesignapp.hairstyleapp/drawable/ic_share");
  //  InputStream path = getContentResolver().openInputStream(uri);
//Resources resources =  getApplicationContext().getResources();
   // Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(R.drawable.ic_share) + '/' + resources.getResourceTypeName(R.drawable.ic_share)+ '/' + resources.getResourceEntryName(R.drawable.ic_share));

    private void execMultipartPost() throws Exception {
        pDialog.setMessage("Uploading design...");
        pDialog.show();

        SubEndpoint = albumNameText.getText().toString().trim();

// Uri uriFromPath = Uri.fromFile(new File(realPath));
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();

      //  MyToast.showToast(Upload_Design.this,SubEndpoint);
       MyToast.showToast(Upload_Design.this,"Uploading...");

        Toast.makeText(Upload_Design.this,realPath,Toast.LENGTH_LONG).show();
        // realPath = p.toString();
        File largeFile = new File(realPath);
        final  File file = resizeFile(largeFile);
        String contentType = file.toURL().openConnection().getContentType();
        Log.d(TAG, "file: " + file.getPath());
        Log.d(TAG, "contentType: " + contentType);
        RequestBody fileBody = RequestBody.create(MediaType.parse(contentType), file);
        final String filename = "file_" + System.currentTimeMillis() / 1000L;
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileUploadType", "1")
                .addFormDataPart("miniType", contentType)
                .addFormDataPart("ext", file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")))
                .addFormDataPart("fileTypeName", "img")
                .addFormDataPart("clientFilePath", selectedImageUri.getPath())
                .addFormDataPart("filedata", filename + ".png", fileBody)
                .build();
        Request request = new Request.Builder()
              //.url("http://www.yourdesignsapp.com/wp-content/themes/himalayas/account/clothings/"+SubEndpoint+".php")
                 .url("https://www.hairminion.com/files/tocci/"+SubEndpoint+".php")
                .post(requestBody)
                .build();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        pDialog.hide();
                        String error = e.getMessage();
                        MyToast.showToast(Upload_Design.this,error);
                        MyToast.showToast(Upload_Design.this, "Upload failed");

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                err = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.hide();
                        Log.d("okhttp Response", err);
                        MyToast.showToast(Upload_Design.this,err);



//
                    }
                });
            }
            });
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void initCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Permission to use Camera", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            initCameraIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCameraIntent();
            } else {
                Toast.makeText(this, "Permission denied by user", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void initGalleryIntent() {
        MyToast.showToast(this, "Opening documents");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }
    private void initCameraIntent() {

        MyToast.showToast(this, "Opening camera");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getOutputMediafile(1);
        selectedImageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }
    private File getOutputMediafile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name)
        );
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }
        return mediaFile;
    }

    String realPath = "";
    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                realPath = selectedImageUri.getPath();
            } else if (requestCode == REQUEST_GALLERY) {
                selectedImageUri = data.getData();
                if (Build.VERSION.SDK_INT < 11) {
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
                } else if (Build.VERSION.SDK_INT < 19) {

                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                } else {
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                    MyToast.showToast(Upload_Design.this,realPath);
                }
                Log.d(TAG, "real path: " + realPath);
            }
            mBitmap = ImageUtils.getScaledImage(selectedImageUri, this);
            setImageBitmap(mBitmap);
        }
    }
    private void setImageBitmap(Bitmap bm) {
        Upload_Demo.setImageBitmap(bm);
        upload_button.setClickable(true);
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
    public File resizeFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }
}