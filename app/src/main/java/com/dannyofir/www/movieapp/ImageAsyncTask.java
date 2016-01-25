package com.dannyofir.www.movieapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

//This async task receives a url (as a string) of a movie poster, and shows it.
public class ImageAsyncTask extends AsyncTask<String, Void, ImageView> {

    private Activity activity;
    private ImageView imageResult;
    private ProgressDialog dialog;
    private Bitmap bmp;

    public ImageAsyncTask(Activity activity, ImageView imageView) {
        this.activity = activity;
        imageResult = imageView;
    }

    @Override
    protected void onPreExecute() {
        //If the imageview is in the movie edit, it shows the progress dialog.
        //If it is in the main listview for example, it is not shown.
        if(imageResult == activity.findViewById(R.id.imageViewEditMoviePoster)){
            dialog = new ProgressDialog(activity);
            dialog.setTitle("Connecting...");
            dialog.setMessage("Please Wait...");
            dialog.show();
        }
    }

    @Override
    //Basically checks if the image url is valid. if not there is a default image shown. (Online)
    protected ImageView doInBackground(String... params) {
        String url = params[0];
        URL in = null;
        try {
            in = new URL(url);
        } catch (Exception e) {
            try {
                in = new URL("http://www.film.org/Philadelphia/Images/image-not-found.gif");
            } catch (Exception e1) {
                in = null;// doesnt matter - i think
            }
        }
        InputStream inputStream = null;
        try {
            inputStream = in.openStream();
        } catch (Exception e) {
            try{
                in = new URL("http://www.film.org/Philadelphia/Images/image-not-found.gif");
                inputStream = in.openStream();
            } catch (Exception e1) {
                e1.printStackTrace();//doesnt matter - i think
            }
        }
        bmp = BitmapFactory.decodeStream(inputStream);

        return null;
    }

    @Override
    //Here the results of doInBackround are applied to the imageview.
    protected void onPostExecute(ImageView imageView) {
        if(imageResult == activity.findViewById(R.id.imageViewEditMoviePoster)) {
            dialog.dismiss();
        }
        if (bmp != null) {
            imageResult.setImageBitmap(bmp);
            bmp = null;
        }
    }
}
