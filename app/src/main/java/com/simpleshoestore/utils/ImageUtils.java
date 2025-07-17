package com.simpleshoestore.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static String saveImageToInternalStorage(Context context, Bitmap bitmap) {
        try {
            File directory = new File(context.getFilesDir(), "images");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filename = "shoe_" + System.currentTimeMillis() + ".jpg";
            File file = new File(directory, filename);

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();

            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Bitmap loadImageFromStorage(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        try {
            File file = new File(path);
            if (file.exists()) {
                return BitmapFactory.decodeFile(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}