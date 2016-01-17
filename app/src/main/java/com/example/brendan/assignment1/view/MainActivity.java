package com.example.brendan.assignment1.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.brendan.assignment1.view.observer.MessageType;
import com.example.brendan.assignment1.view.observer.Observable;
import com.example.brendan.assignment1.R;
import com.example.brendan.assignment1.presenter.MainActivityPresenter;
import com.example.brendan.assignment1.view.preferences.PreferenceParser;

public class MainActivity extends AppCompatActivity implements Observable {

    private static int RESULT_LOAD_IMAGE = 1;
    private Button loadImageButton;
    private Button filterButton;
    private ImageView imageView;
    private Bitmap image;
    private PreferenceParser preferenceParser;

    private MainActivityPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainActivityPresenter(this);
        preferenceParser = new PreferenceParser(this);

        setLayoutViews();
        requestImageLoadPermissions();
        initializeLoadButton();
        initializeFilterButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        imageView.setImageBitmap(image);
    }

    private void setLayoutViews() {
        loadImageButton = (Button) findViewById(R.id.load_button);
        filterButton = (Button) findViewById(R.id.filter_button);
        imageView = (ImageView) findViewById(R.id.image_view);
    }

    private void initializeLoadButton() {
        loadImageButton.setOnClickListener(new LoadButtonClickListener());
    }

    private void initializeFilterButton() {
        filterButton.setOnClickListener(new FilterButtonClickListener());
    }

    private void requestImageLoadPermissions() {
        String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE"};
        int permsRequestCode = 100;
        ActivityCompat.requestPermissions(this, permissions, permsRequestCode);
    }

    // After the load image dialogue box has been closed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            presenter.setBitmapFromPath(picturePath);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return startSettingsActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }

    // Notify the front end that the displayed image should change.
    @Override
    public void notifyObservable(MessageType type, Object arg) {
        if (type == MessageType.IMAGE) {
            image = (Bitmap) arg;
            imageView.setImageBitmap(image);
        }
        else if (type == MessageType.PROCESSING_DONE) {
            enableButtons();
        }
    }

    private class LoadButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }
    }

    private class FilterButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (validateSettings()) {
                presenter.filterImage(image, preferenceParser.getFilterType(), preferenceParser.getFilterSize());
                disableButtons();
            }
        }
    }

    private boolean validateSettings() {
        if (image == null) {
            toast("No image loaded");
            return false;
        }
        else if (preferenceParser.getFilterType() == null) {
            toast("No filter type was selected");
            return false;
        }
        else if (!isOdd(preferenceParser.getFilterSize())) {
            toast("Filter size must be odd");
            return false;
        }
        else if (filterTooLarge(preferenceParser.getFilterSize())) {
            toast("Filter size must be smaller than image");
            return false;
        }
        else if (preferenceParser.getFilterSize() < 1) {
            toast("Filter size must be positive");
            return false;
        }
        return true;
    }

    private boolean isOdd(int val) {
        return val%2 == 1;
    }

    private boolean filterTooLarge(int val) {
        return val > image.getHeight() || val > image.getWidth();
    }

    private void toast(String text) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    private void disableButtons() {
        filterButton.setEnabled(false);
        loadImageButton.setEnabled(false);
    }

    private void enableButtons() {
        filterButton.setEnabled(true);
        loadImageButton.setEnabled(true);
    }

}
