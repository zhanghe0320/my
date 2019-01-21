package com.onesmock.Util.productList;

import android.graphics.Bitmap;

/**
 * Created by peter on 18/8/6.
 */

public class ImageInfo {
    private String imagePath;
    private Bitmap bitmap;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
