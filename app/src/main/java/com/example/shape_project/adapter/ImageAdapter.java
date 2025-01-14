package com.example.shape_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shape_project.R;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final List<File> imageFiles; // Liste des fichiers image à afficher
    private final Context context; // Contexte pour interagir avec l'application

    // Constructeur pour initialiser le contexte et la liste des fichiers image
    public ImageAdapter(Context context, List<File> imageFiles) {
        this.context = context;
        this.imageFiles = imageFiles;
    }

    @NonNull
    @Override
    // Cette méthode est utilisée pour créer la vue de chaque élément de la RecyclerView
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflater le layout item_image pour chaque élément de la RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    // Cette méthode est utilisée pour lier chaque élément (image) à la vue correspondante dans la RecyclerView
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // Récupérer le fichier image pour cet élément
        File imageFile = imageFiles.get(position);
        Log.d("Image Adapter", "Loading image: " + imageFile.getAbsolutePath());

        // Décoder le fichier image en Bitmap pour l'afficher dans l'ImageView
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        // Vérifier si le Bitmap a été correctement décodé et l'afficher dans l'ImageView
        if (bitmap != null) {
            holder.imageView.setImageBitmap(bitmap);
        } else {
            Log.e("ImageAdapter", "Failed to decode image: " + imageFile.getAbsolutePath());
        }

        // Définir un OnClickListener pour gérer les clics sur l'image
        holder.itemView.setOnClickListener(v -> {
            Log.d("ImageAdapter", "Image clicked: " + imageFile.getAbsolutePath());

            // Créer un Intent pour ouvrir l'image dans une visionneuse d'images (par exemple, Google Photos)
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri imageUri = Uri.fromFile(imageFile); // Créer un Uri à partir du fichier image
            intent.setDataAndType(imageUri, "image/*");

            // Vérifier les applications disponibles pouvant afficher l'image
            List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);
            if (resolveInfoList.size() > 0) {
                // Si des applications compatibles sont trouvées, démarrer l'activité
                context.startActivity(intent);
            } else {
                // Sinon, afficher un message d'erreur
                Log.e("ImageAdapter", "No apps found to view the image");
                Toast.makeText(context, "No app found to view the image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    // Retourner le nombre total d'éléments (images) dans la liste
    public int getItemCount() {
        return imageFiles.size();
    }

    // Classe interne pour gérer les vues de chaque élément (ImageView)
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView; // ImageView pour afficher l'image

        // Constructeur pour initialiser l'ImageView à partir de la vue
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem); // Récupérer l'ImageView de l'élément
        }
    }
}
