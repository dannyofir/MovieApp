package com.dannyofir.www.movieapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

//The activity that searches for movies with the omdb api database, and returns movies that are close to the search word.
//All close movies are put in the list as movie objects, and then when clicked sent to the main activity as intents.
public class AddMovieInternetActivity extends AppCompatActivity {

    private ListView listViewInternetSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie_internet);
        changeFont();
        listViewInternetSearch = (ListView) findViewById(R.id.listViewInternetSearch);
        onListViewItemClick();
    }

    //On Click for the "Go" search button. takes what we want to search for and sends it to the SearchAsyncTask.
    public void buttonAddMovieInternetSearch_onClick(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextAddMovieInternetSearch);
        InputMethodManager manager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        String search = editText.getText().toString();
        SearchAsyncTask searchAsyncTask = new SearchAsyncTask(this);
        searchAsyncTask.execute(search);
    }

    public void buttonAddMovieInternetCancel_onClick(View view) {
        finish();
    }

    //This method sets the search listview so it is pressable and what happens when it is pressed (sends movie to main activity and adds to the main list).
    public void onListViewItemClick() {

        listViewInternetSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie selectedMovie = (Movie) adapterView.getItemAtPosition(i); //Ask Assaf if this is right
                String name = selectedMovie.getName();
                String about = selectedMovie.getAbout();
                String url = selectedMovie.getUrl();
                String genre = selectedMovie.getGenre();
                String director = selectedMovie.getDirector();
                String actors = selectedMovie.getActors();
                String rating = selectedMovie.getRating();
                String runtime = selectedMovie.getRuntime();
                String year = selectedMovie.getYear();
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
                finish();
            }
        });
    }

    //This method changes the font for the entire layout (where it is needed).
    public void changeFont() {
        AssetManager am = this.getApplicationContext().getAssets();
        Typeface customFont = Typeface.createFromAsset(am, "fonts/Champagne & Limousines Bold.ttf");
        TextView textViewSearch = (TextView) findViewById(R.id.textViewAddMovieInternetSearch);
        textViewSearch.setTypeface(customFont);
        EditText editTextSearch = (EditText) findViewById(R.id.editTextAddMovieInternetSearch);
        editTextSearch.setTypeface(customFont);
        Button go = (Button) findViewById(R.id.buttonAddMovieInternetSearch);
        go.setTypeface(customFont);
        Button cancel = (Button) findViewById(R.id.buttonAddMovieInternetCancel);
        cancel.setTypeface(customFont);
    }

}
