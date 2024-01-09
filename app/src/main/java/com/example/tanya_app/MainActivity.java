package com.example.tanya_app;
import android.Manifest;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;


import android.app.Activity;
import android.app.IntentService;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tanya_app.databinding.ActivityMainBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 2;
    private ImageView popupImage;

    private ImageView imageView;

    public static RequestQueue requestQueue;

    private Uri selectedImageUri;

    Dialog create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        create = new Dialog(this);
        popupImage = create.findViewById(R.id.popupImage);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);
        }
        ImageButton imageButton = findViewById(R.id.buttonProfile);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity();
            }
        });
    }

    public void TambahPopUp(View view) {
        try {
            Dialog create = new Dialog(this);
            create.setContentView(R.layout.popup);

            Button choose = create.findViewById(R.id.chooseImage);
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePicker.with(MainActivity.this)
                            .galleryOnly()
                            .start();
                }
            });

            Button createButton = create.findViewById(R.id.ButtonCreate);
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Mendapatkan nilai yang diinputkan dari elemen-elemen di dalam popup
                    EditText editText = create.findViewById(R.id.editTextText2);
                    String editTextValue = editText.getText().toString();

                    // Lakukan sesuatu dengan nilai yang diinputkan, contoh:
                    Toast.makeText(MainActivity.this, "Nilai yang diinputkan: " + editTextValue, Toast.LENGTH_SHORT).show();

                    // Tampilkan nilai pada TextView di tampilan utama
                    showTextInMainView(editTextValue);

                    // Tampilkan gambar pada ImageView di tampilan utama
                    showImageInMainView(selectedImageUri);

                    // Tutup popup setelah mendapatkan nilai
                    create.dismiss();
                }
            });

            create.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            create.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
        } else {
            Toast.makeText(MainActivity.this, "Image picking failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showTextInMainView(String value) {
        // Tampilkan nilai yang diinputkan pada TextView di tampilan utama
        TextView mainTextView = findViewById(R.id.pertanyaan1);
        mainTextView.setText(value);
    }

    private void showImageInMainView(Uri uri) {
        // Lakukan sesuatu dengan URI gambar, misalnya tampilkan di ImageView pada main view
        if (uri != null) {
            ImageView mainImageView = findViewById(R.id.gambarPertanyaan1);
            mainImageView.setImageURI(uri);
        }
    }

    private void startNewActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}



