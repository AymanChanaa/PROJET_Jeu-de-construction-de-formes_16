package com.example.shape_project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bouton pour commencer l'apprentissage
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité principale d'apprentissage
                Intent intent = new Intent(MainActivity.this, LearningActivity.class);
                startActivity(intent);
            }
        });

        // Bouton pour voir les réalisations
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnViewAchievements = findViewById(R.id.btnViewAchievements);
        btnViewAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the directory path
                File picturesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyShapes");
                String folderPath = picturesDir.getAbsolutePath();

                // Launch AchievementsActivity with the folder path
                Intent intent = new Intent(MainActivity.this, AchievementsActivity.class);
                intent.putExtra("folderPath", folderPath);
                startActivity(intent);
            }
        });

        // Bouton pour afficher l'aide
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité d'aide
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

    }
}