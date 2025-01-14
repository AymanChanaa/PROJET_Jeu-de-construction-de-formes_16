package com.example.shape_project.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.widget.GridView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shape_project.adapter.ImageGridAdapter;
import com.example.shape_project.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AchievementsActivity extends AppCompatActivity {
    private RecyclerView recyclerView; // RecyclerView pour afficher les images en grille
    private List<File> imageFiles = new ArrayList<>(); // Liste pour stocker les fichiers image
    // private ImageAdapter imageAdapter; // Adaptateur pour recycler les images


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements); // Définir le layout de l'activité

        // Vérifier si l'application a la permission de lire le stockage externe
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Si la permission n'est pas accordée, demander la permission à l'utilisateur
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100); // Code de demande unique (ici 100)
        }

        // Initialisation de la GridView (grille pour afficher les images)
        GridView gridView = findViewById(R.id.gridViewImages);

        // Récupérer le chemin du dossier passé à l'activité via l'intent
        String folderPath = getIntent().getStringExtra("folderPath");
        if (folderPath != null) {
            File folder = new File(folderPath); // Créer un objet File pour le dossier

            // Vérifier si le dossier existe et est un répertoire
            if (folder.exists() && folder.isDirectory()) {
                // Lister les fichiers du dossier qui sont des images PNG
                File[] files = folder.listFiles(file -> file.isFile() && file.getName().endsWith(".png"));
                if (files != null) {
                    // Loguer les fichiers d'images trouvés pour debug
                    for (File file : files) {
                        Log.d("Image Files", "File: " + file.getAbsolutePath());
                    }
                    // Ajouter les fichiers à la liste d'images
                    imageFiles.addAll(Arrays.asList(files));
                }
            }
        }

        // Set up the adapter and pass context
        // imageAdapter = new ImageAdapter(this, imageFiles);
        // recyclerView.setAdapter(imageAdapter);

        // Initialisation de l'adaptateur pour la GridView
        ImageGridAdapter adapter = new ImageGridAdapter(this, imageFiles);
        // Définir l'adaptateur pour la GridView
        gridView.setAdapter(adapter);
    }
}
