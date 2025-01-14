package com.example.shape_project;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.GridView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AchievementsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<File> imageFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        // Check if the app has permission to read external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100); // Use a unique request code, e.g., 100
        }

        GridView gridView = findViewById(R.id.gridViewImages);
        // recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Grid layout with 3 columns

        // Get the folder path from the intent
        String folderPath = getIntent().getStringExtra("folderPath");
        if (folderPath != null) {
            File folder = new File(folderPath);

            // List image files in the folder
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles(file -> file.isFile() && file.getName().endsWith(".png"));
                if (files != null) {
                    for (File file : files) {
                        Log.d("Image Files", "File: " + file.getAbsolutePath());
                    }
                    imageFiles.addAll(Arrays.asList(files));
                }
            }
        }

        // Set up the adapter and pass context
        // imageAdapter = new ImageAdapter(this, imageFiles);
        // recyclerView.setAdapter(imageAdapter);

        ImageGridAdapter adapter = new ImageGridAdapter(this, imageFiles);
        gridView.setAdapter(adapter);
    }
}
