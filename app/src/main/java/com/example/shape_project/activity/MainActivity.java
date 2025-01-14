package com.example.shape_project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shape_project.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Charge le layout principal de l'application

        // Bouton pour commencer l'apprentissage des formes
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnStart = findViewById(R.id.btnStart); // Récupère le bouton correspondant
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité principale d'apprentissage
                Intent intent = new Intent(MainActivity.this, LearningActivity.class);
                startActivity(intent); // Ouvre l'activité LearningActivity
            }
        });

        // Bouton pour voir les réalisations sauvegardées
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnViewAchievements = findViewById(R.id.btnViewAchievements); // Récupère le bouton correspondant
        btnViewAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupère le chemin du répertoire contenant les images sauvegardées
                File picturesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyShapes");
                String folderPath = picturesDir.getAbsolutePath(); // Chemin absolu du dossier

                // Lancer l'activité AchievementsActivity en passant le chemin du dossier
                Intent intent = new Intent(MainActivity.this, AchievementsActivity.class);
                intent.putExtra("folderPath", folderPath); // Ajoute le chemin comme paramètre
                startActivity(intent); // Ouvre l'activité AchievementsActivity
            }
        });

        // Bouton pour afficher l'aide
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnHelp = findViewById(R.id.btnHelp); // Récupère le bouton correspondant
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité d'aide
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent); // Ouvre l'activité HelpActivity
            }
        });
    }
}
