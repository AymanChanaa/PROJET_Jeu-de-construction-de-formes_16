package com.example.shape_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FullScreenImageActivity extends AppCompatActivity {
    private ImageView fullScreenImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        fullScreenImageView = findViewById(R.id.fullScreenImageView);

        // Get the image path passed from the previous activity
        String imagePath = getIntent().getStringExtra("imagePath");

        if (imagePath != null) {
            // Log the received image path
            Log.d("FullScreenImageActivity", "Received image path: " + imagePath);

            // Decode the image file into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            if (bitmap != null) {
                // Set the Bitmap to the ImageView
                fullScreenImageView.setImageBitmap(bitmap);
            } else {
                // Handle error if image decoding fails
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle error if image path is not received
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
        }
    }
}
