package com.example.tanya_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileActivity extends AppCompatActivity {

    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile = findViewById(R.id.buttonProfile);
        FloatingActionButton button = findViewById(R.id.gantiProfile);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(ProfileActivity.this)
                        .galleryOnly()
                        .start();
            }
        });

            ImageView imageView = findViewById(R.id.imageView3);

            // Convert ImageView to Bitmap
            Bitmap bitmap = drawableToBitmap(imageView.getDrawable());

            // Apply blur effect to the Bitmap
            Bitmap blurredBitmap = blurBitmap(this, bitmap, 25f); // You can adjust the blur radius as needed

            // Set the blurred Bitmap to the ImageView
            imageView.setImageBitmap(blurredBitmap);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        profile.setImageURI(uri);
    }

    private Bitmap blurBitmap(Context context, Bitmap bitmap, float radius) {
            Bitmap inputBitmap = bitmap.copy(bitmap.getConfig(), true);

            // Create RenderScript
            RenderScript renderScript = RenderScript.create(context);

            // Create an allocation from Bitmap
            Allocation input = Allocation.createFromBitmap(renderScript, inputBitmap);
            Allocation output = Allocation.createTyped(renderScript, input.getType());

            // Create a script
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

//            // Set the input for the script
//            script.setInput(input);
//
//            // Set the blur radius
//            script.setRadius(radius);
//
//            // Perform the blur
//            script.forEach(output);
//
//            // Copy the output allocation to the blurred Bitmap
//            output.copyTo(inputBitmap);
//
//            // Release resources
//            renderScript.destroy();

            return inputBitmap;
        }

        private Bitmap drawableToBitmap(Drawable drawable) {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }
    }
