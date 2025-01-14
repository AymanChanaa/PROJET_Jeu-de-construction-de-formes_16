package com.example.shape_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.shape_project.R;

import java.io.File;
import java.util.List;

public class ImageGridAdapter extends BaseAdapter {
    private Context context; // Contexte pour interagir avec l'application
    private List<File> imageFiles; // Liste des fichiers image à afficher dans la grille

    // Constructeur pour initialiser le contexte et la liste des fichiers image
    public ImageGridAdapter(Context context, List<File> imageFiles) {
        this.context = context;
        this.imageFiles = imageFiles;
    }

    @Override
    // Retourner le nombre d'éléments dans la liste des fichiers image
    public int getCount() {
        return imageFiles.size();
    }

    @Override
    // Retourner l'élément à la position spécifiée (un fichier image)
    public Object getItem(int position) {
        return imageFiles.get(position);
    }

    @Override
    // Retourner l'identifiant unique de l'élément à la position spécifiée
    public long getItemId(int position) {
        return position;
    }

    @Override
    // Retourner la vue pour un élément à la position spécifiée dans la grille
    public View getView(int position, View convertView, ViewGroup parent) {
        // Si la vue convertie est null, créer une nouvelle vue pour l'élément
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_image, parent, false);
        }

        // Récupérer l'ImageView de l'élément de la grille
        ImageView imageView = convertView.findViewById(R.id.imageView);

        // Récupérer le fichier image à la position spécifiée
        File imageFile = imageFiles.get(position);

        // Utiliser Glide pour charger l'image dans l'ImageView
        Glide.with(context).load(imageFile).into(imageView);

        return convertView; // Retourner la vue de l'élément
    }
}
