package com.hairstyle.hairstyleapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ViewImage extends AppCompatActivity {

    // Declare Variable
    TextView text;
    ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Retrieve data from MainActivity on GridView item click
        Intent i = getIntent();

// Get the position
        int position = i.getExtras().getInt("position");

// Get String arrays FilePathStrings
        String[] filepath = i.getStringArrayExtra("filepath");

// Get String arrays FileNameStrings
      //  String[] filename = i.getStringArrayExtra("filename");

// Locate the TextView in view_image.xml
        text = findViewById(R.id.imagetext);

// Load the text into the TextView followed by the position
      //  text.setText(filename[position]);

// Locate the ImageView in view_image.xml
        imageview = findViewById(R.id.full_image_view);

// Decode the filepath with BitmapFactory followed by the position
        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);

// Set the decoded bitmap into ImageView
        imageview.setImageBitmap(bmp);

    }
    }


