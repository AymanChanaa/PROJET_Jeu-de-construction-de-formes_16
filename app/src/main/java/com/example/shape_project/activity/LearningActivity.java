package com.example.shape_project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.shape_project.R;
import com.example.shape_project.view.ShapeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LearningActivity extends AppCompatActivity {

    private ConstraintLayout canvasLayout;  // Zone de dessin pour les formes
    private ShapeView selectedShape; // Forme actuellement sélectionnée
    // Couleurs prédéfinies
    private int[] predefinedColors = {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.CYAN,
            Color.MAGENTA,
            Color.BLACK,
            Color.WHITE

    };
    private int currentColorIndex = 0;  // Index de la couleur actuelle
    private ImageView imageView; // ImageView pour afficher une image sélectionnée

    // Ouvre la galerie pour sélectionner une image
    private void openGallery() {
        // Launch gallery to pick an image
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private TextView timerTextView;  // Afficheur du minuteur
    private CountDownTimer countDownTimer; // Objet pour le compte à rebours
    private long timeLeftInMillis = 60000; // Temps restant (1 minute)

    // Démarre le compte à rebours
    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer(); // Met à jour l'affichage du minuteur
            }

            @Override
            public void onFinish() {
                Toast.makeText(LearningActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();

                // Désactive toutes les formes et les interactions
                List<ShapeView> allShapes = getAllViews(); // Get all the shapes on the canvas
                for (ShapeView shapeView : allShapes) {
                    shapeView.setEnabled(false); // Désactive l'interaction avec les formes
                }

                // Désactive les boutons et autres éléments d'interface
                SeekBar seekBarSize = findViewById(R.id.seekBarSize);
                seekBarSize.setEnabled(false); // Disable SeekBar

                Button btnAddCircle = findViewById(R.id.btnAddCircle);
                Button btnAddSquare = findViewById(R.id.btnAddSquare);
                Button btnAddRectangle = findViewById(R.id.btnAddRectangle);
                Button btnAddTriangle = findViewById(R.id.btnAddTriangle);
                Button btnSaveCanvas = findViewById(R.id.btnSaveCanvas);
                Button btnSelectImage = findViewById(R.id.btnSelectImage);
                Button btnChangeColor = findViewById(R.id.btnChangeColor);

                btnAddCircle.setEnabled(false);
                btnAddSquare.setEnabled(false);
                btnAddRectangle.setEnabled(false);
                btnAddTriangle.setEnabled(false);
                btnSaveCanvas.setEnabled(true);
                btnSelectImage.setEnabled(false);
                btnChangeColor.setEnabled(false);
            }

        }.start();
    }

    // Met à jour l'affichage du minuteur
    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftText = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftText);
    }

    // Gère l'image sélectionnée depuis la galerie
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        // Affiche l'image sélectionnée dans l'ImageView
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            imageView.setImageBitmap(bitmap);
                            startCountdownTimer();  // Lance le minuteur après sélection
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation des éléments de l'interface
        setContentView(R.layout.activity_learning);
        timerTextView = findViewById(R.id.timerText);
        canvasLayout = findViewById(R.id.canvasLayout);

        // Bouton pour réinitialiser l'application
        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> resetEverything());

        // Boutons pour ajouter des formes
        Button btnAddCircle = findViewById(R.id.btnAddCircle);
        btnAddCircle.setOnClickListener(v -> addShape("circle"));

        Button btnAddSquare = findViewById(R.id.btnAddSquare);
        btnAddSquare.setOnClickListener(v -> addShape("square"));

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnAddRectangle = findViewById(R.id.btnAddRectangle);
        btnAddRectangle.setOnClickListener(v -> addShape("rectangle"));

        Button btnAddTriangle = findViewById(R.id.btnAddTriangle);
        btnAddTriangle.setOnClickListener(v -> addShape("triangle"));

        imageView = findViewById(R.id.imageView);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);

        // Bouton pour sélectionner une image
        btnSelectImage.setOnClickListener(v -> openGallery());

        // Slider pour redimensionner la forme sélectionnée
        SeekBar seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (selectedShape != null&&progress!=0&&progress!=selectedShape.getLastProgress()){
                    Optional<ShapeView> shapeView = getAllViews().stream()
                            .filter(shape -> shape == selectedShape) // Filtrer pour la forme sélectionnée
                            .findFirst(); // Obtenez la première forme correspondante
                    shapeView.ifPresent(view -> {
                        int delta = progress - view.getLastProgress(); // Calculer le changement en cours
                        view.setSize(selectedShape.getSize() + 4*delta); // delta est négatif, donc il soustrait
                        view.setLastProgress( progress); // Mettre à jour la dernière progression pour le prochain changement
                    });
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        // Bouton pour changer la couleur de la forme sélectionnée
        Button btnChangeColor = findViewById(R.id.btnChangeColor);

        btnChangeColor.setOnClickListener(v -> {
            if (selectedShape != null) {
                selectedShape.setShapeColor(predefinedColors[currentColorIndex]);
                currentColorIndex++;

                if (currentColorIndex >= predefinedColors.length) {
                    currentColorIndex = 0;
                }
                View colorDot = findViewById(R.id.colorDot);
                colorDot.setBackgroundColor(predefinedColors[currentColorIndex]);
            }
        });

        // Bouton pour effacer une forme sélectionnée
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            if (selectedShape != null) {
                canvasLayout.removeView(selectedShape);
                selectedShape = null; // Reset the selected shape
                Toast.makeText(LearningActivity.this, "Shape cleared", Toast.LENGTH_SHORT).show();
            }
        });

        // Bouton pour sauvegarder le canevas comme image
        Button btnSaveCanvas = findViewById(R.id.btnSaveCanvas);
        btnSaveCanvas.setOnClickListener(v -> saveCanvasLayoutAsImage());
    }

    // Réinitialise tous les éléments de l'application
    private void resetEverything() {
        // Stop and reset the timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        timeLeftInMillis = 60000;  // Réinitialise le temps
        updateTimer();

        // Supprime toutes les formes
        canvasLayout.removeAllViews();

        // Activer tous les boutons
        findViewById(R.id.btnAddCircle).setEnabled(true);
        findViewById(R.id.btnAddSquare).setEnabled(true);
        findViewById(R.id.btnAddRectangle).setEnabled(true);
        findViewById(R.id.btnAddTriangle).setEnabled(true);
        findViewById(R.id.btnSaveCanvas).setEnabled(true);
        findViewById(R.id.btnSelectImage).setEnabled(true);
        findViewById(R.id.btnChangeColor).setEnabled(true);

        SeekBar seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSize.setProgress(0);
        seekBarSize.setEnabled(true);

        // Effacer l'image dans ImageView
        imageView.setImageBitmap(null);

        // Réinitialise la sélection
        selectedShape = null;

        // Afficher un message de confirmation
        Toast.makeText(this, "Everything has been reset", Toast.LENGTH_SHORT).show();
    }


    private List<ShapeView> getAllViews() {
        int childCount = canvasLayout.getChildCount(); // Obtenir le nombre de vues des enfants
        List<ShapeView> viewList = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            viewList.add((ShapeView)canvasLayout.getChildAt(i)); // Ajoutez chaque vue enfant à la liste
        }
        return viewList;
    }

    public void saveCanvasLayoutAsImage() {
        // Créer une image bitmap avec la taille du canvasLayout
        Bitmap bitmap = Bitmap.createBitmap(canvasLayout.getWidth(), canvasLayout.getHeight(), Bitmap.Config.ARGB_8888);

        // Créez un canevas qui dessinera sur le bitmap
        Canvas canvas = new Canvas(bitmap);

        // Dessinez la mise en page sur la toile
        canvasLayout.draw(canvas);

        // Définir le répertoire et le nom du fichier
        String folderName = "MyShapes";
        String fileName = "shape_" + System.currentTimeMillis() + ".png"; // Unique file name

        // Obtenir le répertoire de stockage externe pour les images
        File picturesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);

        // Assurez-vous que le répertoire existe
        if (!picturesDir.exists()) {
            if (!picturesDir.mkdirs()) {
                Log.e("ImageUtils", "Failed to create directory: " + picturesDir.getAbsolutePath());
                Toast.makeText(this, "Failed to create directory", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Créer le fichier image
        File imageFile = new File(picturesDir, fileName);

        try (OutputStream outputStream = new FileOutputStream(imageFile)) {
            // Enregistrer le bitmap dans le fichier
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // Save as PNG
            outputStream.flush();

            // Informer la galerie de la nouvelle image
            MediaScannerConnection.scanFile(this, new String[]{imageFile.getAbsolutePath()}, null, (path, uri) -> {
                Log.d("ImageUtils", "Image saved successfully to gallery: " + path);
            });

            Toast.makeText(this, "Canvas saved as image", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("ImageUtils", "Error saving image", e);
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    // Ajoute une nouvelle forme sur le canevas
    @SuppressLint("ClickableViewAccessibility")
    private void addShape(String shapeType) {
        final ShapeView shapeView = new ShapeView(this, shapeType);
        SeekBar seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSize.setProgress(0);
        shapeView.setOnTouchListener(new View.OnTouchListener() {
            float dx, dy;
            // Gère le toucher d'une forme
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = event.getRawX() - v.getX();
                        dy = event.getRawY() - v.getY();
                        selectedShape = (ShapeView) v; // Sélectionne la forme
                        seekBarSize.setProgress(selectedShape.getLastProgress());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        v.setX(event.getRawX() - dx);
                        v.setY(event.getRawY() - dy);
                        break;
                }
                return true;
            }
        });
        canvasLayout.addView(shapeView);
    }

}
