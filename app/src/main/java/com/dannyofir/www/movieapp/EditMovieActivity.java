package com.dannyofir.www.movieapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

//This activity is opened when we want to edit a selected movie or add a new movie manually.
public class EditMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);
        changeFont();
        getParentIntent();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void buttonEditMovieCancel_onClick(View view) {
        finish();
    }

    //When the OK button is clicked, all the information in the edittext's are transferred with an intent to the main activity.
    public void buttonEditMovieOk_onClick(View view) {
        try {
            EditText movieName = (EditText) findViewById(R.id.editTextEditMovieName);
            EditText movieAbout = (EditText) findViewById(R.id.editTextEditMovieAbout);
            EditText movieUrl = (EditText) findViewById(R.id.editTextEditMovieUrl);
            EditText movieGenre = (EditText) findViewById(R.id.editTextEditMovieGenre);
            EditText movieDirector = (EditText) findViewById(R.id.editTextEditMovieDirector);
            EditText movieActors = (EditText) findViewById(R.id.editTextEditMovieActors);
            EditText movieRating = (EditText) findViewById(R.id.editTextEditMovieRating);
            EditText movieRuntime = (EditText) findViewById(R.id.editTextEditMovieRuntime);
            EditText movieYear = (EditText) findViewById(R.id.editTextEditMovieYear);
            String name = movieName.getText().toString();
            String about = movieAbout.getText().toString();
            String url = movieUrl.getText().toString();
            String genre = movieGenre.getText().toString();
            String director = movieDirector.getText().toString();
            String actors = movieActors.getText().toString();
            String rating = movieRating.getText().toString();
            String runtime = movieRuntime.getText().toString();
            String year = movieYear.getText().toString();
            //Doesn't allow a movie with an empty name to be created
            if(name.equals("")){
                Toast.makeText(this, "Empty Name! Please Try Again.", Toast.LENGTH_SHORT).show();
                finish();
            }
            Intent intent = new Intent();
            intent.putExtra("Name", name);
            intent.putExtra("About", about);
            intent.putExtra("Url", url);
            intent.putExtra("Genre", genre);
            intent.putExtra("Director", director);
            intent.putExtra("Actors", actors);
            intent.putExtra("Rating", rating);
            intent.putExtra("Runtime", runtime);
            intent.putExtra("Year", year);
            setResult(RESULT_OK, intent);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid Input! Please Try Again.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    //This method gets the parent intent (basically if we decided to edit a movie) and gets all the movies info.
    //All the info is put in the relevant field in our activity
    public void getParentIntent() {
        Intent intent = getIntent();
        EditText editTextName = (EditText) findViewById(R.id.editTextEditMovieName);
        EditText editTextAbout = (EditText) findViewById(R.id.editTextEditMovieAbout);
        EditText editTextUrl = (EditText) findViewById(R.id.editTextEditMovieUrl);
        EditText movieGenre = (EditText) findViewById(R.id.editTextEditMovieGenre);
        EditText movieDirector = (EditText) findViewById(R.id.editTextEditMovieDirector);
        EditText movieActors = (EditText) findViewById(R.id.editTextEditMovieActors);
        EditText movieRating = (EditText) findViewById(R.id.editTextEditMovieRating);
        EditText movieRuntime = (EditText) findViewById(R.id.editTextEditMovieRuntime);
        EditText movieYear = (EditText) findViewById(R.id.editTextEditMovieYear);
        editTextName.setText(intent.getStringExtra("Name"));
        editTextAbout.setText(intent.getStringExtra("About"));
        editTextUrl.setText(intent.getStringExtra("Url"));
        movieGenre.setText(intent.getStringExtra("Genre"));
        movieDirector.setText(intent.getStringExtra("Director"));
        movieActors.setText(intent.getStringExtra("Actors"));
        movieRating.setText(intent.getStringExtra("Rating"));
        movieRuntime.setText(intent.getStringExtra("Runtime"));
        movieYear.setText(intent.getStringExtra("Year"));
        if(intent.getExtras() != null){ //<<<--- This checks if we are editing existing movie or adding a new one. If adding a new one an image with not be shown.
            showImage();
        }
    }


    //This method takes the movie poster url and loads the image in the imageview (using ImageAsyncTask).
    public void showImage(){
        ImageView imageView = (ImageView) findViewById(R.id.imageViewEditMoviePoster);
        EditText editTextUrl = (EditText) findViewById(R.id.editTextEditMovieUrl);
        String imageUrl = editTextUrl.getText().toString();
        ImageAsyncTask imageAsyncTask = new ImageAsyncTask(this, imageView);
        imageAsyncTask.execute(imageUrl);
    }

    public void buttonEditMovieShowImage_onClick(View view) throws MalformedURLException{
            showImage();
    }

    //This method changes the font for the entire layout (where it is needed).
    public void changeFont(){
        AssetManager am = this.getApplicationContext().getAssets();
        Typeface customFont = Typeface.createFromAsset(am, "fonts/Champagne & Limousines Bold.ttf");
        EditText editTextName = (EditText) findViewById(R.id.editTextEditMovieName);
        editTextName.setTypeface(customFont);
        EditText editTextAbout = (EditText) findViewById(R.id.editTextEditMovieAbout);
        editTextAbout.setTypeface(customFont);
        EditText editTextUrl = (EditText) findViewById(R.id.editTextEditMovieUrl);
        editTextUrl.setTypeface(customFont);
        EditText movieGenre = (EditText) findViewById(R.id.editTextEditMovieGenre);
        movieGenre.setTypeface(customFont);
        EditText movieDirector = (EditText) findViewById(R.id.editTextEditMovieDirector);
        movieDirector.setTypeface(customFont);
        EditText movieActors = (EditText) findViewById(R.id.editTextEditMovieActors);
        movieActors.setTypeface(customFont);
        EditText movieRating = (EditText) findViewById(R.id.editTextEditMovieRating);
        movieRating.setTypeface(customFont);
        EditText movieRuntime = (EditText) findViewById(R.id.editTextEditMovieRuntime);
        movieRuntime.setTypeface(customFont);
        EditText movieYear = (EditText) findViewById(R.id.editTextEditMovieYear);
        movieYear.setTypeface(customFont);
        Button show = (Button) findViewById(R.id.buttonEditMovieShowImage);
        show.setTypeface(customFont);
        Button ok = (Button) findViewById(R.id.buttonEditMovieOk);
        ok.setTypeface(customFont);
        Button cancel = (Button) findViewById(R.id.buttonEditMovieCancel);
        cancel.setTypeface(customFont);
    }
}
