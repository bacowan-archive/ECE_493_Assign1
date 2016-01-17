package com.example.brendan.assignment1.presenter;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.brendan.assignment1.view.observer.BasicObserver;
import com.example.brendan.assignment1.view.observer.MessageType;
import com.example.brendan.assignment1.view.observer.Observable;
import com.example.brendan.assignment1.view.observer.Observer;
import com.example.brendan.assignment1.model.BitmapUtils;
import com.example.brendan.assignment1.model.FilterType;

/**
 * Created by Brendan on 1/13/2016.
 */
public class MainActivityPresenter implements Observer {

    private BasicObserver baseObserver;

    public MainActivityPresenter(Observable mainActivity) {
        baseObserver = new BasicObserver();
        baseObserver.addObservable(mainActivity);
    }

    public void setBitmapFromPath(String path) {
        Bitmap bmp = BitmapUtils.bitmapFromPath(path);
        notifyObservers(MessageType.IMAGE, bmp);
    }

    public void filterImage(Bitmap initialImage, FilterType filterType, int filterSize) {
        FilterParameter parameter = new FilterParameter(initialImage, filterType, filterSize);
        new FilterImageTask().execute(parameter);
    }

    @Override
    public void notifyObservers(MessageType type, Object arg) {
        baseObserver.notifyObservers(type, arg);
    }

    private class FilterImageTask extends AsyncTask<FilterParameter, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(FilterParameter... params) {
            return BitmapUtils.filterImage(params[0].image, params[0].filterType, params[0].filterSize);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            notifyObservers(MessageType.IMAGE, result);
            notifyObservers(MessageType.PROCESSING_DONE, null);
        }
    }

    private class FilterParameter {

        public Bitmap image;
        public FilterType filterType;
        public int filterSize;

        public FilterParameter(Bitmap image, FilterType filterType, int filterSize) {
            this.image = image;
            this.filterType = filterType;
            this.filterSize = filterSize;
        }
    }
}
