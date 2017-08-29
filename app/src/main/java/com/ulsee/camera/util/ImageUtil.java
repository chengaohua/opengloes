package com.ulsee.camera.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by uriah on 17-8-28.
 */

public class ImageUtil {

    public static Bitmap getRotateBitmap(Bitmap bitmap, float rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }
}
