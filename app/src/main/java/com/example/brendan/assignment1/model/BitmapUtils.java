package com.example.brendan.assignment1.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.brendan.assignment1.Exceptions.InvalidFilterType;

/**
 * Created by Brendan on 1/13/2016.
 */
public class BitmapUtils {

    public static Bitmap bitmapFromPath(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static Bitmap filterImage(Bitmap image, FilterType filterType, int filterSize) {
        return createFilter(filterType, filterSize).filterImage(image);
    }

    private static ImageFilter createFilter(FilterType filterType, int filterSize) {
        return new ImageFilter(createSectionFilter(filterType), filterSize);
    }

    private static ImageSectionFilter createSectionFilter(FilterType filterType) {
        if (filterType.equals(FilterType.MEAN))
            return new MeanImageSectionFilter();
        if (filterType.equals(FilterType.MEDIAN))
            return new MedianImageSectionFilter();
        throw new InvalidFilterType(filterType.name());
    }

}
