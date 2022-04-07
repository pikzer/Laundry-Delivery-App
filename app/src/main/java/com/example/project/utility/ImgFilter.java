package com.example.project.utility;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

public class ImgFilter {
    public static void grayscaleFilter(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setImageAlpha(128);   // 128 = 0.5
    }
    public static void  unFilter(ImageView v) {
        v.setColorFilter(null);
        v.setImageAlpha(255);
    }
}
