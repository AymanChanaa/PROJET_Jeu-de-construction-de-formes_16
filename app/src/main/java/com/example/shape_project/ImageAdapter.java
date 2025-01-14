package com.example.shape_project;

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

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final List<File> imageFiles;
    private final Context context;

    public ImageAdapter(Context context, List<File> imageFiles) {
        this.context = context;
        this.imageFiles = imageFiles;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        File imageFile = imageFiles.get(position);
        Log.d("Image Adapter", "Loading image: " + imageFile.getAbsolutePath());

        // Decode the image file into a Bitmap for displaying in the RecyclerView
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        // Set the Bitmap to the ImageView
        if (bitmap != null) {
            holder.imageView.setImageBitmap(bitmap);
        } else {
            Log.e("ImageAdapter", "Failed to decode image: " + imageFile.getAbsolutePath());
        }

        // Set the OnClickListener to handle image clicks
        holder.itemView.setOnClickListener(v -> {
            Log.d("ImageAdapter", "Image clicked: " + imageFile.getAbsolutePath());

            // Create an Intent to open the image in a photo viewer (Google Photos or another)
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri imageUri = Uri.fromFile(imageFile);
            intent.setDataAndType(imageUri, "image/*");

            // Log the available apps that can handle this intent
            List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);
            if (resolveInfoList.size() > 0) {
                context.startActivity(intent);
            } else {
                Log.e("ImageAdapter", "No apps found to view the image");
                Toast.makeText(context, "No app found to view the image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
        }
    }
}
