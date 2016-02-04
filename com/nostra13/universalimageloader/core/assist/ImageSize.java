package com.nostra13.universalimageloader.core.assist;

public final class ImageSize {
    public final int height;
    public final int width;

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public final String toString() {
        return this.width + "x" + this.height;
    }
}
