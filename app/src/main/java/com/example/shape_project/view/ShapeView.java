package com.example.shape_project.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class ShapeView extends View {
    private String shapeType; // Type de forme à dessiner (cercle, carré, rectangle, triangle)
    private Paint paint; // Objet Paint pour définir les propriétés graphiques (couleur, style, etc.)
    private int lastProgress = 0; // Dernier niveau de progression utilisé (peut être lié à un curseur ou une échelle)
    private int size = 100; // Taille de la forme dessinée (largeur et hauteur en pixels)

    // Getter pour récupérer la dernière progression
    public int getLastProgress() {
        return lastProgress;
    }

    // Setter pour mettre à jour la dernière progression
    public void setLastProgress(int lastProgress) {
        this.lastProgress = lastProgress;
    }

    // Getter pour récupérer la taille de la forme
    public int getSize() {
        return size;
    }

    // Méthode pour afficher les détails de l'objet ShapeView en chaîne de caractères
    @Override
    public String toString() {
        return "ShapeView{" +
                "shapeType='" + shapeType + '\'' +
                ", paint=" + paint +
                ", size=" + size +
                '}';
    }

    // Constructeur personnalisé prenant un contexte et le type de forme comme paramètres
    public ShapeView(Context context, String shapeType) {
        super(context);
        this.shapeType = shapeType;
        init(); // Initialisation des propriétés
    }

    // Constructeur prenant un contexte et des attributs (utilisé pour les layouts XML)
    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(); // Initialisation des propriétés
    }

    // Méthode d'initialisation des propriétés graphiques
    private void init() {
        paint = new Paint(); // Création d'un objet Paint
        paint.setColor(Color.BLUE); // Définition de la couleur par défaut (bleu)
        paint.setStyle(Paint.Style.FILL); // Remplissage complet de la forme
    }

    // Méthode pour modifier la taille de la forme
    public void setSize(int newSize) {
        this.size = newSize; // Met à jour la taille
        requestLayout(); // Demande une nouvelle mesure
        invalidate(); // Redessine la vue avec la nouvelle taille
    }

    // Méthode pour mesurer la taille de la vue en fonction de la taille spécifiée
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(size, size); // Définit la taille mesurée de la vue
    }

    // Méthode pour dessiner la forme sur le canevas
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth(); // Largeur actuelle de la vue
        int height = getHeight(); // Hauteur actuelle de la vue

        // Dessine la forme en fonction de son type
        switch (shapeType) {
            case "circle": // Dessin d'un cercle
                canvas.drawCircle(width / 2f, height / 2f, Math.min(width, height) / 2f, paint);
                break;
            case "square": // Dessin d'un carré
                canvas.drawRect(0, 0, width, height, paint);
                break;
            case "rectangle": // Dessin d'un rectangle
                canvas.drawRect(0, 0, width, height / 2f, paint);
                break;
            case "triangle": // Dessin d'un triangle
                Path path = new Path();
                path.moveTo(width / 2f, 0); // Sommet supérieur du triangle
                path.lineTo(0, height); // Coin inférieur gauche
                path.lineTo(width, height); // Coin inférieur droit
                path.close(); // Ferme le chemin
                canvas.drawPath(path, paint);
                break;
        }
    }

    // Méthode pour changer la couleur de la forme
    public void setShapeColor(int color) {
        paint.setColor(color); // Change la couleur du pinceau
        invalidate(); // Redessine la vue avec la nouvelle couleur
    }
}
