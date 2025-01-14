package com.example.shape_project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class LearningActivity extends AppCompatActivity {

    private ConstraintLayout canvasLayout;
    private ShapeView selectedShape;
    // Predefined basic colors
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
    private int currentColorIndex = 0;
    private ImageView imageView;
    private void openGallery() {
        // Launch gallery to pick an image
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private TextView timerTextView;  // TextView to show the countdown timer
    private CountDownTimer countDownTimer; // Timer object
    private long timeLeftInMillis = 60000; // 1 minute countdown (60,000 milliseconds)

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                Toast.makeText(LearningActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();

                // Disable all shapes on the canvas
                List<ShapeView> allShapes = getAllViews(); // Get all the shapes on the canvas
                for (ShapeView shapeView : allShapes) {
                    shapeView.setEnabled(false); // Disable interaction with the shape
                }

                // Disable the SeekBar and other UI elements
                SeekBar seekBarSize = findViewById(R.id.seekBarSize);
                seekBarSize.setEnabled(false); // Disable SeekBar

                Button btnAddCircle = findViewById(R.id.btnAddCircle);
                Button btnAddSquare = findViewById(R.id.btnAddSquare);
                Button btnAddRectangle = findViewById(R.id.btnAddRectangle);
                Button btnAddTriangle = findViewById(R.id.btnAddTriangle);
                Button btnSaveCanvas = findViewById(R.id.btnSaveCanvas);
                Button btnSelectImage = findViewById(R.id.btnSelectImage);
                Button btnChangeColor = findViewById(R.id.btnChangeColor);

                // Disable all buttons
                btnAddCircle.setEnabled(false);
                btnAddSquare.setEnabled(false);
                btnAddRectangle.setEnabled(false);
                btnAddTriangle.setEnabled(false);
                btnSaveCanvas.setEnabled(true);
                btnSelectImage.setEnabled(false);
//                btnSelectImage.setEnabled(false);
                btnChangeColor.setEnabled(false);
            }

        }.start();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftText = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftText);
    }

    // Activity Result API to handle selected image
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        // Display the selected image in ImageView
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            imageView.setImageBitmap(bitmap);
                            startCountdownTimer();
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
        setContentView(R.layout.activity_learning);
        timerTextView = findViewById(R.id.timerText);
        canvasLayout = findViewById(R.id.canvasLayout);
        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> resetEverything());

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

        // Button to select image
        btnSelectImage.setOnClickListener(v -> openGallery());
        // Slider pour redimensionner la forme sélectionnée
        SeekBar seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (selectedShape != null&&progress!=0&&progress!=selectedShape.getLastProgress()){
                    Optional<ShapeView> shapeView = getAllViews().stream()
                            .filter(shape -> shape == selectedShape) // Filter for the selected shape
                            .findFirst(); // Get the first matching shape
                    shapeView.ifPresent(view -> {
                        int delta = progress - view.getLastProgress(); // Calculate the change in progress
                        view.setSize(selectedShape.getSize() + 4*delta); // delta is negative, so it subtracts
                        view.setLastProgress( progress); // Update last progress for next change
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
                // Set the shape color to the current predefined color
                selectedShape.setShapeColor(predefinedColors[currentColorIndex]);

                // Increment the index to move to the next color
                currentColorIndex++;

                // If the index exceeds the number of colors, reset it to 0 (start from the beginning)
                if (currentColorIndex >= predefinedColors.length) {
                    currentColorIndex = 0;
                }
                View colorDot = findViewById(R.id.colorDot);
                colorDot.setBackgroundColor(predefinedColors[currentColorIndex]);
            }
        });

        // Button to clear the selected shape
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            if (selectedShape != null) {
                // Remove the selected shape from the layout
                canvasLayout.removeView(selectedShape);
                selectedShape = null; // Reset the selected shape
                Toast.makeText(LearningActivity.this, "Shape cleared", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSaveCanvas = findViewById(R.id.btnSaveCanvas);
        btnSaveCanvas.setOnClickListener(v -> saveCanvasLayoutAsImage());

        // Update the color dot on the button

    }

    private void resetEverything() {
        // Stop and reset the timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        timeLeftInMillis = 60000; // Reset to 1 minute
        updateTimer(); // Update the timer display

        // Clear all shapes from the canvas
        canvasLayout.removeAllViews();

        // Enable all buttons
        findViewById(R.id.btnAddCircle).setEnabled(true);
        findViewById(R.id.btnAddSquare).setEnabled(true);
        findViewById(R.id.btnAddRectangle).setEnabled(true);
        findViewById(R.id.btnAddTriangle).setEnabled(true);
        findViewById(R.id.btnSaveCanvas).setEnabled(true);
        findViewById(R.id.btnSelectImage).setEnabled(true);
        findViewById(R.id.btnChangeColor).setEnabled(true);

        // Reset the SeekBar
        SeekBar seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSize.setProgress(0);
        seekBarSize.setEnabled(true);

        // Clear the image in the ImageView
        imageView.setImageBitmap(null);

        // Reset selected shape
        selectedShape = null;

        // Show a confirmation message
        Toast.makeText(this, "Everything has been reset", Toast.LENGTH_SHORT).show();
    }


    private List<ShapeView> getAllViews() {
        int childCount = canvasLayout.getChildCount(); // Get the number of child views
        List<ShapeView> viewList = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            viewList.add((ShapeView)canvasLayout.getChildAt(i)); // Add each child view to the list
        }
        return viewList;
    }

    public void saveCanvasLayoutAsImage() {
        // Create a Bitmap with the size of the canvasLayout
        Bitmap bitmap = Bitmap.createBitmap(canvasLayout.getWidth(), canvasLayout.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a Canvas that will draw onto the Bitmap
        Canvas canvas = new Canvas(bitmap);

        // Draw the layout onto the canvas
        canvasLayout.draw(canvas);

        // Define the directory and file name
        String folderName = "MyShapes";
        String fileName = "shape_" + System.currentTimeMillis() + ".png"; // Unique file name

        // Get the external storage directory for pictures
        File picturesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName);

        // Ensure the directory exists
        if (!picturesDir.exists()) {
            if (!picturesDir.mkdirs()) {
                Log.e("ImageUtils", "Failed to create directory: " + picturesDir.getAbsolutePath());
                Toast.makeText(this, "Failed to create directory", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Create the image file
        File imageFile = new File(picturesDir, fileName);

        try (OutputStream outputStream = new FileOutputStream(imageFile)) {
            // Save the bitmap to the file
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // Save as PNG
            outputStream.flush();

            // Notify the gallery about the new image
            MediaScannerConnection.scanFile(this, new String[]{imageFile.getAbsolutePath()}, null, (path, uri) -> {
                Log.d("ImageUtils", "Image saved successfully to gallery: " + path);
            });

            Toast.makeText(this, "Canvas saved as image", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("ImageUtils", "Error saving image", e);
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private void addShape(String shapeType) {
        final ShapeView shapeView = new ShapeView(this, shapeType);
        SeekBar seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSize.setProgress(0);
        shapeView.setOnTouchListener(new View.OnTouchListener() {
            float dx, dy;
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
